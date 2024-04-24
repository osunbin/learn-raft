package com.bin.raft.core;

import com.bin.raft.RaftGroupId;
import com.bin.raft.RaftMember;
import com.bin.raft.common.exception.RaftException;
import com.bin.raft.common.utils.IDGenerator;
import com.bin.raft.config.NetworkConfig;
import com.bin.raft.config.RaftConfig;
import com.bin.raft.execution.ExecutionService;
import com.bin.raft.io.TcpServer;
import com.bin.raft.io.netty.server.NettyTcpServer;
import org.apache.logging.log4j.core.util.UuidUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


public class RaftService {

    private final Logger logger = LoggerFactory.getLogger(RaftService.class);


    private final ReadWriteLock nodeLock = new ReentrantReadWriteLock();


    private final ConcurrentMap<RaftGroupId, RaftNode> nodes = new ConcurrentHashMap<>();

    private ExecutionService executionService = new ExecutionService();

    private RaftConfig raftConfig;


    private TcpServer tcpServer;

    private final boolean raftClusterEnabled;

    private final MetadataRaftGroupManager metadataGroupManager;


    public RaftService(RaftConfig raftConfig) throws RaftException {
        if (raftConfig == null)
            throw new RaftException("raftConfig is null");

        if (!raftConfig.check())
            throw new RaftException("raftConfig not localAddress");


        this.raftConfig = raftConfig;
        // TODO
        tcpServer =  new NettyTcpServer(null,raftConfig.getNetworkConfig());

        this.raftClusterEnabled = raftConfig.getRaftMemberCount() > 0;
        this.metadataGroupManager = new MetadataRaftGroupManager(this, raftConfig);

    }


    public void start() {

        tcpServer.start();

        metadataGroupManager.start();
    }



    public RaftConfig getRaftConfig() {
        return raftConfig;
    }

    public boolean isRaftClusterEnabled() {
        return raftClusterEnabled;
    }


    public ExecutionService getExecutionService() {
        return executionService;
    }


    public void shutdown() {
        // TODO
        tcpServer.shutdown();
    }
}
