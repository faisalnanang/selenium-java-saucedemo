package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class CartPage {
    private final WebDriver driver;
    private final WebDriverWait wait;
    public CartPage(WebDriver driver) { this.driver = driver; this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); }
    public void expectLoaded() { wait.until(ExpectedConditions.urlContains("cart.html")); Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='title']"))).getText(), "Your Cart"); }
    public void expectProduct(String name) { Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='inventory-item']"))).getText().contains(name)); }
    public void checkout() { wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='checkout']"))).click(); }
    public void removeProduct(String slug) { wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='remove-" + slug + "']"))).click(); }
    public void expectEmpty() { Assert.assertTrue(wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-list']"))).findElements(By.cssSelector("[data-test='inventory-item']")).isEmpty()); }
    public String titleText() { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='title']"))).getText(); }
    public String checkoutButtonText() { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='checkout']"))).getText(); }
}
