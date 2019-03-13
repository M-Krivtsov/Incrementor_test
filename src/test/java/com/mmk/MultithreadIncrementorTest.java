package com.mmk;

import com.mmk.util.StopWatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.BrokenBarrierException;
import java.util.Locale;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public abstract class MultithreadIncrementorTest extends SinglethreadIncrementorTest {
    // Multi-Thread aware Incrementor value must match count of increments if it is less than maximum (Integer.MAX_VALUE)
    public void testMultipleIncrementationMultithread(Incrementor incrementor, int threadCount, int maximumValue) {
        int requiredIncrementCount = HIGH_INCREMENT_COUNT; // expected total count of increments
        // split evenly between threads
        int requiredIncrementCountPerThread = (requiredIncrementCount + threadCount - 1) / threadCount;

        StopWatch stopWatch = new StopWatch();                                           // measure performance of multithreaded increments
        CyclicBarrier startBarrier = new CyclicBarrier(threadCount, stopWatch::start);   // thread start synchronization barrier
        CyclicBarrier stopBarrier = new CyclicBarrier(threadCount + 1, stopWatch::stop); // job done synchronization barrier

        // increments left to assign to threads
        int remainingIncrementCount = requiredIncrementCount;
        // spawn threads
        for (int i = 0; i < threadCount; ++i) {
            // get at most count-per-thread increments for this thread
            int incrementCountForThisThread = Math.min(requiredIncrementCountPerThread, remainingIncrementCount);
            remainingIncrementCount -= incrementCountForThisThread;

            // create new thread for calculated number of increments
            Thread thread = new Thread(() -> {
                try {                                    // ensure all threads start at 'same time'
                    startBarrier.await();                // wait until all threads are ready
                }
                catch (InterruptedException exc) {
                    fail("Thread interrupted");
                }
                catch (BrokenBarrierException exc) {
                    fail("Barrier broken");
                }

                // increment until expected number of increments is achieved
                for (int j = 0; j < incrementCountForThisThread; ++j) {
                    incrementor.incrementNumber();
                }

                try {
                    stopBarrier.await();                 // park finished thread until all increments are performed on all threads
                }
                catch (InterruptedException exc) {
                    fail("Thread interrupted");
                }
                catch (BrokenBarrierException exc) {
                    fail("Barrier broken");
                }
            });
            thread.start();
        }

        try {
            // wait for finish of all increment threads
            stopBarrier.await();
        }
        catch (InterruptedException exc) {
            fail("Thread interrupted");
        }
        catch (BrokenBarrierException exc) {
            fail("Barrier broken");
        }

        // check results
        assertThat(incrementor.getNumber(), is(requiredIncrementCount % maximumValue));

        System.out.println(String.format(Locale.getDefault(), "%d increments took %d ms on %d threads", requiredIncrementCount, stopWatch.getElapsedTimeMillis(), threadCount));
    }

    // Multi-Thread aware Incrementor value must match count of increments if it is less than maximum (Integer.MAX_VALUE) on single thread
    @Test
    public void testMultipleIncrementationSinglethread() {
        testMultipleIncrementationMultithread(createNewIncrementorInstance(), 1, Integer.MAX_VALUE);
    }

    // Multi-Thread aware Incrementor value must match modulus of increments count by maximum value as it resets on reaching maximum value on single thread
    @Test
    public void testMultipleIncrementationSinglethreadWithLimit() {
        int maximumValue = 250;

        Incrementor incrementor = createNewIncrementorInstance();
        incrementor.setMaximumValue(maximumValue);
        testMultipleIncrementationMultithread(incrementor, 1, maximumValue);
    }

    // Multi-Thread aware Incrementor value must match count of increments if it is less than maximum (Integer.MAX_VALUE) on multi threads
    @Test
    public void testMultipleIncrementationMultithread() {
        testMultipleIncrementationMultithread(createNewIncrementorInstance(), 16, Integer.MAX_VALUE);
    }

    // Multi-Thread aware Incrementor value must match modulus of increments count by maximum value as it resets on reaching maximum value on multiple threads
    @Test
    public void testMultipleIncrementationMultithreadWithLimit() {
        int maximumValue = 250;

        Incrementor incrementor = createNewIncrementorInstance();
        incrementor.setMaximumValue(maximumValue);
        testMultipleIncrementationMultithread(incrementor, 16, maximumValue);
    }

    // Multi-Thread aware Incrementor value must match modulus of increments count by maximum value as it resets on reaching maximum value on multiple threads
    // in case of more threads than maximumValue
    @Test
    public void testMultipleIncrementationOnManyThreadsWithLimit() {
        int maximumValue = 250;

        Incrementor incrementor = createNewIncrementorInstance();
        incrementor.setMaximumValue(maximumValue);
        testMultipleIncrementationMultithread(incrementor, maximumValue * 8, maximumValue);
    }

}
