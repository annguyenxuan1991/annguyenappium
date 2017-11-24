package core;

import api.android.Android;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class UiObject {

    private String locator;
    private static Logger logger = Logger.getLogger(UiObject.class);

    public UiObject(String locator) {
        this.locator = locator;
        logger.debug("Creating UiObject...");
    }

    private boolean isXpath() {
        return !locator.contains("UiSelector");
    }

    public boolean isExists() {
        try {
            WebElement element;
            if(isXpath()) element = Android.driver.findElementByXPath(locator);
            else element = Android.driver.findElementByAndroidUIAutomator(locator);
            return element.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }

    }

    public boolean isChecked() {
        WebElement webElement;
        if(isXpath()) webElement = Android.driver.findElementByXPath(locator);
        else webElement = Android.driver.findElementByAndroidUIAutomator(locator);
        return webElement.getAttribute("checked").equals("true");
    }

    public boolean isCheckable(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("checkable").equals("true");
    }

    public boolean isClickable(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("clickable").equals("true");
    }

    public boolean isEnabled(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("enabled").equals("true");
    }

    public boolean isFocusable(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("focusable").equals("true");
    }

    public boolean isFocused(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("focused").equals("true");
    }

    public boolean isScrollable(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("scrollable").equals("true");
    }

    public boolean isLongClickable(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("longClickable").equals("true");
    }

    public boolean isSelected(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("selected").equals("true");
    }

    public Point getLocation(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getLocation();
    }

    public String getText(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("name");
    }

    public String getResourceId(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("resourceId");
    }

    public String getClassName(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("className");
    }

    public String getContentDesc(){
        WebElement element;
        if(isXpath()) element = Android.driver.findElementByXPath(locator);
        else element = Android.driver.findElementByAndroidUIAutomator(locator);
        return element.getAttribute("contentDesc");
    }

    public UiObject clearText() {
        if(isXpath()) Android.driver.findElementByXPath(locator).clear();
        else Android.driver.findElementByAndroidUIAutomator(locator).clear();
        return this;
    }

    public UiObject inputText(String input) {
        if(isXpath()) Android.driver.findElementByXPath(locator).sendKeys(input);
        else Android.driver.findElementByAndroidUIAutomator(locator).sendKeys(input);
        return this;
    }

    public UiObject tap() {
        if(isXpath()) Android.driver.findElementByXPath(locator).click();
        else Android.driver.findElementByAndroidUIAutomator(locator).click();
        return this;
    }

    public UiObject scrollTo(){
        if(!locator.contains("text")) throw new RuntimeException("Scroll to method can only be used with text attributes and current locator: "+locator+" does not contain any text attributes!");
        if(isXpath()) Android.driver.scrollTo(locator.substring(locator.indexOf("@text=\""), locator.indexOf("\"]")).replace("@text=\"", ""));
        else{
            String text;
            if(locator.contains("textContains")) text = locator.substring(locator.indexOf(".textContains(\""), locator.indexOf("\")")).replace(".textContains(\"", "");
            else text = locator.substring(locator.indexOf(".text(\""), locator.indexOf("\")")).replace(".text(\"", "");
            Android.driver.scrollTo(text);
        }
        return this;
    }

    public UiObject waitToAppear(int seconds) {
        Timer timer = new Timer();
        timer.start();

        while (!timer.isExpiredIn(seconds)) if(isExists()) break;
        if(timer.isExpiredIn(seconds) && !isExists()) throw new AssertionError("Element \""+locator+"\" failed to appear within \""+seconds+"\" seconds");
        return this;
    }

    public UiObject waitToDisappear(int seconds) {
        Timer timer = new Timer();
        timer.start();

        while (!timer.isExpiredIn(seconds)) if(!isExists()) break;
        if(timer.isExpiredIn(seconds) && isExists()) throw new AssertionError("Element \""+locator+"\" failed to disappear within \""+seconds+"\" seconds");
        return this;
    }


}
