package com.bin.raft.cluster;

import com.bin.raft.common.utils.AddressUtil;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

import static com.bin.raft.common.utils.Preconditions.checkNotNull;

public class Address {


    private static final byte IPV4 = 4;
    private static final byte IPV6 = 6;

    private int port = -1;
    private String host;
    private byte type;

    private String scopeId;
    private boolean hostSet;

    public Address() {
    }

    public Address(String host, int port) throws UnknownHostException {
        this(host, InetAddress.getByName(host), port);
    }

    public Address(InetAddress inetAddress, int port) {
        this(null, inetAddress, port);
        hostSet = false;
    }


    public Address(InetSocketAddress inetSocketAddress) {
        this(resolve(inetSocketAddress), inetSocketAddress.getPort());
    }

    public Address(String hostname, InetAddress inetAddress, int port) {
        checkNotNull(inetAddress, "inetAddress can't be null");

        type = (inetAddress instanceof Inet4Address) ? IPV4 : IPV6;
        String[] addressArgs = inetAddress.getHostAddress().split("\\%");
        host = hostname != null ? hostname : addressArgs[0];
        if (addressArgs.length == 2) {
            scopeId = addressArgs[1];
        }
        this.port = port;
        hostSet = !AddressUtil.isIpAddress(host);
    }

    public Address(Address address) {
        this.host = address.host;
        this.port = address.port;
        this.type = address.type;
        this.scopeId = address.scopeId;
        this.hostSet = address.hostSet;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getInetAddress() throws UnknownHostException {
        return InetAddress.getByName(getScopedHost());
    }

    public InetSocketAddress getInetSocketAddress() throws UnknownHostException {
        return new InetSocketAddress(getInetAddress(), port);
    }

    public boolean isIPv4() {
        return type == IPV4;
    }

    public boolean isIPv6() {
        return type == IPV6;
    }

    public String getScopeId() {
        return isIPv6() ? scopeId : null;
    }

    public void setScopeId(final String scopeId) {
        if (isIPv6()) {
            this.scopeId = scopeId;
        }
    }

    public String getScopedHost() {
        return (isIPv4() || hostSet || scopeId == null) ? getHost()
                : getHost() + '%' + scopeId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Address)) {
            return false;
        }
        final Address address = (Address) o;
        return port == address.port && this.type == address.type && this.host.equals(address.host);
    }

    @Override
    public int hashCode() {
        int result = port;
        result = 31 * result + host.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return '[' + host + "]:" + port;
    }

    private static InetAddress resolve(InetSocketAddress inetSocketAddress) {
        checkNotNull(inetSocketAddress, "inetSocketAddress can't be null");

        InetAddress address = inetSocketAddress.getAddress();
        if (address == null) {
            throw new IllegalArgumentException("Can't resolve address: " + inetSocketAddress);
        }
        return address;
    }
}
