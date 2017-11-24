package api.apps.speedtest.home;

import core.UiObject;
import core.UiSelector;

public class HomeUiObjects {

    private static String logoId = "org.zwanoo.android.speedtest:id/logo";
    private static String pingText = "PING";
    private static String downloadText = "DOWNLOAD";
    private static String uploadText = "UPLOAD";
    private static String pingSpeedId = "org.zwanoo.android.speedtest:id/pingSpeed";
    private static String downLoadSpeedId = "org.zwanoo.android.speedtest:id/downloadSpeed";
    private static String uploadSpeedId = "org.zwanoo.android.speedtest:id/uploadSpeed";
    private static String testAgainText = "Test Again";
    private static String removeAdsText = "Remove Ads";
    private static String shareId = "org.zwanoo.android.speedtest:id/shareButton";

    private UiObject
        logo,
        ping,
        download,
        upload,
        pingSpeed,
        downloadSpeed,
        uploadSpeed,
        testAgainBtn,
        removeAdsBtn,
        shareBtn;

    public UiObject logo() {
        if(logo == null) logo = new UiSelector().resourceId(logoId).makeUiObject();
        return logo;
    }

    public UiObject ping() {
        if(ping == null) ping = new UiSelector().text(pingText).makeUiObject();
        return ping;
    }

    public UiObject download() {
        if(download == null) download = new UiSelector().text(downloadText).makeUiObject();
        return download;
    }

    public UiObject upload() {
        if(upload == null) upload = new UiSelector().text(uploadText).makeUiObject();
        return upload;
    }

    public UiObject pingSpeed() {
        if(pingSpeed == null) pingSpeed = new UiSelector().resourceId(pingSpeedId).makeUiObject();
        return pingSpeed;
    }

    public UiObject downloadSpeed() {
        if(downloadSpeed == null) downloadSpeed = new UiSelector().resourceId(downLoadSpeedId).makeUiObject();
        return downloadSpeed;
    }

    public UiObject uploadSpeed() {
        if(uploadSpeed == null) uploadSpeed = new UiSelector().resourceId(uploadSpeedId).makeUiObject();
        return uploadSpeed;
    }

    public UiObject testAgainBtn() {
        if(testAgainBtn == null) testAgainBtn = new UiSelector().text(testAgainText).makeUiObject();
        return testAgainBtn;
    }

    public UiObject removeAdsBtn() {
        if(removeAdsBtn == null) removeAdsBtn = new UiSelector().text(removeAdsText).makeUiObject();
        return removeAdsBtn;
    }

    public UiObject shareBtn() {
        if(shareBtn == null) shareBtn = new UiSelector().resourceId(shareId).makeUiObject();
        return shareBtn;
    }

}
