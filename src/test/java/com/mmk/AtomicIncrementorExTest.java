package com.mmk;

public class AtomicIncrementorExTest extends MultithreadIncrementorTest {
    @Override
    Incrementor createNewIncrementorInstance() {
         return new AtomicIncrementorEx();
    }
}
