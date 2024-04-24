package com.bin.raft.io.netty.server;

import com.bin.raft.cluster.Address;
import com.bin.raft.common.netty.NettyHelper;
import com.bin.raft.config.NetworkConfig;
import com.bin.raft.io.TcpServer;
import com.bin.raft.io.netty.Packet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteOrder;
import java.util.function.Consumer;

public class NettyTcpServer implements TcpServer {

    private static Logger logger = LoggerFactory.getLogger(NettyTcpServer.class);


    private Channel parentChannel;



    private int port = 5701;

    private int workThreads = 3;

    NettyTCPServerHandler nettyHandler;

    private NetworkConfig networkConfig;

    public NettyTcpServer(Consumer<Packet> packets,NetworkConfig networkConfig) {
        nettyHandler = new NettyTCPServerHandler(packets);
        this.networkConfig = networkConfig;
        if ( networkConfig.getWorkThreads() != 0)
             workThreads = networkConfig.getWorkThreads();
    }


    public void start() {
         newTcpServer();

    }





    private SocketAddress newTcpServer() {

        EventLoopGroup bossGroup = NettyHelper.newNioOrEpollEventLoopGroup(1,"raft tcp server boss");

        EventLoopGroup workGroup = NettyHelper.newNioOrEpollEventLoopGroup(workThreads,"raft tcp server worker");

        ServerBootstrap serverBootstrap = NettyHelper.serverBootstrap(bossGroup,workGroup);
        serverBootstrap
                .option(ChannelOption.ALLOCATOR, ByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                .childOption(ChannelOption.SO_RCVBUF, 16 * 1024);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            public void initChannel(SocketChannel ch) {
                ChannelPipeline pipeline = ch.pipeline();
                ch.pipeline().addLast("frameDecoder", new LengthFieldBasedFrameDecoder(ByteOrder.LITTLE_ENDIAN, Integer.MAX_VALUE, 0, 4, 0, 0, true));

                ch.pipeline().addLast("handler", nettyHandler);
            }
        });

        ChannelFuture channelFuture = serverBootstrap.bind(port).syncUninterruptibly().addListener(future -> {

            logger.info("Netty tcp Server started on port {}", port);
        });
        channelFuture.channel().closeFuture().addListener(future -> {
            logger.info("Netty tcp  Server Start Shutdown ............");
            bossGroup.shutdownGracefully().syncUninterruptibly();
            workGroup.shutdownGracefully().syncUninterruptibly();
        });
        parentChannel = channelFuture.channel();
        return channelFuture.channel().localAddress();
    }




    @Override
    public void shutdown() {
        logger.info("shutdown called {}", networkConfig.getPort());
        if (parentChannel != null) {
            parentChannel.close();
        }
    }

}
