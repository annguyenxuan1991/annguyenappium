package test;

import api.android.Android;
import core.Managers.DriverManager;

import java.net.MalformedURLException;

public class Runner {

    public static void main(String[] args) throws MalformedURLException {


        DriverManager.createDriver();
        Android.apps.speedTest.beginTest.tapBeginTestBtn();
        Android.apps.speedTest.home.tapTestAgainBtn();
        Android.apps.speedTest.forceStop();
        DriverManager.killDriver();
    }
}
