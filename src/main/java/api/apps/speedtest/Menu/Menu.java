package api.apps.speedtest.Menu;

import api.android.Android;
import api.apps.speedtest.Home.Home;
import org.apache.log4j.Logger;

public class Menu {

    private Logger logger = Logger.getLogger(Menu.class);

    private MenuUiObjects uiObjects = new MenuUiObjects();

    public Home tapSpeedTestBtn() {
        logger.info("Tapping on speedTest button");
        uiObjects.speedTestBtn().tap();
        return Android.apps.speedTest.home.waitToload();
    }
}
