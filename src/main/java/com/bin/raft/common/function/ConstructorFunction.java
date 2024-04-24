package com.bin.raft.common.function;

@FunctionalInterface
public interface ConstructorFunction<K, V> {


    V createNew(K arg);
}