package support;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;

@Listeners(TestFailureListener.class)
public abstract class BaseTest {
    protected WebDriver driver;
    protected VideoRecorder videoRecorder;

    @BeforeMethod(alwaysRun = true)
    public void setUp() {
        driver = DriverFactory.create();
        videoRecorder = new VideoRecorder();
        videoRecorder.start();
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected String baseUrl() {
        return System.getProperty("baseUrl", "https://www.saucedemo.com");
    }
}
