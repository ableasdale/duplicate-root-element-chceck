package com.marklogic.support.providers;

import java.util.concurrent.atomic.AtomicLong;

public class CounterProvider {

    private final AtomicLong counter = new AtomicLong();

    private static class CounterProviderHolder {
        private static final CounterProvider INSTANCE = new CounterProvider();
    }

    public static CounterProvider getInstance() {
        return CounterProviderHolder.INSTANCE;
    }

    public AtomicLong getCounter() {
        return counter;
    }

    public long getTotalCountSoFar(){
        return getInstance().counter.get();
    }
}
