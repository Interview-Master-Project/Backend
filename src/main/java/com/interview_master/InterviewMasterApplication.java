package com.interview_master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class InterviewMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewMasterApplication.class, args);
    }

}
