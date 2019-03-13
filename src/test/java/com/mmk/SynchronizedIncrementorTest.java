package com.mmk;

public class SynchronizedIncrementorTest extends MultithreadIncrementorTest {
    @Override
    Incrementor createNewIncrementorInstance() {
         return new SynchronizedIncrementor();
    }
}
