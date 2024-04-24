package com.bin.raft.config;

import com.bin.raft.cluster.Address;

import java.net.UnknownHostException;
import java.util.Collections;
import java.util.Set;

import static com.bin.raft.config.NetworkConfig.defaultNetworkConfig;

public class RaftConfig {

    private Set<Address> address = Collections.emptySet();

    private Address localAddress;

    private String id;

    private NetworkConfig networkConfig = defaultNetworkConfig();


    public Set<Address> getAddress() {
        return address;
    }

    public RaftConfig setAddress(Set<Address> address) {
        this.address = address;
        return this;
    }


    public int getRaftMemberCount() {
        return address.size();
    }

    public Address getLocalAddress() {
        return localAddress;
    }

    public void setLocalAddress(String host, int port) throws UnknownHostException {
        this.localAddress = new Address(host, port);
    }

    public NetworkConfig getNetworkConfig() {
        return networkConfig;
    }

    public void setNetworkConfig(NetworkConfig networkConfig) {
        this.networkConfig = networkConfig;
    }


    public boolean  check() {
        if (localAddress == null)
            return false;

        return true;
    }
}
