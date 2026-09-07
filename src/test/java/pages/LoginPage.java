package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import support.AllureScreenshot;

public class LoginPage {
    private final WebDriver driver;
    private final By username = By.cssSelector("[data-test='username']");
    private final By password = By.cssSelector("[data-test='password']");
    private final By loginButton = By.cssSelector("[data-test='login-button']");
    private final By error = By.cssSelector("[data-test='error']");

    public LoginPage(WebDriver driver) { this.driver = driver; }
    public void open(String baseUrl) {
        driver.get(baseUrl);
        AllureScreenshot.attach(driver, "Login page loaded");
    }
    public void login(String user, String secret) {
        driver.findElement(username).sendKeys(user);
        driver.findElement(password).sendKeys(secret);
        driver.findElement(loginButton).click();
        AllureScreenshot.attach(driver, "After login");
    }
    public void expectError(String message) { Assert.assertTrue(driver.findElement(error).getText().contains(message)); }
    public String usernamePlaceholder() { return driver.findElement(username).getAttribute("placeholder"); }
    public String passwordPlaceholder() { return driver.findElement(password).getAttribute("placeholder"); }
    public String loginButtonText() { return driver.findElement(loginButton).getAttribute("value"); }
}
