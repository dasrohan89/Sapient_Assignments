package com.rest.assignment.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * This configuration class has three responsibilities:
 * 
 *     It enables the auto configuration of the Spring application context.</li>
 *     
 *         It ensures that Spring looks for other components (controllers, services, and repositories) from the
 *         com.rest.assignment.product package.
 *     It launches our application in the main() method.
 * 
 * @author Rohan Das
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan
public class ProductAppConfig {

    public static void main(String[] args) {
        SpringApplication.run(ProductAppConfig.class, args);
    }
}
