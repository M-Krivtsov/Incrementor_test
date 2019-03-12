package com.mmk;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Implementation of thread-aware incrementor using AtomicInteger.
 * This implementation uses atomic CAS operations to update inner value,
 * retrying in case of race conditions. Current value can be retrieved without any
 * additional precautions.
 */
public class AtomicIncrementor implements Incrementor {
    private final AtomicInteger mCurrentValue = new AtomicInteger(0);
    private int mMaximumValue = Integer.MAX_VALUE;

    @Override
    public int getNumber() {
        return mCurrentValue.get();    // get current value from atomic
    }

    @Override
    public void incrementNumber() {
        mCurrentValue.updateAndGet(value -> {
            int newValue = value + 1;                       // calculate incremented value
            return newValue < mMaximumValue ? newValue : 0; // check maximum value constraint
        });
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
