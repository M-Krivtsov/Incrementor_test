package com.mmk;

/**
 * Cyclic incremental counter. Starts from 0 and increments by 1 on each call to {@link #incrementNumber()}.
 * Current value can be retrieved with {@link #getNumber()}. If internal value reached maximum value
 * (Default is {@link java.lang.Integer#MAX_VALUE}) it resets to zero.
 */
public interface Incrementor {
    /**
     * Get current counter value. Starts with 0.
     *
     * @return current counter value.
     */
    int getNumber();

    /**
     * Increment current value. {@link #getNumber()} would return
     * value incremented by one after that.
     */
    void incrementNumber();

    /**
     * Set maximum value for this incrementor.
     * If internal value reaches maximum value, it resets to zero.
     * If new maximum is less than current value, then current value is reset.
     *
     * Default maximum value is {@link java.lang.Integer#MAX_VALUE}
     * 
     * @param maximumValue new maximum value for incrementor, can't be less than or equal to zero
     */
    void setMaximumValue(int maximumValue);
}
