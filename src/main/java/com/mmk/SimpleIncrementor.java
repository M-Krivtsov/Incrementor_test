package com.mmk;

public class SimpleIncrementor implements Incrementor {
    private int mCurrentValue = 0;
    private int mMaximumValue = Integer.MAX_VALUE;

    @Override
    public int getNumber() {
        return mCurrentValue;
    }

    @Override
    public void incrementNumber() {
        ++mCurrentValue;

        checkForOverflow();
    }
    
    @Override
    public void setMaximumValue(int maximumValue) {
        if (maximumValue <= 0) { // check input value bounds
            throw new IllegalArgumentException("Maximum value must be greater than zero");
        }

        mMaximumValue = maximumValue;

        checkForOverflow();
    }

    /**
     * Check current value against maximum value and reset if
     * current value reaches or exceeds maximum.
     */
    private void checkForOverflow() {
        if (mCurrentValue >= mMaximumValue) {
            mCurrentValue = 0;
        }
    }

}
