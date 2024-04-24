package com.bin.raft.io.netty.client;

import com.bin.raft.cluster.Address;
import com.bin.raft.common.netty.NettyHelper;
import com.bin.raft.operation.Operation;
import com.bin.raft.io.Connection;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteOrder;

public class NettyTcpClient implements Connection {

    private final Logger logger = LoggerFactory.getLogger(NettyTcpClient.class);


    private Channel channel;


    private ChannelFuture connectFuture;


    private SimpleChannelInboundHandler<ByteBuf> clientHandler;


    public NettyTcpClient(SimpleChannelInboundHandler<ByteBuf> inboundHandler) {
        clientHandler = inboundHandler;
    }




   public void connect(Address addr,int timeout) throws IOException {

        EventLoopGroup eventLoopGroup =
                NettyHelper.newNioOrEpollEventLoopGroup(1);
        Bootstrap bootstrap = NettyHelper.clientBootstrap(eventLoopGroup);
        bootstrap.channel(NettyHelper.nioOrEpollSocketChannel())
                .option(ChannelOption.SO_LINGER, -1)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS,timeout)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) {
                        ChannelPipeline pipeline = ch.pipeline();
                        ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, 4, 0, 0, true));

                        ch.pipeline().addLast("handler", clientHandler);
                    }
                });


        connectFuture = bootstrap.connect(addr.getHost(), addr.getPort());
        connectFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                // this lock guarantees that channel won't be assigned after cleanup().
                boolean connected = false;

                try {
                    if (!channelFuture.isSuccess()) {
                        logger.warn("future isn't success.", channelFuture.cause());
                        return;
                    } else if (connectFuture == null) {
                        logger.info("connect attempt cancelled");

                        channelFuture.channel().close();
                        return;
                    }

                    channel = channelFuture.channel();


                    connected = true;
                } finally {
                    connectFuture = null;

                    if (connected) {
                        logger.info("channel is connected: {}", channelFuture.channel());
                    }
                }
            }
        });

        connectFuture.channel().closeFuture().addListener(future -> {
            logger.info("Netty tcp  Server Start Shutdown ............");
            eventLoopGroup.shutdownGracefully().syncUninterruptibly();
        });

    }


    @Override
    public boolean write(Operation frame) {
        //  TODO 这里做序列化
        channel.writeAndFlush(frame);
        return false;
    }


    public void close() {

        if (connectFuture != null) {
            connectFuture.cancel(false);
            connectFuture = null;
        }
        if (channel != null) {
            channel.close().syncUninterruptibly();
            channel = null;
        }
    }

}
