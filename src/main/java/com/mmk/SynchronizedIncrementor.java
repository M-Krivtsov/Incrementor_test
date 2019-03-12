package com.mmk;

/**
 * Implementation of thread-aware incrementor using synchronization blocks.
 * This implementation uses synchronized methods to make all changes effectively synchronous.
 */
public class SynchronizedIncrementor implements Incrementor {
    // Current value does not have to be volatile as it is changed only in synchronized code.
    private int mCurrentValue = 0;
    private int mMaximumValue = Integer.MAX_VALUE;

    @Override
    public int getNumber() {
        return mCurrentValue;
    }

    @Override
    public synchronized void incrementNumber() {
        ++mCurrentValue;

        checkForOverflow();
    }
    
    @Override
    public synchronized void setMaximumValue(int maximumValue) {
        if (maximumValue <= 0) { // check input value bounds
            throw new IllegalArgumentException("Maximum value must be greater than zero");
        }

        mMaximumValue = maximumValue;

        checkForOverflow();
    }

    /**
     * Check current value against maximum value and reset if current value reaches or exceeds maximum.
     * Not marked 'synchronized' because it is called only from synchronized methods.
     */
    private void checkForOverflow() {
        if (mCurrentValue >= mMaximumValue) {
            mCurrentValue = 0;
        }
    }

}
