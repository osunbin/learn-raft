package com.bin.raft.core;

public enum RaftRole {

    /**
     * Followers are passive, they issue no requests on their own
     * but simply respond to requests from leaders and candidates.
     */
    FOLLOWER,

    /**
     * Candidate is used to elect a new leader. When a candidate wins votes of
     * majority, it becomes the leader. Otherwise it becomes follower
     * or candidate again.
     */
    CANDIDATE,

    /**
     * The leader handles all client requests (append entry, membership change,
     * etc.) and replicates them to followers.
     */
    LEADER
}