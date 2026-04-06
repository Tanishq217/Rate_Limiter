package org.example.computer;

import org.example.computer.RateLimiterFactory.Algorithm;


public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("=== Rate Limiter Demo ===\n");

        System.out.println("Demo 1: Fixed Window Counter (5 requests/10 secs)\n");
        RateLimiter fixedLimiter = RateLimiterFactory.createLimiter(
                Algorithm.FIXED_WINDOW, 5, 10000);

        ExternalService service1 = new ExternalService(fixedLimiter);

        for (int i = 1; i <= 7; i++) {
            boolean allowed = service1.callExternalApi("customer_T1", true);
            System.out.println("Request " + i + ": " + (allowed ? "Allowed" : "Denied"));
            Thread.sleep(1000); // 1 second between requests
        }

        System.out.println("\n--- Switching Algorithm ---");

        System.out.println("\nDemo 2: Sliding Window Counter (5 requests/10 secs)\n");
        RateLimiter slidingLimiter = RateLimiterFactory.createLimiter(
                Algorithm.SLIDING_WINDOW, 5, 10000);

        ExternalService service2 = new ExternalService(slidingLimiter);

        for (int i = 1; i <= 7; i++) {
            boolean allowed = service2.callExternalApi("customer_T1", true);
            System.out.println("Request " + i + ": " + (allowed ? "Allowed" : "Denied"));
            Thread.sleep(1000);
        }
    }
}