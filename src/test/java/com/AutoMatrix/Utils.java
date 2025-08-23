package com.AutoMatrix;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class Utils {

    private static WebDriverWait wait;

    public static void loginToAccount(WebDriver driver)
    {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

//        // Click on the 'Sign In' link
//        WebElement signInLink = wait.until(ExpectedConditions.elementToBeClickable(By.id("Sign In")));
//        Assert.assertTrue(signInLink.isDisplayed(), "'Sign In' link is not visible!");
//        signInLink.click();

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
    }
}
