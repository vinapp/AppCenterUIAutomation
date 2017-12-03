package com.ibm.appcenter.tests;

import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.AssertJUnit;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.ibm.appcenter.tests.utils.ServerUtil;

import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;

public class AppCenterClientLogin {

		public WebDriver driver;
	    public ServerUtil serverutil;
	    public WebDriverWait wait;

	    @BeforeClass
		void setup() throws MalformedURLException {
	        serverutil = ServerUtil.getInstance();
	        System.out.println("AppCenterClientLogin.setup(getAppCenterClientPath) "+ serverutil.getAppCenterClientPath());
	    	DesiredCapabilities capabilities = new DesiredCapabilities();
			//capabilities.setCapability("appium-version", "1.0");
			capabilities.setCapability("automationName", "XCUITest");
			capabilities.setCapability("platformName", "iOS");
			capabilities.setCapability("platformVersion", "11.1");
			//capabilities.setCapability("deviceName", "iPhone 6");
			capabilities.setCapability("deviceName", "iPhone Simulator");
			//*********Change this to point to your path*********//
			capabilities.setCapability("app", serverutil.getAppCenterClientPath());
			//un-install and install the app before the test
			capabilities.setCapability("full-reset", true);
            capabilities.setCapability("no-reset", false);
			driver = new IOSDriver<WebElement>(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
            wait = new WebDriverWait(driver, 15);
		}

		@AfterClass
		public void teardown() {
			driver.quit();
		}

	    @Test
		public void loginMobileClientAppCenter() {

	    	//*********Enter the login details and login*********//
			WebElement userField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value='User name']"));
			userField.click();
			userField.sendKeys(serverutil.getUsername());

			WebElement passwordField = driver.findElement(By.xpath("//XCUIElementTypeSecureTextField[@value='Password']"));
			passwordField.click();
			passwordField.sendKeys(serverutil.getPassword());

			WebElement hostField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value='Host name or IP']"));
			hostField.click();
			hostField.sendKeys(serverutil.getHost());

			WebElement portField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value='Server port']"));
			portField.click();
			portField.sendKeys(""+ serverutil.getPort() + "");

			WebElement contextField = driver.findElement(By.xpath("//XCUIElementTypeTextField[@value='Application context']"));
			contextField.click();
			contextField.sendKeys(serverutil.getMobContext());

			driver.findElement(By.xpath("//XCUIElementTypeButton[@name='Log in']")).click();
			WebElement alertElement =
				    new WebDriverWait(driver, 60).until(
				    ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeAlert[@visible='true']")));

			//*********If notification is enabled you will get the popup*********//
			if (alertElement.isDisplayed()) {
				driver.switchTo().alert().accept();
			}

			//*********Catalog: Displaying the apps*********//
			WebElement catalogElement = wait.until(
				    ExpectedConditions.presenceOfElementLocated(By.xpath("//XCUIElementTypeStaticText[@name='Catalog']")));
			if (catalogElement.isDisplayed()) {
				AssertJUnit.assertEquals("Catalog", catalogElement.getText());
			} else {
				AssertJUnit.assertEquals(1, 0);//fail the case
			}
		}
}
