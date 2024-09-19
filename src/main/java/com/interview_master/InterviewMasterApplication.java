package com.interview_master;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.MultipartAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class, MultipartAutoConfiguration.class})
public class InterviewMasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewMasterApplication.class, args);
    }

}
