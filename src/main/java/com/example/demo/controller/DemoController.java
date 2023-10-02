package com.example.demo.controller;

import com.example.demo.service.DemoService;
import io.micrometer.core.annotation.Timed;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
@Slf4j
public class DemoController {

    // Inject the DemoService for handling business logic
    @Autowired
    private DemoService demoService;

    // Retrieve the application name from configuration properties
    @Value("${spring.application.name}")
    private String applicationName;

    // Handle HTTP GET request for '/order' endpoint
    @GetMapping("/order")
    public ResponseEntity orderBook() throws InterruptedException {
        // Delegate the request to the DemoService for processing
        return demoService.orderBook();
    }

    // Handle HTTP GET request for '/payment' endpoint
    @GetMapping("/payment")
    public ResponseEntity payment() {
        // Delegate the request to the DemoService for processing
        return demoService.payment();
    }

    // Handle HTTP GET request for '/active-users' endpoint
    @GetMapping("/active-users")
    public AtomicInteger getActiveUsers() {
        // Retrieve the count of active users from the DemoService
        return demoService.getActiveUsers();
    }
}