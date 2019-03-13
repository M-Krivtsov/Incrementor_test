package com.mmk;

public class AtomicIncrementorTest extends MultithreadIncrementorTest {
    @Override
    Incrementor createNewIncrementorInstance() {
         return new AtomicIncrementor();
    }
}
