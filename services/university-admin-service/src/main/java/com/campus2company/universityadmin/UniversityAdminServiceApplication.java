package com.campus2company.universityadmin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {
        "com.campus2company.universityadmin",
        "com.campus2company.common"
})
public class UniversityAdminServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UniversityAdminServiceApplication.class, args);
    }
}