Feature: Register user
	As a User
	I want to Register 
	So that I can access My Account
	
Scenario: register with valid details
	Given I am on Register Page
	When I enter username as 'username'
	And I enter password as 'password' and retypePassword as 'password'
	And I select create account
	Then account 'username' should be created successfully
	And the 'home' page is displayed
	And I should see menu item 'logout username'
	
	
Scenario Outline: register with in valid details
	Given I am on Register Page
	When I enter username as '<Username>'
	And I enter password as '<Password>' and retypePassword as '<RetypePassword>'
	And I select create account
	Then account '<Username>' shouldn't be created 
	And I should see error message '<Error Message>'
	
Examples:
	|Username|Password|RetypePassword|Error Message|
	|sri     |sri     |sri           |password must be at least 5 characters|
	|sri     |srikanth|srikanth1     |entered passwords do not match|
	
	
Scenario:Attempt user registration with existing username
	Given I am on Register Page
	And user exists with username 'jack'
    When I enter username as 'jack'
    And I enter password as 'password' and retypePassword as 'password'
    And I select create account
    Then I should see error message 'The user with id jack already exists!'
    	
 
	
	
	
	
	
	
	