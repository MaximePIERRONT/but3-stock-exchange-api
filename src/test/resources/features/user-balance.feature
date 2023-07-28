Feature: User Balance Management
  As a user,
  I want to manage my balance,
  So that I can buy and sell stocks effectively.

  Scenario: Add Funds
    Given I am a registered user logged in to my account
    When I add $5000 to my account
    Then my account balance should increase by $5000

  Scenario: Withdraw Funds
    Given I am a registered user logged in to my account
    And I have a balance of $5000
    When I withdraw $1000 from my account
    Then my account balance should decrease by $1000

  Scenario: Insufficient Funds for Withdrawal
    Given I am a registered user logged in to my account
    And I have a balance of $5000
    When I try to withdraw $6000 from my account
    Then I should see an error message saying "Insufficient funds"

  Scenario: Check Balance
    Given I am a registered user logged in to my account
    When I check my account balance
    Then I should see the current balance displayed
