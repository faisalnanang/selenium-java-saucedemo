package pages;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CheckoutPage {
    private final WebDriver driver;
    private final By title = By.cssSelector("[data-test='title']");
    private final WebDriverWait wait;

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void expectInformationLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-one.html"));
        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText(),
                "Checkout: Your Information");
    }

    public void fillInformation(String first, String last, String postal) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='firstName']")))
                .sendKeys(first);
        driver.findElement(By.cssSelector("[data-test='lastName']")).sendKeys(last);
        driver.findElement(By.cssSelector("[data-test='postalCode']")).sendKeys(postal);
    }

    public void continueToOverview() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='continue']"))).click();
    }

    public void expectInformationError(String message) {
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='error']")))
                        .getText().contains(message));
    }

    public void expectOverviewLoaded() {
        wait.until(ExpectedConditions.urlContains("checkout-step-two.html"));
        Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText(),
                "Checkout: Overview");
    }

    public void expectProduct(String name) {
        Assert.assertTrue(wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='inventory-item']")))
                .getText().contains(name));
    }

    public void expectTotalCalculation() {
        BigDecimal subtotal = amount("subtotal-label");
        BigDecimal tax = amount("tax-label");
        BigDecimal total = amount("total-label");
        Assert.assertEquals(subtotal.add(tax), total);
    }

    private BigDecimal amount(String testId) {
        String text = wait
                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='" + testId + "']")))
                .getText();
        Matcher match = Pattern.compile("\\$([0-9]+(?:\\.[0-9]{2})?)").matcher(text);
        Assert.assertTrue(match.find());
        return new BigDecimal(match.group(1));
    }

    public void expectTotalVisible() {
        Assert.assertTrue(
                wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='total-label']")))
                        .getText().contains("Total:"));
    }

    public void finish() {
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']"))).click();
    }
}
