package com.opencast.cucumber;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.DataTable;
import cucumber.api.PendingException;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

public class RegisterSteps {

	Logger logger = LoggerFactory.getLogger(RegisterSteps.class);

	private final WebDriver driver = new FirefoxDriver();

	private RegisterPage registerPage;

	@Before
	public void setUpCucumberTests() throws ClassNotFoundException, SQLException {
		// clean up database
		registerPage = new RegisterPage(driver);
	}

	@After
	public void tearDownCucumberTests() {
		driver.close();
	}

	
	@Given("^I am on Register Page$")
	public void i_am_on_Register_Page() throws Throwable {
	    String pageURL = registerPage.getPageURL("register");
	    driver.get(pageURL);
	}

	@When("^I enter username as '(.*)'$")
	public void i_enter_username_as_username(String username) throws Throwable {
	   registerPage.typeUsername(username);
	}

	@When("^I enter password as '(.*)' and retypePassword as '(.*)'$")
	public void i_enter_password_as_and_retypePassword_as(String password, String retypePassword) throws Throwable {
		registerPage.typePasswordAndRetypePassword(password, retypePassword);
	}

	@When("^I select create account$")
	public void i_select_create_account() throws Throwable {
	    registerPage.clickOn("create");
	}

	@Then("^account '(.*)' should be created successfully$")
	public void account_should_be_created_successfully(String username) throws Throwable {
	    assertThat(registerPage.accountCreated(username), is(true));
	}

	@Then("^I should see menu item '(.*)'$")
	public void i_should_see_menu_item(String menuItem) throws Throwable {
	    assertThat(registerPage.menuItemExists(menuItem), is(true));
	}

	@Then("^the '(.*)' page is displayed$")
	public void the_page_is_displayed(String page) throws Throwable {
	    assertThat(registerPage.isCurrentPage(page), is(true));
	}
	
	@Then("^account '(.*)' shouldn't be created$")
	public void account_shouldnt_be_created(String username) throws Throwable {
	    assertThat(registerPage.accountCreated(username), is(false));
	}

	@Then("^I should see error message '(.*)'$")
	public void i_should_see_message(String message) throws Throwable {
		assertThat(registerPage.isErrorMessageDisplayed(message), is(true));
	}

	@Given("^user exists with username '(.*)'$")
	public void user_exists_with_username(String username) throws Throwable {
	    assertThat(registerPage.setUpUser(username), is(true));
	}
	
	
}
