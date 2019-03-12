package com.mmk;

import org.junit.Test;

public class AtomicIncrementorTest extends MultithreadIncrementorTest {
    @Override
    Incrementor createNewIncrementorInstance() {
         return new AtomicIncrementor();
    }
    
    // Multi-Thread aware Incrementor value must match count of increments if it is less than maximum (Integer.MAX_VALUE)
    @Test
    public void testMultipleIncrementationMultithread() {
        testMultipleIncrementationMultithread(createNewIncrementorInstance(), Integer.MAX_VALUE);
    }

    // Multi-Thread aware Incrementor value must match modulus of increments count by maximum value as it resets on reaching maximum value
    @Test
    public void testMultipleIncrementationMultithreadWithLimit() {
        int maximumValue = 250;

        Incrementor incrementor = createNewIncrementorInstance();
        incrementor.setMaximumValue(maximumValue);
        testMultipleIncrementationMultithread(incrementor, maximumValue);
    }
}
