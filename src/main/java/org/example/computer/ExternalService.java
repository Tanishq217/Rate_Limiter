package org.example.computer;

public class ExternalService {
    private final RateLimiter rateLimiter;



    public ExternalService(RateLimiter rateLimiter) {

        this.rateLimiter = rateLimiter;
    }

    public boolean callExternalApi(String customerId, boolean needsExternalCall) {

        if (!needsExternalCall) {

              System.out.println("No external call needed for " + customerId);

            return true;
        }

        if   (rateLimiter.allowRequest(customerId)) {

               System.out.println("External API called for " + customerId);

              return true; // Simulate successful external call
        }  else {

             System.out.println("Rate limit exceeded for " + customerId + " - external call denied");
            return false;
        }
    }
}