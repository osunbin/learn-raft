package com.bin.raft;

import java.util.Objects;

public class RaftEndpoint {

    private String id;

    public RaftEndpoint() {

    }

    public RaftEndpoint(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RaftEndpoint that = (RaftEndpoint) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
