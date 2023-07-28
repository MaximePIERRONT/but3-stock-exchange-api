Feature: Stock Trading
  As a user,
  I want to buy and sell stocks,
  So that I can manage my investments.

  Scenario: Buy Stocks
    Given I am a registered user logged in to my account
    And I have a balance of $5000
    And The stock "ABC" should exist at $20 per stock
    And I have 0 shares of "ABC" stock
    And the stock reserve "ABC" should be 100
    When I buy 50 shares of "ABC" stock
    Then my account balance should be $4000
    And I should have 50 shares of "ABC" stock

  Scenario: Sell Stocks
    Given I am a registered user logged in to my account
    And I have a balance of $5000
    And The stock "ABC" should exist at $20 per stock
    And I have 50 shares of "ABC" stock
    When I sell 50 shares of "ABC" stock
    Then my account balance should be $6000
    And I should have 0 shares of "ABC" stock

  Scenario: Insufficient Funds for Buying Stocks
    Given I am a registered user logged in to my account
    And I have a balance of $500
    And The stock "ABC" should exist at $20 per stock
    When I try to buy 50 shares of "ABC" stock
    Then I should see an error message saying "Insufficient funds"

  Scenario: Insufficient Stocks for Selling
    Given I am a registered user logged in to my account
    And The stock "ABC" should exist at $20 per stock
    And I have 0 shares of "ABC" stock
    When I try to sell 50 shares of "ABC" stock
    Then I should see an error message saying "Insufficient stocks"
