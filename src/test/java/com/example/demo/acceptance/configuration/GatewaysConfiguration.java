package com.example.demo.acceptance.configuration;

import com.example.demo.adapters.InMemoryAuthenticationGateway;
import com.example.demo.domain.gateways.AuthenticationGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class GatewaysConfiguration {

    @Bean
    @Scope("cucumber-glue")
    public AuthenticationGateway authenticationGateway() {
        return new InMemoryAuthenticationGateway();
    }

}
