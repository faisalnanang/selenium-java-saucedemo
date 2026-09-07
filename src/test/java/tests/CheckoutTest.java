package tests;

import data.Users;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import pages.OrderConfirmationPage;
import support.BaseTest;

public class CheckoutTest extends BaseTest {
    private static final String PRODUCT_SLUG = "sauce-labs-backpack";
    private static final String PRODUCT_NAME = "Sauce Labs Backpack";

    @Test(description = "Standard user can complete a purchase")
    public void standardUserCanCompletePurchase() {
        new LoginPage(driver).open(baseUrl());
        new LoginPage(driver).login(Users.STANDARD.username(), Users.STANDARD.password());
        InventoryPage inventory = new InventoryPage(driver);
        inventory.expectLoaded();
        inventory.addProduct(PRODUCT_SLUG);
        inventory.expectCartCount(1);
        inventory.openCart();

        CartPage cart = new CartPage(driver);
        cart.expectLoaded();
        cart.expectProduct(PRODUCT_NAME);
        cart.checkout();

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.expectInformationLoaded();
        checkout.fillInformation("Test", "Customer", "12345");
        checkout.continueToOverview();
        checkout.expectOverviewLoaded();
        checkout.expectProduct(PRODUCT_NAME);
        checkout.expectTotalVisible();
        checkout.finish();

        OrderConfirmationPage confirmation = new OrderConfirmationPage(driver);
        confirmation.expectCompleted();
        confirmation.backToProducts();
        inventory.expectLoaded();
    }
}
