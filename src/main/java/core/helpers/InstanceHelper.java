package core.helpers;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class InstanceHelper {

    public static AndroidDriver getAndroidDriverInstance(URL host, DesiredCapabilities capabilities) {
            return new AndroidDriver(host, capabilities);
    }
}
