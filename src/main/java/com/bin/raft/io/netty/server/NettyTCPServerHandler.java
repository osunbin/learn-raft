package com.bin.raft.io.netty.server;



import com.bin.raft.io.netty.Packet;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


import java.util.function.Consumer;

@ChannelHandler.Sharable
public class NettyTCPServerHandler extends SimpleChannelInboundHandler<ByteBuf> {



    private Consumer<Packet> packets;


    public NettyTCPServerHandler(Consumer<Packet> packets) {
        this.packets = packets;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
        packets.accept(new Packet(byteBuf));
    }

}
