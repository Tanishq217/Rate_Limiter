package org.example.computer;



public class RateLimiterFactory {

    public enum Algorithm {

        FIXED_WINDOW,

        SLIDING_WINDOW,

    }

    public static RateLimiter createLimiter(Algorithm algorithm, int maxRequests, long windowSizeInMillis) {

        switch (algorithm) {

            case FIXED_WINDOW:

                return new FixedWindowCounter(maxRequests, windowSizeInMillis);

            case SLIDING_WINDOW:
                return new SlidingWindowCounter(maxRequests, windowSizeInMillis);

            default:
                throw new IllegalArgumentException("Unsupported algorithm: " + algorithm);

        }
    }
}