package com.mmk;

import com.mmk.util.StopWatch;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public abstract class SinglethreadIncrementorTest {
    static final int HIGH_INCREMENT_COUNT = 1 << 24;

    // create instance of Incrementor to test
    abstract Incrementor createNewIncrementorInstance();
    
    // Incrementor must throw exception if maximum value is set to zero
    @Test(expected = IllegalArgumentException.class)
    public void testSettingWrongMaximumZero() {
        Incrementor incrementor = createNewIncrementorInstance();
        incrementor.setMaximumValue(0);
    }

    // Incrementor must throw exception if maximum value is set to negative value
    @Test(expected = IllegalArgumentException.class)
    public void testSettingWrongMaximumNegative() {
        Incrementor incrementor = createNewIncrementorInstance();
        incrementor.setMaximumValue(-1234);
    }

    // Incrementor must start with zero value
    @Test
    public void testInitialValueIsZero() {
        Incrementor incrementor = createNewIncrementorInstance();
        assertThat(incrementor.getNumber(), is(0));
    }

    // Incrementor value must match count of increments if it is less than maximum (Integer.MAX_VALUE)
    @Test
    public void testMultipleIncrementation() {
        int requiredIncrementCount = HIGH_INCREMENT_COUNT;

        Incrementor incrementor = createNewIncrementorInstance();

        StopWatch stopWatch = new StopWatch(); // measure performance of singlethreaded increments
        stopWatch.start();

        for (int i = 0; i < requiredIncrementCount; ++i) {
            incrementor.incrementNumber();
        }

        stopWatch.stop();

        assertThat(incrementor.getNumber(), is(requiredIncrementCount));

        System.out.println(String.format(Locale.getDefault(), "%d increments took %d ms", requiredIncrementCount, stopWatch.getElapsedTimeMillis()));
    }

    // Incrementor value must match modulus of increments count by Integer.MAX_VALUE as it resets on reaching maximum value
    @Test
    public void testMultipleIncrementationWithMaxLimit() {
        long requiredIncrementCount = Integer.MAX_VALUE + 10L;

        Incrementor incrementor = createNewIncrementorInstance();

        for (long i = 0; i < requiredIncrementCount; ++i) {
            incrementor.incrementNumber();
        }

        assertThat((long)incrementor.getNumber(), is(requiredIncrementCount % Integer.MAX_VALUE));
    }

    // Incrementor value must match modulus of increments count by maximum value as it resets on reaching maximum value
    @Test
    public void testMultipleIncrementationWithLimit() {
        int maximumValue = 100;
        int requiredIncrementCount = 1024;

        Incrementor incrementor = createNewIncrementorInstance();

        incrementor.setMaximumValue(maximumValue);
        for (int i = 0; i < requiredIncrementCount; ++i) {
            incrementor.incrementNumber();
        }

        assertThat(incrementor.getNumber(), is(requiredIncrementCount % maximumValue));
    }

    // Incrementor value must reset if maximum value is set less or equal to current value
    @Test
    public void testSettingLimitLessThanCurrent() {
        int maximumValue = 100;
        int requiredIncrementCount = 256;

        Incrementor incrementor = createNewIncrementorInstance();

        for (int i = 0; i < requiredIncrementCount; ++i) {
            incrementor.incrementNumber();
        }

        incrementor.setMaximumValue(maximumValue);

        assertThat(incrementor.getNumber(), is(0));
    }
}
