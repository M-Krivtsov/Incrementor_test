package com.mmk;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of thread-aware incrementor using AtomicInteger.
 * This implementation uses atomic increment to update inner value and then uses CAS to
 * check for reaching max value. This approach prevents race conditions when internal value is less than maximum.
 * Current value can be retrieved without any additional precautions.
 */
public class AtomicIncrementorEx implements Incrementor {
    private final AtomicInteger mCurrentValue = new AtomicInteger(0);
    private int mMaximumValue = Integer.MAX_VALUE;

    @Override
    public int getNumber() {
        trim();                        // ensure value is in range
        return mCurrentValue.get();    // get current value from atomic
    }

    @Override
    public void incrementNumber() {
        mCurrentValue.incrementAndGet();
        trim();
    }

    /**
     * Ensure counter is less than maximum value.
     */
    void trim() {
        int value;
        int newValue;
        while (true) {
            value = mCurrentValue.get();
            if (value >= 0 && value < mMaximumValue) { // value is in range, stop iterations
                break;
            }
            // decrement value by maximum value to move into range
            // In case of very many threads calling atomic increment at once this would not move value in range in first iteration
            // but it would be moved into range in consecutive iterations.
            newValue = value - mMaximumValue;
            // try setting updated value, we need to recheck resulting inner value anyway in case we were more than max value out of range
            // so we do not need to check if update was succesfull or not.
            mCurrentValue.compareAndSet(value, newValue);
        }
    }

    @Override
    public void setMaximumValue(int maximumValue) {
        if (maximumValue <= 0) { // check input value bounds
            throw new IllegalArgumentException("Maximum value must be greater than zero");
        }

        mMaximumValue = maximumValue;

        mCurrentValue.updateAndGet(value -> {
            return value < mMaximumValue ? value : 0; // check maximum value constraint
        });
    }
}
