package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class InventoryPage {
    private final WebDriver driver;
    private final By title = By.cssSelector("[data-test='title']");
    private final By cartLink = By.cssSelector("[data-test='shopping-cart-link']");
    private final By cartBadge = By.cssSelector("[data-test='shopping-cart-badge']");
    private final By sort = By.cssSelector("[data-test='product-sort-container']");
    private final WebDriverWait wait;

    public InventoryPage(WebDriver driver) { this.driver = driver; this.wait = new WebDriverWait(driver, Duration.ofSeconds(10)); }
    public void expectLoaded() { wait.until(ExpectedConditions.urlContains("inventory.html")); Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText(), "Products"); }
    public void addProduct(String slug) {
        By addButton = By.cssSelector("[data-test='add-to-cart-" + slug + "']");
        wait.until(ExpectedConditions.elementToBeClickable(addButton)).click();
        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("[data-test='remove-" + slug + "']"), "Remove"));
    }
    public void openCart() {
        var cart = wait.until(ExpectedConditions.elementToBeClickable(cartLink));
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", cart);
        wait.until(ExpectedConditions.urlContains("cart.html"));
    }
    public void expectCartCount(int count) { Assert.assertEquals(wait.until(ExpectedConditions.visibilityOfElementLocated(cartBadge)).getText(), String.valueOf(count)); }
    public void sortBy(String value) { new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(sort))).selectByValue(value); }
    public String firstProductName() { return wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='inventory-item-name']"))).getText(); }
    public String titleText() { return wait.until(ExpectedConditions.visibilityOfElementLocated(title)).getText(); }
}
