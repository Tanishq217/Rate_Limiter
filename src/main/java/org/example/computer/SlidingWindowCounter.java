package org.example.computer;


import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class SlidingWindowCounter implements RateLimiter {

    private final int maxRequests;


    private final long windowSizeInMillis;
    private final int subWindowCount;


    private final ConcurrentHashMap<String, SlidingWindow> windows = new ConcurrentHashMap<>();

    public SlidingWindowCounter(int maxRequests, long windowSizeInMillis) {
        this(maxRequests, windowSizeInMillis, 10);
    }

    public SlidingWindowCounter(int maxRequests, long windowSizeInMillis, int subWindowCount) {

        this.maxRequests = maxRequests;

        this.windowSizeInMillis = windowSizeInMillis;

        this.subWindowCount = subWindowCount;
    }

    @Override
    public boolean allowRequest(String key) {
        long currentTime = System.currentTimeMillis();
        SlidingWindow window = windows.computeIfAbsent(key, k -> new SlidingWindow());


        synchronized (window) {
            long subWindowSize = windowSizeInMillis / subWindowCount;

            long currentSubWindow = currentTime / subWindowSize;

            window.subWindows.keySet().removeIf(sw -> sw < currentSubWindow - subWindowCount);


            AtomicInteger currentCount = window.subWindows.computeIfAbsent(currentSubWindow,
                    sw -> new AtomicInteger(0));


            int totalRequests = 0;

            for (AtomicInteger count : window.subWindows.values()) {
                totalRequests += count.get();
            }



            if (totalRequests < maxRequests) {
                currentCount.incrementAndGet();

                return true;
            }
            return false;
        }
    }

    private static class SlidingWindow {
        ConcurrentHashMap<Long, AtomicInteger> subWindows = new ConcurrentHashMap<>();
    }
}