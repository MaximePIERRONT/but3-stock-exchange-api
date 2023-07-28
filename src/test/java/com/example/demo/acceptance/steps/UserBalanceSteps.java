package com.example.demo.acceptance.steps;

import com.example.demo.domain.gateways.AuthenticationGateway;
import com.example.demo.domain.repositories.UserRepository;
import com.example.demo.usecases.BalanceUseCase;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import static org.junit.jupiter.api.Assertions.*;

public class UserBalanceSteps {
    private final AuthenticationGateway authenticationGateway;

    private final BalanceUseCase balanceUseCase = new BalanceUseCase();
    private long balance;
    private Exception exception;

    public UserBalanceSteps(AuthenticationGateway authenticationGateway) {
        this.authenticationGateway = authenticationGateway;
    }

    @When("I add ${long} to my account")
    public void iAdd$ToMyAccount(long amount) {
        authenticationGateway.currentUser().ifPresent(user -> {
            this.balance = user.getBalance();
            balanceUseCase.add(user, amount);
        });
    }

    @Then("my account balance should increase by ${long}")
    public void myAccountBalanceShouldIncreaseBy$(long amount) {
        authenticationGateway.currentUser().ifPresent(user -> {
            assertEquals(this.balance + amount, user.getBalance());
        });
    }

    @And("I have a balance of ${long}")
    public void iHaveABalanceOf$(long amount) {
        authenticationGateway.currentUser().ifPresent(user -> {
            user.setBalance(amount);
            assertEquals(user.getBalance(), amount);
        });
    }

    @When("I withdraw ${long} from my account")
    public void iWithdraw$FromMyAccount(long amount) {
        authenticationGateway.currentUser().ifPresent(user -> {
            this.balance = user.getBalance();
            balanceUseCase.withdraw(user, amount);
        });
    }

    @Then("my account balance should decrease by ${long}")
    public void myAccountBalanceShouldDecreaseBy$(long amount) {
        authenticationGateway.currentUser().ifPresent(user -> {
            assertEquals(this.balance - user.getBalance(), amount);
        });
    }

    @When("I try to withdraw ${long} from my account")
    public void iTryToWithdraw$FromMyAccount(long amount) {
        authenticationGateway.currentUser().ifPresent(user -> {
            this.balance = user.getBalance();
            Exception exception = assertThrows(IllegalArgumentException.class, () -> {
                balanceUseCase.withdraw(user, amount);
            });
            ErrorSteps.setException(exception);
        });
    }

    @When("I check my account balance")
    public void iCheckMyAccountBalance() {
        authenticationGateway.currentUser().ifPresent(user -> {
            this.balance = user.getBalance();
        });
    }

    @Then("I should see the current balance displayed")
    public void iShouldSeeTheCurrentBalanceDisplayed() {
        authenticationGateway.currentUser().ifPresent(user -> {
            assertEquals(this.balance, user.getBalance());
        });
    }

    @Then("my account balance should be ${int}")
    public void myAccountBalanceShouldBe$(int expectedBalance) {
        authenticationGateway.currentUser().ifPresent(user -> {
            assertEquals(expectedBalance, user.getBalance());
        });
    }
}
