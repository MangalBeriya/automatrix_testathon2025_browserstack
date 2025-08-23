package com.AutoMatrix;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class ECommerceTests extends WebDriverConfig {

    private WebDriverWait wait;

    @Test
    public void loginWithValidCredentials() throws Exception {

        // Navigate to the Testathon demo site
        driver.get("https://testathon.live/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Assert page title is correct
        Assert.assertTrue(driver.getTitle().contains("StackDemo"), "Page title mismatch!");

        // Click on the 'Sign In' link
        WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("Sign In")));
        Assert.assertTrue(signInLink.isDisplayed(), "'Sign In' link is not visible!");
        signInLink.click();

        // Select Username
        WebElement usernameDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Select Username')]")));
        usernameDropdown.click();

        WebElement usernameOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("react-select-2-option-0-3")));
        Assert.assertTrue(usernameOption.isDisplayed(), "Username option not visible!");
        usernameOption.click();

        // Select Password
        WebElement passwordDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Select Password')]")));
        passwordDropdown.click();

        WebElement passwordOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.id("react-select-3-option-0-0")));
        Assert.assertTrue(passwordOption.isDisplayed(), "Password option not visible!");
        passwordOption.click();

        // Click 'Login' button
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn")));
        Assert.assertTrue(loginButton.isEnabled(), "Login button is disabled!");
        loginButton.click();

        // Validate post-login username
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//span[@class='username']")));
        String usernameAfterLogin = usernameElement.getText().trim();

        Assert.assertEquals(usernameAfterLogin, "fav_user", "Logged-in username mismatch!");

        // Validate Logout option is available
        WebElement logoutLink = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        Assert.assertTrue(logoutLink.isDisplayed(), "Logout link not visible!");

        // Close the browser window.
        driver.quit();

    }

    @Test
    public void logoutAndSessionClearance() {

        // Navigate to the Testathon demo site
        driver.get("https://testathon.live/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // --- LOGIN PROCESS ---

        // Click on 'Sign In' link
        wait.until(ExpectedConditions.elementToBeClickable(By.id("Sign In"))).click();

        // Select Username
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Select Username')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("react-select-2-option-0-3"))).click();

        // Select Password
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(text(),'Select Password')]"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.id("react-select-3-option-0-0"))).click();

        // Click on Login
        wait.until(ExpectedConditions.elementToBeClickable(By.id("login-btn"))).click();

        // --- LOGIN VALIDATION ---
        WebElement usernameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("span.username")));
        String actualUsername = usernameElement.getText().trim();
        Assert.assertEquals(actualUsername, "fav_user", "Username after login is incorrect!");

        // --- LOGOUT PROCESS ---
        wait.until(ExpectedConditions.elementToBeClickable(By.id("logout"))).click();
        driver.navigate().refresh();

        // --- LOGOUT VALIDATION ---
        WebElement signInButton = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("Sign In")));
        Assert.assertTrue(signInButton.isDisplayed(), "Sign In button should be visible after logout!");

        // Close the browser
        driver.quit();
    }

    @Test
    public void singleProductCheckoutFlow() throws InterruptedException {

        // Navigate to the Testathon demo site
        driver.get("https://testathon.live/");

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Assert the page title
        Assert.assertTrue(driver.getTitle().contains("StackDemo"), "Page title mismatch!");

        // Create a simple list to store product details (index 0: name, index 1: price)
        List<String> productInfo = new ArrayList<>();

        // Initialize explicit wait
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Wait until the first product name (inside element id '1') is visible
        WebElement productNameElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//*[@id='1']/p")));
        String productName = productNameElement.getText().trim();

        // Save product name
        productInfo.add(productName);
        Assert.assertFalse(productName.isEmpty(), "Product name should not be empty!");

        // Save product price
        String productPrice = driver.findElement(By.xpath("//*[@id='1']//div[3]/div/b")).getText().trim();
        productInfo.add(productPrice);
        Assert.assertTrue(productPrice.matches(".*\\d.*"), "Product price should contain digits!");

        // Click on 'Add to Cart' button
        driver.findElement(By.xpath("//*[@id='1']/div[4]")).click();

        // Wait for the floating cart to be visible
        WebElement cartContent = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".float\\-cart__content")));
        Assert.assertTrue(cartContent.isDisplayed(), "Floating cart should be visible after adding product!");

        // Wait until at least one cart item is visible
        WebElement cartItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='float-cart__shelf-container']//div[@class='shelf-item']")));
        Assert.assertTrue(cartItem.isDisplayed(), "Cart item is not visible!");

        // Verify product name inside cart matches the product on the main page
        String productNameInCart = driver.findElement(
                By.xpath("//*[@id='__next']/div/div/div[2]/div[2]/div[2]/div/div[3]/p[1]")).getText().trim();
        Assert.assertEquals(productNameInCart, productInfo.get(0), "Product name in cart mismatch!");

        // Verify product price in cart matches the product price on main page
        Thread.sleep(2000);
        String actualPrice = driver.findElement(
                        By.xpath("//div[@class='float-cart__shelf-container']//div[@class='shelf-item']/div[4]/p"))
                .getText().replaceAll("[^0-9.]", ""); // keep numeric + decimals
        String expectedPrice = productInfo.get(1).replaceAll("[^0-9.]", "");
        Assert.assertEquals(Double.parseDouble(actualPrice), Double.parseDouble(expectedPrice),
                "Product price in cart mismatch!");

        // Proceed to Checkout
        WebElement checkoutButton = driver.findElement(By.xpath("//div[@class='buy-btn']"));
        Assert.assertTrue(checkoutButton.isDisplayed() && checkoutButton.isEnabled(), "Checkout button should be enabled!");
        checkoutButton.click();

        // Perform login
        Utils.loginToAccount(driver);

        // Assert product name on Checkout page
        WebElement productCheckoutElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".product-title.optimizedCheckout-contentPrimary")));
        String productOnCheckout = productCheckoutElement.getText().trim();
        Assert.assertEquals(productOnCheckout, productInfo.get(0), "Product name on Checkout page mismatch!");

        // Fill Shipping details
        driver.findElement(By.id("firstNameInput")).sendKeys("Mangal");
        driver.findElement(By.id("lastNameInput")).sendKeys("Beriya");
        driver.findElement(By.id("addressLine1Input")).sendKeys("BrowserStack office mumbai.");
        driver.findElement(By.id("provinceInput")).sendKeys("Gujarat");
        driver.findElement(By.id("postCodeInput")).sendKeys("360570");

        // Continue to place order
        WebElement continueBtn = driver.findElement(By.id("checkout-shipping-continue"));
        Assert.assertTrue(continueBtn.isEnabled(), "Continue button should be enabled!");
        continueBtn.click();

        // Verify confirmation message after placing order
        WebElement successMessageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//legend[@id='confirmation-message']")));
        String successMessage = successMessageElement.getText().trim();
        Assert.assertEquals(successMessage, "Your Order has been successfully placed.",
                "Order confirmation message mismatch!");

        // Verify product name on Order Summary
        String productNameOnOrderSummary = driver.findElement(
                By.cssSelector(".product-title.optimizedCheckout-contentPrimary")).getText().trim();
        Assert.assertEquals(productNameOnOrderSummary, productInfo.get(0), "Order Summary product mismatch!");

        // Verify "Continue Shopping" button is displayed and clickable
        WebElement continueShoppingButton = driver.findElement(
                By.xpath("//button[normalize-space()='Continue Shopping »']"));
        Assert.assertTrue(continueShoppingButton.isDisplayed() && continueShoppingButton.isEnabled(),
                "Continue Shopping button should be visible and enabled!");
        continueShoppingButton.click();

        // Final assertion: Verify redirected back to home page
        Assert.assertTrue(driver.getCurrentUrl().contains("testathon.live"), "User not redirected to homepage!");

        // Close browser
        driver.quit();





    }


}
