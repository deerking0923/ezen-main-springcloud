package com.example.userservice.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

@Component
public class PropertyChecker implements CommandLineRunner {
    @Autowired
    private Environment environment;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("========================================");
        System.out.println("=       Checking token properties     =");
        System.out.println("========================================");
        System.out.println("token.expiration_time: " + environment.getProperty("token.expiration_time"));
        System.out.println("token.secret: " + environment.getProperty("token.secret"));
        System.out.println("========================================");
    }
}
