package com.example.demo.acceptance.steps;

import io.cucumber.java.en.Then;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ErrorSteps {

    public static Throwable EXCEPTION;

    @Then("I should see an error message saying {string}")
    public void iShouldSeeAnErrorMessageSaying(String errorMessage) {
        assertEquals(errorMessage, EXCEPTION.getMessage());
    }

    public static void setException(Throwable exception) {
        EXCEPTION = exception;
    }
}
