package test;

import api.android.Android;
import core.managers.DriverManager;
import org.testng.annotations.Test;

import java.net.MalformedURLException;


public class Runner extends Hook {

    @Test
    public void testFirst() throws MalformedURLException {
        Android.apps.speedTest.beginTest.tapBeginTestBtn();
        Android.apps.speedTest.home.tapTestAgainBtn();
        Android.apps.speedTest.forceStop();
    }
}
