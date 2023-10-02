package com.example.demo.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class DemoService {

    // Static variable to keep track of book IDs
    private static int bookId;

    // Random number generator for simulating random delays
    private final Random random;

    // Metrics counters and gauges
    private Counter bookCounter;
    private AtomicInteger activeUsers;
    private Counter errorCounter;
    private AtomicInteger responseTimeGauge;

    // CompositeMeterRegistry for managing metrics
    private final CompositeMeterRegistry meterRegistry;

    // RestTemplate for making HTTP requests
    @Autowired
    private RestTemplate restTemplate;

    // Constructor with CompositeMeterRegistry injection
    @Autowired
    public DemoService(CompositeMeterRegistry meterRegistry) {
        this.random = new Random();
        this.meterRegistry = meterRegistry;

        // Initialize counters and gauges with meaningful names
        this.bookCounter = meterRegistry.counter("order.books");
        this.errorCounter = meterRegistry.counter("total.errors");
        this.activeUsers = meterRegistry.gauge("number.of.active.users", new AtomicInteger(0));
        this.responseTimeGauge = meterRegistry.gauge("order.books.response.time", new AtomicInteger(0));
    }

    // Simulate a random sleep time
    private int sleepRandomTime() {
        int randomTimeMillis = random.nextInt(5000);
        try {
            Thread.sleep(randomTimeMillis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return randomTimeMillis;
    }

    // Get the count of active users (simulated)
    public AtomicInteger getActiveUsers() {
        Random random = new Random();
        int max = 100;
        int min = -25;
        activeUsers.set(random.nextInt(max - min + 1) + min);
        if (activeUsers.get() < 0) {
            errorCounter.increment();
        }
        return activeUsers;
    }



    // Simulate ordering a book
    public ResponseEntity<String> orderBook() throws InterruptedException {
        int responseTime = sleepRandomTime();
        responseTimeGauge.set(responseTime);
        bookCounter.increment();
        String response = restTemplate.getForObject("http://localhost:8090/payment", String.class);
        return ResponseEntity.ok("Redirecting to payment -> " + response);
    }

    // Simulate a payment response
    public ResponseEntity<String> payment() {
        return ResponseEntity.ok("Payment done. Order successful. ");
    }

    /*
    The idea here is to run the same application two times with different ports (8080 and 8090),
    such that one application calls the other applications on a different path.
    This is how it acts a simulation of distributed system.
    Order as one microservice and Payment as other microservice
     */
}
