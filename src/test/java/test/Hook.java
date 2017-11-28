package test;

import core.ADB;
import core.managers.DriverManager;
import core.managers.ParallelManager;
import org.testng.annotations.*;

import java.io.IOException;
import java.net.MalformedURLException;

public class Hook {

    @BeforeTest
    public void setupTest() throws IOException {
    }

    @BeforeSuite
    public void setUpSuite() {
    }

    @BeforeClass
    public void setUpClass() throws MalformedURLException {
        ParallelManager.setService(DriverManager.createService());
        System.out.println(Thread.currentThread().getName());
        DriverManager.createDriver();
    }

    @AfterClass
    public void tearDownClass() {

    }

    @BeforeMethod
    public void setUpMethod() {

    }

    @AfterMethod
    public void tearDownMethod() {

    }


}
