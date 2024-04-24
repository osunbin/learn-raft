package com.bin.raft.io;

import com.bin.raft.cluster.Address;

public interface TcpServer {
    void start();



    void shutdown();
}
