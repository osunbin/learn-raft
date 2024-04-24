package com.bin.raft.config;



public class NetworkConfig {


    private static final int CONNECTION_TIMEOUT_SEC = 5;

    /**
     * Default value of port number.
     */
    public static final int DEFAULT_PORT = 5701;



    private int connectionTimeoutSeconds = CONNECTION_TIMEOUT_SEC;



    private int port = DEFAULT_PORT;


    private int workThreads = 2;

    public int getConnectionTimeoutSeconds() {
        return connectionTimeoutSeconds;
    }




    public static NetworkConfig defaultNetworkConfig() {
        return new NetworkConfig();
    }

    public int getPort() {
        return port;
    }

    public NetworkConfig setPort(int port) {
        this.port = port;
        return this;
    }


    public int getWorkThreads() {
        return workThreads;
    }

    public NetworkConfig setWorkThreads(int workThreads) {
        this.workThreads = workThreads;
        return this;
    }

}
