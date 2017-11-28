package core.managers;

import core.ADB;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.service.DriverService;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ConcurrentHashMap;

public class ParallelManager {

    private static ConcurrentHashMap<String, AndroidDriver> driverPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, DriverService> servicePool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, ADB> adbPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, String> deviceIDPool = new ConcurrentHashMap<>();
    private static ConcurrentHashMap<String, URL> urlPool = new ConcurrentHashMap<>();

    public static void setDriver(AndroidDriver driver) {
        driverPool.put(Thread.currentThread().getName(), driver);
    }


    public static AndroidDriver getDriver() {
        return driverPool.get(Thread.currentThread().getName());
    }

    public static void freeDriverInstance() {
        driverPool.get(Thread.currentThread().getName()).close();
    }

    public static void setService(DriverService service) {
        servicePool.put(Thread.currentThread().getName(), service);
    }


    public static DriverService getService() {
        return servicePool.get(Thread.currentThread().getName());
    }

    public static void startService() throws IOException {
        servicePool.get(Thread.currentThread().getName()).start();
    }

    public static void stopService() {
        servicePool.get(Thread.currentThread().getName()).stop();
    }

    public static void setADB(ADB adb) {
        adbPool.put(Thread.currentThread().getName(), adb);
    }


    public static ADB getADB() {
        return adbPool.get(Thread.currentThread().getName());
    }

    public static void setDeviceID(String deviceID) {
        deviceIDPool.put(Thread.currentThread().getName(), deviceID);
    }


    public static String getDeviceID() {
        return deviceIDPool.get(Thread.currentThread().getName());
    }

    public static void setUrl(URL url) {
        urlPool.put(Thread.currentThread().getName(), url);
    }


    public static URL getUrl() {
        return urlPool.get(Thread.currentThread().getName());
    }

}
