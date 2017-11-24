package api.apps.speedtest;

import api.android.Android;
import api.apps.speedtest.BeginTest.BeginTest;
import api.apps.speedtest.Home.Home;
import api.apps.speedtest.Menu.Menu;
import api.interfaces.Application;

public class SpeedTestApp implements Application {

    public Home home = new Home();
    public Menu menu = new Menu();
    public BeginTest beginTest = new BeginTest();

    public void open() {
        Android.adb.openAppsActivity(packageID(), activityID());
    }

    public void forceStop() {
        Android.adb.forceStopApp(packageID());
    }

    public void clearData() {
        Android.adb.clearAppsData(packageID());
    }

    public String packageID() {
        return SpeedTestConstants.PACKAGE_ID;
    }

    public String activityID() {
        return SpeedTestConstants.ACTIVITY_ID;
    }
}
