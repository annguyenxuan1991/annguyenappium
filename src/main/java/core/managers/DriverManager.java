package core.managers;

import api.android.Android;
import core.ADB;
import core.constants.Arg;
import core.constants.Resources;
import core.Timer;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class DriverManager {

    private static Logger logger = Logger.getLogger(DriverManager.class);

    private static String nodeJS = "/usr/local/bin/node";
    private static String appiumJS = "/Users/khanhnxb-mc/node_modules/appium/build/lib/main.js";

    private static String deviceId;
    private static URL host;
    private static DriverService service;

    public static DesiredCapabilities getDefaultCapsAndroid(String deviceID){
        logger.info("Creating driver caps for device: "+deviceID);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("version", new ADB(deviceID).getAndroidVersion());
        caps.setCapability("deviceName", deviceID);
        caps.setCapability("platformName", "Android");
        caps.setCapability("appPackage", "org.zwanoo.android.speedtest");
        caps.setCapability("appActivity", "com.ookla.speedtest.softfacade.MainActivity");
        return caps;
    }

    private static String getRandomPort(int fromPort, int toPort) {
        return Integer.toString(new Random().nextInt((toPort - fromPort) + 1) + fromPort);
    }

    public static URL host() throws MalformedURLException {
            return host = new URL("http://127.0.0.1:"+getRandomPort(4720, 4730)+"/wd/hub");
    }

    public static DriverService createService() throws MalformedURLException {
        String osName = System.getProperty("os.name");

        if (osName.contains("Windows")) {
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File("node.exe path"))
                    .withAppiumJS(new File("appium.js path"))
                    .withLogFile(new File(new File("log"), "androidLog.txt")));
        } else if (osName.contains("Mac")) {
            logger.info("Creating Appium service on MAC with Url: http://127.0.0.1 and Port: "+
                    Integer.parseInt(host().toString().split(":")[2].replace("/wd/hub","")));
            service = new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(nodeJS))
                    .withAppiumJS(new File(appiumJS))
                    .withIPAddress(host.toString().split(":")[1].replace("//", ""))
                    .usingPort(Integer.parseInt(host.toString().split(":")[2].replace("/wd/hub","")))
                    .withArgument(Arg.TIMEOUT, "120")
                    .withArgument(GeneralServerFlag.LOG_LEVEL, "warn")
                    .build();
        } else {
            logger.error("Starting appium service failed due to un-supported OS or for some unexpected error");
        }
        return service;
    }

    public static void createDriver() throws MalformedURLException {
        ArrayList<String> devices = getAvailableDevices();
        for(String device : devices){
            try{
                deviceId = device;
                if(useDevice(deviceId)){
                    queueUp();
                    gracePeriod();
                    logger.info("Trying to create new Driver for device: "+device);
                    ParallelManager.getService().start();
                    ParallelManager.setDriver(new AndroidDriver(host, getDefaultCapsAndroid(deviceId)));
                    leaveQueue();
                }
            }catch (Exception e){
                e.printStackTrace();
                //Ignore and try next device
            }
        }
    }
    public static void killDriver(){
        if(Android.driver != null){
            logger.info("Killing Android Driver");
            ParallelManager.freeDriverInstance();
            ParallelManager.stopService();
        } else logger.info("Android Driver is not initialized, nothing to kill");
    }


    private static ArrayList<String> getAvailableDevices(){
        logger.info("Checking for available devices");
        ArrayList<String> availableDevices = new ArrayList<>();
        ArrayList connectedDevices = ADB.getConnectedDevices();
        for(Object connectedDevice: connectedDevices){
            String device = connectedDevice.toString();
            if(useDevice(deviceId)) availableDevices.add(device);
            else logger.info("Device: "+deviceId+" is being used by another JVM");
        }
        if(availableDevices.size() == 0) throw new RuntimeException("Not a single device is available for testing at this time");
        return availableDevices;
    }

    private static boolean useDevice(String deviceID) {
            JSONObject json = Resources.getQueue();
            if(json.containsKey(deviceID)){
                JSONObject deviceJson = (JSONObject) json.get(deviceID);
                long time = (long) deviceJson.get("queued_at");
                int diff = Timer.getDifference(time, Timer.getTimeStamp());
                if(diff >= 30) return true;
                else return false;
            } else return true;
    }

    private static void queueUp() {
        try {
            logger.info("Queueing Up: "+deviceId);
            JSONObject json = new JSONObject();
            json.put("queued_at", Timer.getTimeStamp());
            JSONObject jsonQueue = Resources.getQueue();
            jsonQueue.put(deviceId, json);
            logger.info("JSON Queue: "+jsonQueue);
            ServerManager.writeFile(new File(Resources.QUEUE), jsonQueue.toString());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    private static void gracePeriod(){
        int waitTime = 0;
        try {
            JSONObject  json = Resources.getQueue();
            Set keys = json.keySet();

            JSONObject ourDeviceJson = (JSONObject) json.get(deviceId);
            json.remove(deviceId);
            long weQueuedAt = (long) ourDeviceJson.get("queued_at");

            for(Object key : keys){
                JSONObject deviceJson = (JSONObject) json.get(key);
                long theyQueuedAt = (long) deviceJson.get("queued_at");
                //If we did not queue first we need to wait for the other device to initialize driver so there is no collision
                if(weQueuedAt > theyQueuedAt) {
                    //Be queued first and recently, otherwise we can assume device was already initialized or no longer being used
                    int diff = Timer.getDifference(theyQueuedAt, Timer.getTimeStamp());
                    if(diff < 50){
                        logger.info("Device: "+key+" queued first, I will need to give it extra time to initialize");
                        waitTime += 15;
                    }
                }
            }
            try {Thread.sleep(waitTime);} catch (InterruptedException e) {e.printStackTrace();}
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    private static void leaveQueue(){
        try {
            JSONObject jsonQueue = Resources.getQueue();
            jsonQueue.remove(deviceId);
            ServerManager.writeFile(new File(Resources.QUEUE), jsonQueue.toString());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
