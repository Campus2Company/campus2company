package com.campus2company.messaging;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.campus2company.messaging", "com.campus2company.common"})
public class MessagingServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(MessagingServiceApplication.class, args);
    }
}
