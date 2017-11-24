package api.apps.speedtest.menu;

import core.UiObject;
import core.UiSelector;

public class MenuUiObjects {

    private static String speedTestText = "SPEEDTEST";
    private static String resultsText = "RESULTS";
    private static String settingsText = "SETTINGS";
    private static String vpnText = "VPN";

    private UiObject
        speedTest,
        results,
        settings,
        vpn;

    public UiObject speedTestBtn() {
        if(speedTest == null) speedTest = new UiSelector().text(speedTestText).makeUiObject();
        return speedTest;
    }

    public UiObject resultsBtn() {
        if(results == null) results = new UiSelector().text(resultsText).makeUiObject();
        return results;
    }

    public UiObject settingsBtn() {
        if(settings == null) settings = new UiSelector().text(settingsText).makeUiObject();
        return settings;
    }

    public UiObject vpnBtn() {
        if(vpn == null) vpn = new UiSelector().text(vpnText).makeUiObject();
        return vpn;
    }
}
