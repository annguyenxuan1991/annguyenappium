package api.apps.speedtest.BeginTest;

import api.android.Android;
import api.apps.speedtest.Home.Home;
import api.interfaces.Activity;
import org.apache.log4j.Logger;

import java.util.NoSuchElementException;

public class BeginTest implements Activity {

    private Logger logger = Logger.getLogger(BeginTest.class);

    private BeginTestUiObjects uiObjects = new BeginTestUiObjects();

    public Home tapBeginTestBtn() {
        try {
            logger.info("Tapping on Begin Test button");
            uiObjects.beginTestBtn().waitToAppear(120).tap();
            return Android.apps.speedTest.home;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Cant tap Begin Test button, element absent or blocked");
        }


    }

    public BeginTest waitToload() {
        try {
            logger.info("Waiting for Begin Test activity");
            uiObjects.beginTestBtn().waitToAppear(10);
            return Android.apps.speedTest.beginTest;
        } catch (AssertionError e) {
            throw new AssertionError("Begin Test activity failed to load/open");
        }

    }
}
