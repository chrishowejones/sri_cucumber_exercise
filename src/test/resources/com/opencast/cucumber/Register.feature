Feature: Register user
	As a user
	I want to register for the picture gallery
	So that I can upload my favourite pictures
	
Scenario: I don't have an account registered but want to create one.
	Given I can see the "register" page
	When I type:
	| Field Name  |  Value    |
	| id	      | Fred      |
	| pass        | password  |
	| pass1       | password  |
	And click on "create"
	Then I see the "home" page
	And I see the menu item "logout Fred"