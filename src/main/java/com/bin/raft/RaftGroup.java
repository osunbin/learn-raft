package com.bin.raft;

import java.util.Set;

public class RaftGroup {


    /**
     * Name of the internal CP group that is used for maintaining CP groups
     * and CP members
     */
   public static final String METADATA_CP_GROUP_NAME = "METADATA";

    /**
     * Name of the DEFAULT CP group that is used when no group name is
     * specified while creating a CP data structure proxy.
     */
    public static final String DEFAULT_GROUP_NAME = "default";


    private RaftGroupId id;


    private RaftGroupStatus status;

    private Set<RaftMember> raftMembers;

    public RaftGroup() {
    }


    public RaftGroup(RaftGroupId id, RaftGroupStatus status, Set<RaftMember> raftMembers) {
        this.id = id;
        this.status = status;
        this.raftMembers = raftMembers;
    }


    public RaftGroupId getId() {
        return id;
    }

    public RaftGroupStatus getStatus() {
        return status;
    }

    public Set<RaftMember> getMembers() {
        return raftMembers;
    }


    @Override
    public String toString() {
        return "RaftGroup{" + "id=" + id + ", status=" + status +  ", members="
                + raftMembers + '}';
    }

    public static enum RaftGroupStatus {
        /**
         * A CP group is active after it is initialized with the first request
         * sent to it, and before its destroy process is initialized.
         */
        ACTIVE,

        /**
         * A CP group switches to this state after its destroy process is
         * initialized, but not completed yet.
         */
        DESTROYING,

        /**
         * A CP group switches to this state after its destroy process is
         * completed.
         */
        DESTROYED
    }
}
