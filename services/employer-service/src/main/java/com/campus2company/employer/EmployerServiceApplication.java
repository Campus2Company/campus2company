package com.campus2company.employer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.campus2company.employer", "com.campus2company.common"})
public class EmployerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployerServiceApplication.class, args);
    }
}
