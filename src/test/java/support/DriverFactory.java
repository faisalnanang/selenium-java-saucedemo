package support;

import java.time.Duration;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import java.util.logging.Level;

public final class DriverFactory {
    private DriverFactory() { }

    public static WebDriver create() {
        String browser = System.getProperty("browser", "chrome").toLowerCase();
        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
        WebDriver driver;
        if (browser.equals("firefox")) {
            FirefoxOptions options = new FirefoxOptions();
            if (headless) options.addArguments("-headless");
            driver = new FirefoxDriver(options);
        } else if (browser.equals("edge")) {
            EdgeOptions options = new EdgeOptions();
            if (headless) options.addArguments("--headless=new");
                options.addArguments("--window-size=1440,1000", "--disable-notifications", "--disable-save-password-bubble", "--no-first-run", "--no-default-browser-check", "--disable-extensions", "--disable-popup-blocking", "--password-store=basic", "--disable-sync");
                options.addArguments("--disable-features=PasswordManagerOnboarding,PasswordLeakDetection,AutofillServerCommunication");
            options.setExperimentalOption("prefs", java.util.Map.of(
                    "credentials_enable_service", false,
                    "profile.password_manager_enabled", false,
                    "profile.password_manager_leak_detection", false,
                    "autofill.profile_enabled", false
            ));
            driver = new EdgeDriver(options);
        } else {
            ChromeOptions options = new ChromeOptions();
            if (headless) options.addArguments("--headless=new");
                options.addArguments("--window-size=1440,1000", "--disable-notifications", "--disable-save-password-bubble", "--no-first-run", "--no-default-browser-check", "--disable-extensions", "--disable-popup-blocking", "--password-store=basic", "--disable-sync");
                options.addArguments("--disable-features=PasswordManagerOnboarding,PasswordLeakDetection,AutofillServerCommunication");
            options.setExperimentalOption("prefs", java.util.Map.of(
                    "credentials_enable_service", false,
                    "profile.password_manager_enabled", false,
                    "profile.password_manager_leak_detection", false,
                    "autofill.profile_enabled", false
            ));
                    LoggingPreferences logs = new LoggingPreferences();
                    logs.enable(LogType.PERFORMANCE, Level.ALL);
                    options.setCapability("goog:loggingPrefs", logs);
            driver = new ChromeDriver(options);
        }
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(30));
        return driver;
    }
}
