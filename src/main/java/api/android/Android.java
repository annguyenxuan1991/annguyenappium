package api.android;

import api.apps.Apps;
import core.ADB;
import core.managers.ParallelManager;
import io.appium.java_client.android.AndroidDriver;

public class Android {

    public static AndroidDriver driver = ParallelManager.getDriver();
    public static ADB adb;
    public static Apps apps = new Apps();

}
