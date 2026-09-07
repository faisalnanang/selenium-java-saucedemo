package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;

public class OrderConfirmationPage {
    private final WebDriver driver;
    public OrderConfirmationPage(WebDriver driver) { this.driver = driver; }
    public void expectCompleted() { Assert.assertTrue(driver.getCurrentUrl().contains("checkout-complete.html")); Assert.assertEquals(driver.findElement(By.cssSelector("[data-test='complete-header']")).getText(), "Thank you for your order!"); Assert.assertTrue(driver.findElement(By.cssSelector("[data-test='complete-text']")).getText().contains("Your order has been dispatched")); }
    public void backToProducts() { driver.findElement(By.cssSelector("[data-test='back-to-products']")).click(); }
}
