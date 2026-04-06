package org.example.computer;



import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class FixedWindowCounter implements RateLimiter {
     private final int maxRequests;
     private final long windowSizeInMillis;
    private final ConcurrentHashMap<String, Window> windows = new ConcurrentHashMap<>();

    public FixedWindowCounter(int maxRequests, long windowSizeInMillis) {
        this.maxRequests = maxRequests;
         this.windowSizeInMillis = windowSizeInMillis;
    }

    @Override

    public boolean allowRequest(String key) {

        long currentTime = System.currentTimeMillis();

         Window window = windows.computeIfAbsent(key, k -> new Window(currentTime));



        synchronized (window) {
             long currentWindowStart = (currentTime / windowSizeInMillis) * windowSizeInMillis;


             // If we're in a new window, reset the counter
             if (currentWindowStart != window.windowStart) {

                 window.windowStart = currentWindowStart;
                window.count.set(0);
            }


            // Check if within limit

             if (window.count.get() < maxRequests) {

                 window.count.incrementAndGet();
                return true;
            }

             return false;
        }
    }

    private static class Window {

        long windowStart;

        AtomicInteger count;

         Window(long windowStart) {

             this.windowStart = windowStart;

             this.count = new AtomicInteger(0);

        }
    }
}