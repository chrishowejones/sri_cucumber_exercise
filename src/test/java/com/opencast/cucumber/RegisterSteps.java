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

	private final HashMap<String, String> pageMap = new HashMap<String, String>();

	private static final String PICTURE_URL = "http://localhost:3000";

	private static final String JDBC_DRIVER = "org.postgresql.Driver";

	private static final String DB_URL = "jdbc:postgresql://localhost/gallery";

	// Database credentials
	private static final String USER = "admin";

	private static final String PASS = "";

	public class FieldValue {

		private String fieldName;

		private String value;

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String getFieldName() {
			return fieldName;
		}

		public void setFieldName(String fieldName) {
			this.fieldName = fieldName;
		}

	}

	@Before
	public void setUpCucumberTests() throws ClassNotFoundException, SQLException {
		// clean up database
		cleanUpUsers();
		// populate page map
		populatePageMap(pageMap);
	}

	@After
	public void tearDownCucumberTests() {
		//driver.close();
	}

	private void cleanUpUsers() throws ClassNotFoundException, SQLException {
		// get database connection
		Connection conn = getConnection();
		PreparedStatement statement = conn
				.prepareStatement("DELETE FROM users;");
		statement.execute();
	}

	private Connection getConnection() throws ClassNotFoundException,
			SQLException {
		Class.forName(JDBC_DRIVER);
		return DriverManager.getConnection(DB_URL, USER, PASS);
	}

	private void populatePageMap(HashMap<String, String> pageMap) {
		logger.debug("populatePageMap with pages.");
		pageMap.put("home", "/");
		pageMap.put("register", "/register");
	}

	@Given("^I can see the \"(.*?)\" page$")
	public void i_can_see_the_page(String page) throws Throwable {
		logger.trace("Called i_can_see the_page for " + page);
		// Look up URL for page from Map
		String pageURL = getPageURL(page);
		logger.debug("page url =" + pageURL);
		driver.get(pageURL);
	}

	private String getPageURL(String page) {
		return PICTURE_URL + pageMap.get(page);
	}

	@When("^I type:$")
	public void i_type(DataTable fieldTable) throws Throwable {
		// Write code here that turns the phrase above into concrete actions
		// For automatic transformation, change DataTable to one of
		// List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
		// E,K,V must be a scalar (String, Integer, Date, enum etc)
		typeFieldValues(fieldTable);
	}

	private void typeFieldValues(DataTable fieldTable) {
		for (FieldValue fieldValue : fieldTable.asList(FieldValue.class)) {
			String fieldName = fieldValue.getFieldName();
			String value = fieldValue.getValue();
			logger.debug("Typing " + value + " in field " + fieldName);
			WebElement field = driver.findElement(By.id(fieldValue
					.getFieldName()));
			field.sendKeys(fieldValue.getValue());
		}

	}

	@When("^click on \"(.*?)\"$")
	public void click_on(String id) throws Throwable {
		clickOn(id);
	}

	private void clickOn(String id) {
		WebElement element = driver.findElement(By.id(id));
		element.click();
	}

	@Then("^I see the \"(.*?)\" page$")
	public void i_see_the_page(String pageName) throws Throwable {
		checkCurrentPageExists(pageName);
	}

	private void checkCurrentPageExists(String pageName) {
		String currentURL = driver.getCurrentUrl();
		String expectedURL = getPageURL(pageName);
		assertThat(currentURL, equalToIgnoringCase(expectedURL));
	}

	@Then("^I see the menu item \"(.*?)\"$")
	public void i_see_the_menu_item(String menuItemText) throws Throwable {
		checkMenuItemExists(menuItemText);
	}

	private void checkMenuItemExists(String menuItemText) {
		logger.debug("Check menu item with text = '" + menuItemText + "' exists");
		WebElement menuItem = driver.findElement(By.linkText(menuItemText));
		assertThat(menuItem, notNullValue());
		assertThat(menuItem.getText(), is(menuItemText));
	}
}
