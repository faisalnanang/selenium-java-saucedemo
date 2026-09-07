package tests;

import data.Users;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.InventoryPage;
import pages.LoginPage;
import pages.OrderConfirmationPage;
import support.BaseTest;

public class UiValidationTest extends BaseTest {
    @Test(description = "Login form keeps expected wording")
    public void loginFormWordingIsCorrect() {
        LoginPage login = new LoginPage(driver);
        login.open(baseUrl());
        Assert.assertEquals(login.usernamePlaceholder(), "Username");
        Assert.assertEquals(login.passwordPlaceholder(), "Password");
        Assert.assertEquals(login.loginButtonText(), "Login");
    }

    @Test(description = "Inventory and cart keep expected wording")
    public void inventoryAndCartWordingIsCorrect() {
        new LoginPage(driver).open(baseUrl());
        new LoginPage(driver).login(Users.STANDARD.username(), Users.STANDARD.password());
        InventoryPage inventory = new InventoryPage(driver);
        inventory.expectLoaded();
        Assert.assertEquals(inventory.titleText(), "Products");
        inventory.addProduct("sauce-labs-backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.expectLoaded();
        Assert.assertEquals(cart.titleText(), "Your Cart");
        cart.expectProduct("Sauce Labs Backpack");
        Assert.assertEquals(cart.checkoutButtonText(), "Checkout");
        cart.checkout();
    }

    @Test(description = "Checkout total equals subtotal plus tax")
    public void checkoutTotalCalculationIsCorrect() {
        new LoginPage(driver).open(baseUrl());
        new LoginPage(driver).login(Users.STANDARD.username(), Users.STANDARD.password());
        InventoryPage inventory = new InventoryPage(driver);
        inventory.expectLoaded();
        inventory.addProduct("sauce-labs-backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.checkout();
        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.expectInformationLoaded();
        checkout.fillInformation("Test", "Customer", "12345");
        checkout.continueToOverview();
        checkout.expectOverviewLoaded();
        checkout.expectProduct("Sauce Labs Backpack");
        checkout.expectTotalCalculation();
        checkout.finish();
        new OrderConfirmationPage(driver).expectCompleted();
    }

    @Test(description = "Inventory sorting and cart removal work")
    public void inventorySortingAndCartRemovalWork() {
        new LoginPage(driver).open(baseUrl());
        new LoginPage(driver).login(Users.STANDARD.username(), Users.STANDARD.password());
        InventoryPage inventory = new InventoryPage(driver);
        inventory.expectLoaded();
        inventory.sortBy("za");
        Assert.assertEquals(inventory.firstProductName(), "Test.allTheThings() T-Shirt (Red)");
        inventory.addProduct("sauce-labs-backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.expectLoaded();
        cart.removeProduct("sauce-labs-backpack");
        cart.expectEmpty();
    }

    @Test(description = "Checkout requires mandatory information")
    public void checkoutRequiresInformation() {
        new LoginPage(driver).open(baseUrl());
        new LoginPage(driver).login(Users.STANDARD.username(), Users.STANDARD.password());
        InventoryPage inventory = new InventoryPage(driver);
        inventory.expectLoaded();
        inventory.addProduct("sauce-labs-backpack");
        inventory.openCart();
        CartPage cart = new CartPage(driver);
        cart.expectLoaded();
        cart.checkout();
        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.expectInformationLoaded();
        checkout.continueToOverview();
        checkout.expectInformationError("First Name is required");
    }
}
