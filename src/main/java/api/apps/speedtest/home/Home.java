package api.apps.speedtest.home;

import api.android.Android;
import api.apps.speedtest.menu.Menu;
import api.interfaces.Activity;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;

public class Home implements Activity {
    private Logger logger = Logger.getLogger(Menu.class);

    private HomeUiObjects uiObjects = new HomeUiObjects();

    public Home waitToload() {
        try {
            logger.info("Waiting for home activity");
            uiObjects.testAgainBtn().waitToAppear(10);
            return Android.apps.speedTest.home;
        } catch (AssertionError e) {
            throw new AssertionError("home activity failed to load/open");
        }
    }

    public Home tapTestAgainBtn() {
        try {
            logger.info("Tapping on Test Again button");
            uiObjects.testAgainBtn().waitToAppear(120).tap();
            return Android.apps.speedTest.home;
        } catch (NoSuchElementException e){
            throw new AssertionError("Cant tap Test Again button, element absent or blocked");
        }
    }

}
