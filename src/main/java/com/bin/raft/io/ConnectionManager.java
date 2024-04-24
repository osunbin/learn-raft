package com.bin.raft.io;

import com.bin.raft.cluster.Address;
import com.bin.raft.config.NetworkConfig;
import com.bin.raft.operation.Operation;
import com.bin.raft.io.netty.client.NettyTCPClientHandler;
import com.bin.raft.io.netty.client.NettyTcpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final Logger logger = LoggerFactory.getLogger(ConnectionManager.class);

    private static final int MILLIS_PER_SECOND = 1000;


    private final ConcurrentHashMap<Address, Connection> connectionMap = new ConcurrentHashMap<>(100);

    private NetworkConfig config;


    private NettyTCPClientHandler clientHandler = new NettyTCPClientHandler();

    // TODO
    public void write(Address remoteAddress, Operation op) {
        Connection connect = getOrConnect(remoteAddress);
        if (connect  == null) {
            return;
        }
        connect.write(op);
    }


    public Connection getOrConnect(Address remoteAddress) {
        Connection connection = getConnection(remoteAddress);
        if (connection == null) {
            if (logger.isDebugEnabled()) {
                logger.debug("Starting to connect to {}" , remoteAddress);
            }
            try {
                connection = tryToConnect(remoteAddress,
                        config.getConnectionTimeoutSeconds() * MILLIS_PER_SECOND);
            } catch (IOException e) {
                logger.error("Could not connect to: {},Reason:",remoteAddress,e);
            }
        }
        return connection;
    }


    private Connection tryToConnect(Address addr,int timeout) throws IOException {
        Connection nettyTcpClient = new NettyTcpClient(clientHandler);
        nettyTcpClient.connect(addr,timeout);
        putConnection(addr,nettyTcpClient);
        return nettyTcpClient;
    }


    public Connection getConnection(Address address) {
        return connectionMap.get(address);
    }



    public void putConnection(Address address, Connection connection) {
        connectionMap.put(address, connection);
    }

    public void putConnectionIfAbsent(Address address, Connection connection) {
        connectionMap.putIfAbsent(address, connection);
    }

}
