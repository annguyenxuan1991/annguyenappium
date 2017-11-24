package core.Managers;

import core.ADB;
import core.Constants.Arg;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.service.DriverService;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
        caps.setCapability("deviceName", deviceID);
        caps.setCapability("platformName", "Android");
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
                    .withArgument(Arg.LOG_LEVEL, "warn")
                    .build();
        } else {
            logger.error("Starting appium service failed due to un-supported OS or for some unexpected error");
        }
        return service;
    }


}
