package com.bin.raft.common.utils;

import com.bin.raft.common.function.ConstructorFunction;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

public class ConcurrencyUtil {

    public static final Executor CALLER_RUNS = new Executor() {
        @Override
        public void execute(Runnable command) {
            command.run();
        }

        @Override
        public String toString() {
            return "CALLER_RUNS";
        }
    };


    public static <K, V> V getOrPutSynchronized(ConcurrentMap<K, V> map, K key, final Object mutex,
                                                ConstructorFunction<K, V> func) {
        if (mutex == null) {
            throw new NullPointerException();
        }
        V value = map.get(key);
        if (value == null) {
            synchronized (mutex) {
                value = map.get(key);
                if (value == null) {
                    value = func.createNew(key);
                    map.put(key, value);
                }
            }
        }
        return value;
    }



    public static <K, V> V getOrPutIfAbsent(ConcurrentMap<K, V> map, K key, ConstructorFunction<K, V> func) {
        V value = map.get(key);
        if (value == null) {
            value = func.createNew(key);
            V current = map.putIfAbsent(key, value);
            value = current == null ? value : current;
        }
        return value;
    }
}
