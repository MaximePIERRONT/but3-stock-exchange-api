package com.example.demo.acceptance.steps;

import com.example.demo.domain.User;
import com.example.demo.domain.gateways.AuthenticationGateway;
import com.example.demo.domain.repositories.UserRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserAuthenticationSteps {

    public static final User USER = new User("john.doe@unknown.com", "password", "John", "Doe");
    private final UserRepository userRepository;
    private final AuthenticationGateway authenticationGateway;

    public UserAuthenticationSteps(UserRepository userRepository, AuthenticationGateway authenticationGateway) {
        this.userRepository = userRepository;
        this.authenticationGateway = authenticationGateway;
    }

    @Given("I have registered an account with valid credentials")
    public void iHaveRegisteredAnAccountWithValidCredentials() {
        userRepository.register(USER);
        assertTrue(userRepository.all().contains(USER));
    }

    @When("I log in with these credentials")
    public void iLogInWithTheseCredentials() {
        Optional<User> user = userRepository.all().stream().filter(user1 -> user1 == USER).findFirst();
        user.ifPresent(u -> authenticationGateway.authenticate(u, USER.getPassword()));
    }

    @When("I log in with incorrect credentials {string}:{string}")
    public void iLogInWithIncorrectCredentials(String mail, String password) {
        Optional<User> user = userRepository.all().stream().filter(user1 -> user1.getMail().equals(mail)).findFirst();
        user.ifPresent(u -> authenticationGateway.authenticate(u, password));
    }

    @Then("I should be logged in to my account")
    public void iShouldBeLoggedInToMyAccount() {
        assertTrue(authenticationGateway.currentUser().isPresent());
    }

    @Then("I should be informed that the login was unsuccessful")
    public void iShouldBeInformedThatTheLoginWasUnsuccessful() {
        assertTrue(authenticationGateway.currentUser().isEmpty());
    }


    @Given("I am logged in to my account")
    public void iAmLoggedInToMyAccount() {
        Optional<User> user = userRepository.all().stream().filter(user1 -> user1 == USER).findFirst();
        user.ifPresent(u -> authenticationGateway.authenticate(u, USER.getPassword()));
        assertTrue(authenticationGateway.currentUser().isPresent());
    }

    @Given("I am a registered user logged in to my account")
    public void iAmARegisteredUserLoggedInToMyAccount() {
        userRepository.register(USER);
        Optional<User> user = userRepository.all().stream().filter(user1 -> user1 == USER).findFirst();
        user.ifPresent(u -> authenticationGateway.authenticate(u, USER.getPassword()));
        assertTrue(authenticationGateway.currentUser().isPresent());
    }
}
