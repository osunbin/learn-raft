package com.bin.raft.execution;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class PoolExecutorThreadFactory implements ThreadFactory {

    private final String threadNamePrefix;
    private final AtomicInteger idGen = new AtomicInteger(0);

    public PoolExecutorThreadFactory(String threadNamePrefix) {
        this.threadNamePrefix = threadNamePrefix;
    }


    @Override
    public Thread newThread(Runnable r) {
        String name = threadNamePrefix + idGen.incrementAndGet();
        return new Thread(r,name);
    }
}
