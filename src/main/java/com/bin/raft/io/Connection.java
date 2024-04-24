package com.bin.raft.io;

import com.bin.raft.cluster.Address;
import com.bin.raft.operation.Operation;

import java.io.IOException;

public interface Connection {


    void connect(Address addr, int timeout) throws IOException;


    boolean write(Operation frame);


    void close() throws IOException;
}
