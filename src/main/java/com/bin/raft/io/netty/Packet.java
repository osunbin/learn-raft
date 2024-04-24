package com.bin.raft.io.netty;

import io.netty.buffer.ByteBuf;

public final class Packet {
    private ByteBuf payload;


    public Packet(ByteBuf payload) {
        this.payload = payload;
    }

    public ByteBuf getPayload() {
        return payload;
    }
}
