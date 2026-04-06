package org.example.computer;



public interface RateLimiter {

    boolean allowRequest(String key);
}