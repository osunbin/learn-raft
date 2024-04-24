package com.bin.raft;

import java.io.Serializable;

public class RaftGroupId implements Serializable {

    private static final long serialVersionUID = -2381010126931378167L;


    private String name;
    private long seed;
    private long groupId;

    public RaftGroupId() {
    }

    public RaftGroupId(String name, long seed, long groupId) {
        assert name != null;
        this.name = name;
        this.seed = seed;
        this.groupId = groupId;
    }


    public String getName() {
        return name;
    }

    public long getSeed() {
        return seed;
    }

    public long getId() {
        return groupId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RaftGroupId that = (RaftGroupId) o;

        if (seed != that.seed) {
            return false;
        }
        if (groupId != that.groupId) {
            return false;
        }
        return name.equals(that.name);
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + (int) (seed ^ (seed >>> 32));
        result = 31 * result + (int) (groupId ^ (groupId >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "RaftGroupId{" + "name='" + name + '\'' + ", seed=" + seed + ", groupId=" + groupId + '}';
    }
}
