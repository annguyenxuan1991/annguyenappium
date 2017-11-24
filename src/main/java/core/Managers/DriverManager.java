package core.Managers;

import api.android.Android;
import core.ADB;
import core.Constants.Arg;
import core.Constants.Resources;
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
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class DriverManager {

    private static Logger logger = Logger.getLogger(DriverManager.class);

    private static String nodeJS = "/usr/local/bin/node";
    private static String appiumJS = "/Users/khanhnxb-mc/node_modules/appium/build/lib/main.js";

    private static String deviceId = (String) ADB.getConnectedDevices().get(0);
    private static Map<String, URL> hosts;
    private static DriverService service;

    private static DesiredCapabilities getDefaultCapsAndroid(String deviceID){
        logger.info("Creating driver caps for device: "+deviceID);
        DesiredCapabilities caps = new DesiredCapabilities();
        caps.setCapability("version", new ADB(deviceID).getAndroidVersion());
        caps.setCapability("deviceName", deviceID);
        caps.setCapability("platformName", "Android");
        caps.setCapability("appPackage", "org.zwanoo.android.speedtest");
        caps.setCapability("appActivity", "com.ookla.speedtest.softfacade.MainActivity");
        return caps;
    }

    private static URL host(String deviceId) throws MalformedURLException {
        if(hosts == null) {
            hosts = new HashMap<String, URL>();
            hosts.put(deviceId, new URL("http://127.0.0.1:4723/wd/hub"));
        }
        return hosts.get(deviceId);
    }

    public static DriverService createService() throws MalformedURLException {
        String osName = System.getProperty("os.name");

        if (osName.contains("Windows")) {
            service = AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
                    .usingDriverExecutable(new File("node.exe path"))
                    .withAppiumJS(new File("appium.js path"))
                    .withLogFile(new File(new File("log"), "androidLog.txt")));

        } else if (osName.contains("Mac")) {
            System.out.println(Arg.LOCATL_TIME_ZONE);
            service = new AppiumServiceBuilder()
                    .usingDriverExecutable(new File(nodeJS))
                    .withAppiumJS(new File(appiumJS))
                    .withIPAddress(host(deviceId).toString().split(":")[1].replace("//", ""))
                    .usingPort(Integer.parseInt(host(deviceId).toString().split(":")[2].replace("/wd/hub","")))
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
                    createService().start();
                    Android.driver = new AndroidDriver(host(device), getDefaultCapsAndroid(device));
                    Android.adb = new ADB(device);
                    leaveQueue();
                    break;
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
            Android.driver.quit();
            service.stop();
        } else logger.info("Android Driver is not initialized, nothing to kill");
    }


    private static ArrayList<String> getAvailableDevices(){
        logger.info("Checking for available devices");
        ArrayList<String> availableDevices = new ArrayList<String>();
        ArrayList connectedDevices = ADB.getConnectedDevices();
        for(Object connectedDevice: connectedDevices){
            String device = connectedDevice.toString();
            ArrayList apps = new ADB(device).getInstalledPackages();
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
    }    public static void leaveQueue(){
        try {
            JSONObject jsonQueue = Resources.getQueue();
            jsonQueue.remove(deviceId);
            ServerManager.writeFile(new File(Resources.QUEUE), jsonQueue.toString());
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

}
