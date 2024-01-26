package com.example.backendassignment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class })


public class BackendAssignmentApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendAssignmentApplication.class, args);

    }
}