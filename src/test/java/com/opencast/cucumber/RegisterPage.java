package com.opencast.cucumber;

import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cucumber.api.DataTable;

public class RegisterPage {

	private static final Logger logger = LoggerFactory.getLogger(RegisterPage.class);

	private static final String PICTURE_URL = "http://localhost:3000";

	private static final String JDBC_DRIVER = "org.postgresql.Driver";

	private static final String DB_URL = "jdbc:postgresql://localhost/gallery";

	// Database credentials
	private static final String USER = "admin";

	private static final String PASS = "";

	private final HashMap<String, String> pageMap = new HashMap<String, String>();

	private WebDriver driver;

	public RegisterPage(WebDriver driver) throws ClassNotFoundException, SQLException {
		this.driver = driver;
		cleanUpUsers();
		populatePageMap(pageMap);
	}

	public String getPageURL(String page) {
		return PICTURE_URL + pageMap.get(page);
	}

	public void typeFieldValues(DataTable fieldTable) {
		for (FieldValue fieldValue : fieldTable.asList(FieldValue.class)) {
			String fieldName = fieldValue.getFieldName();
			String value = fieldValue.getValue();
			logger.debug("Typing " + value + " in field " + fieldName);
			WebElement field = driver.findElement(By.id(fieldValue.getFieldName()));
			field.sendKeys(fieldValue.getValue());
		}
	}

	public void clickOn(String id) {
		WebElement element = driver.findElement(By.id(id));
		element.click();
	}

	public boolean isCurrentPage(String pageName) {
		String currentURL = driver.getCurrentUrl();
		String expectedURL = getPageURL(pageName);
		return currentURL.equalsIgnoreCase(expectedURL);
	}

	public boolean menuItemExists(String menuItemText) {
		logger.debug("Check menu item with text = '" + menuItemText + "' exists");
		driver.findElement(By.linkText(menuItemText));
		return true;
	}

	private void cleanUpUsers() throws ClassNotFoundException, SQLException {
		// get database connection
		Connection conn = getConnection();
		PreparedStatement statement = conn.prepareStatement("DELETE FROM users;");
		statement.execute();
	}

	private Connection getConnection()  {
		Connection connection = null;
		try {
			Class.forName(JDBC_DRIVER);
			connection = DriverManager.getConnection(DB_URL, USER, PASS);
		} catch (Exception e) {
			logger.error("Problem opening connection to database", e);
		}
		return connection;
	}

	private void populatePageMap(HashMap<String, String> pageMap) {
		logger.debug("populatePageMap with pages.");
		pageMap.put("home", "/");
		pageMap.put("register", "/register");
	}

	public void typeUsername(String username) {
		WebElement id = driver.findElement(By.id("id"));
		id.sendKeys(username);
	}

	public void typePasswordAndRetypePassword(String password, String retypePassword) {
		WebElement pass = driver.findElement(By.id("pass"));
		pass.sendKeys(password);
		WebElement pass1 = driver.findElement(By.id("pass1"));
		pass1.sendKeys(retypePassword);
	}

	public boolean accountCreated(String username) {
		boolean accountCreated = false;
		Connection connection = getConnection();
		ResultSet rs;
		try {
			rs = connection.createStatement().executeQuery(
					"select count(*) from users where id = '" + username + "';");
			rs.next();
			accountCreated = (0 < rs.getInt(1));
		} catch (SQLException e) {
			logger.error("Problem with accountCreated", e);
		}
		finally {
			if (connection != null)
				try {
					connection.close();
				} catch (SQLException e) {
					logger.error("Problem closing connection.", e);
				}
		}
		return accountCreated;
	}

	public boolean setUpUser(String username) {
		boolean isUserCreated = false;
		Connection connection = getConnection();
		try {
			PreparedStatement statement = connection.prepareStatement("insert into users (id, pass) values (?, ?)");
			statement.setString(1, username);
			statement.setString(2, "dummy");
			statement.execute();
			isUserCreated = true;
		} catch (Exception e) {
			logger.error("Problem with setUpUser", e);
		}
		finally {
			try {
				connection.close();
			} catch (SQLException e) {
				logger.error("Problem closing connection", e);
			}
		}
		return isUserCreated;
	}

	public boolean isErrorMessageDisplayed(String message) {
		List<WebElement> errorMessages = driver.findElements(By.cssSelector(".error"));
		if (null != errorMessages && !errorMessages.isEmpty()) {
			for (WebElement errorMessage : errorMessages) {
				if (errorMessage.getText().trim().equals(message))
					return true;
			}
		}
		return false;
	}

}
