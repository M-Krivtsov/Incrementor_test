package com.mmk.util;

/**
 * Stopwatch for measuring elapsed time between beginning and end of some process.
 */
public class StopWatch {
    private long mStartTime; // process start time in nanoseconds
    private long mStopTime;  // process end time in nanoseconds

    /**
     * Set time of process start.
     */
    public void start() {
        mStartTime = System.nanoTime();
    }

    /**
     * Set time of process end.
     */
    public void stop() {
        mStopTime = System.nanoTime();
    }

    /**
     * Get elapsed time between call to start and call to end.
     * @return elapsed time in milliseconds
     */
    public long getElapsedTimeMillis() {
        return (mStopTime - mStartTime) / 1_000_000; // get time difference and convert nanos to millis
    }
}
