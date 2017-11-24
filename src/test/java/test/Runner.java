package test;

import api.android.Android;
import core.ADB;
import core.Managers.DriverManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Runner {
    @Test
    public void Test1() throws IOException {

        DriverManager.createService().start();
        ADB adb = new ADB((String) ADB.getConnectedDevices().get(0));

        DesiredCapabilities caps =new DesiredCapabilities();
        caps.setCapability("version", adb.getAndroidVersion());
        caps.setCapability("deviceName", adb.getDeviceModel());
        caps.setCapability("appPackage", "org.zwanoo.android.speedtest");
        caps.setCapability("appActivity", "com.ookla.speedtest.softfacade.MainActivity");

        Android.driver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), caps);

        Android.apps.speedTest.beginTest.tapBeginTestBtn();
        Android.apps.speedTest.home.tapTestAgainBtn();
        Android.apps.speedTest.forceStop();
    }
}
