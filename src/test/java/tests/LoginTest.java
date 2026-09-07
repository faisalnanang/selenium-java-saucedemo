package tests;

import data.Users;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.InventoryPage;
import pages.LoginPage;
import support.BaseTest;

public class LoginTest extends BaseTest {
    private LoginPage loginPage;

    @BeforeMethod(alwaysRun = true, dependsOnMethods = "setUp")
    public void openLogin() {
        loginPage = new LoginPage(driver);
        loginPage.open(baseUrl());
    }

    @Test(description = "Standard user can log in to inventory")
    public void standardUserCanLogin() {
        loginPage.login(Users.STANDARD.username(), Users.STANDARD.password());
        new InventoryPage(driver).expectLoaded();
    }

    @Test(description = "Locked-out user cannot log in")
    public void lockedOutUserCannotLogin() {
        loginPage.login(Users.LOCKED_OUT.username(), Users.LOCKED_OUT.password());
        loginPage.expectError("Epic sadface: Sorry, this user has been locked out.");
    }
}
