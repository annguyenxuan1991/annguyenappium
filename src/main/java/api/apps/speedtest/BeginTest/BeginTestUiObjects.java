package api.apps.speedtest.BeginTest;

import core.UiObject;
import core.UiSelector;

public class BeginTestUiObjects {
    private static String beginTestText = "Begin Test";

    private UiObject beginTestBtn;

    public UiObject beginTestBtn() {
        if(beginTestBtn == null) beginTestBtn = new UiSelector().text(beginTestText).makeUiObject();
        return beginTestBtn;
    }
}
