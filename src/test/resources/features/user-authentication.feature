Feature: User Authentication
  As a user,
  I want to log in to my account,
  So I can access the stock trading system.

  Scenario: Successful Login
    Given I have registered an account with valid credentials
    When I log in with these credentials
    Then I should be logged in to my account

  Scenario: Unsuccessful Login
    Given I have registered an account with valid credentials
    When I log in with incorrect credentials "john.doe@unknown.com":"badpassword"
    Then I should be informed that the login was unsuccessful
