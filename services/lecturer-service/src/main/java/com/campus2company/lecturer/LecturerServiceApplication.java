package com.campus2company.lecturer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "com.campus2company.lecturer",
        "com.campus2company.common"
})
public class LecturerServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(LecturerServiceApplication.class, args);
    }
}
