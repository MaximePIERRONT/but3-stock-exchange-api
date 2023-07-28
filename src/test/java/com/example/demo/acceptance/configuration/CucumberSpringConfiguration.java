package com.example.demo.acceptance.configuration;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberContextConfiguration
@SpringBootTest(classes = {
        RepositoriesConfiguration.class,
        GatewaysConfiguration.class
})
public class CucumberSpringConfiguration {

}