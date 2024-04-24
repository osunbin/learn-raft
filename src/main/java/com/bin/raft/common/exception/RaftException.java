package com.bin.raft.common.exception;

public class RaftException extends RuntimeException {

    private final String leaderId;

    public RaftException(String leaderId) {
        this.leaderId = leaderId;
    }

    public RaftException(final String message,String leaderId) {
        super(message);
        this.leaderId = leaderId;
    }

    public RaftException(final String message, final Throwable cause,String leaderId) {
        super(message, cause);
        this.leaderId = leaderId;
    }

    public RaftException(final Throwable cause,String leaderId) {
        super(cause);
        this.leaderId = leaderId;
    }

    public String getLeaderId() {
        return leaderId;
    }
}
