package com.bin.raft;

import com.bin.raft.cluster.Address;

import java.io.Serializable;
import java.util.Objects;

import static com.bin.raft.common.utils.Preconditions.checkNotNull;

public class RaftMember implements Serializable {

    private static final long serialVersionUID = 5628148969327743953L;

    private String id;
    private Address address;
    private transient RaftEndpoint endpoint;


    public RaftMember(String id, Address address) {
        checkNotNull(id);
        checkNotNull(address);
        this.id = id;
        this.endpoint = new RaftEndpoint(id);
        this.address = address;
    }




    public String getId() {
        return id;
    }


    public Address getAddress() {
        return address;
    }

    public RaftEndpoint toRaftEndpoint() {
        return endpoint;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RaftMember raftMember = (RaftMember) o;
        return Objects.equals(id, raftMember.id) && Objects.equals(address, raftMember.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, address);
    }
}
