package com.bin.raft.core;

import com.bin.raft.RaftMember;
import com.bin.raft.cluster.Address;
import com.bin.raft.common.netty.NettyHelper;
import com.bin.raft.common.utils.IDGenerator;
import com.bin.raft.config.RaftConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class MetadataRaftGroupManager {

    private static final Logger logger = LoggerFactory.getLogger(MetadataRaftGroupManager.class);

    private final AtomicBoolean discoveryCompleted = new AtomicBoolean();

    private final AtomicReference<RaftMember> localCPMember = new AtomicReference<>();


    private final RaftService raftService;

    private final RaftConfig raftConfig;

    private final boolean raftClusterEnabled;
    public MetadataRaftGroupManager(RaftService raftService, RaftConfig raftConfig) {
        this.raftService = raftService;
        this.raftConfig = raftConfig;
        raftClusterEnabled = raftService.isRaftClusterEnabled();
    }


    boolean start() {
        if (raftClusterEnabled) {
            scheduleDiscoverInitialRaftMembersTask(true);
        }
        return raftClusterEnabled;
    }




    private void scheduleDiscoverInitialRaftMembersTask(boolean terminateOnDiscoveryFailure) {

    }



    private enum DiscoveryTaskState {
        RUNNING, SCHEDULED, COMPLETED
        // scheduled  completed
    }

    private class DiscoverInitialCPMembersTask implements Runnable {

        private final boolean terminateOnDiscoveryFailure;
        private volatile boolean cancelled;
        private volatile DiscoveryTaskState state;

        DiscoverInitialCPMembersTask(boolean terminateOnDiscoveryFailure) {
            this.terminateOnDiscoveryFailure = terminateOnDiscoveryFailure;
            state = DiscoveryTaskState.SCHEDULED;
        }

        @Override
        public void run() {
            state = DiscoveryTaskState.RUNNING;
            try {
                doRun();
            } finally {
                if (state == DiscoveryTaskState.RUNNING) {
                    state = DiscoveryTaskState.COMPLETED;
                }
            }
        }


        private void doRun() {
            if (shouldRescheduleOrSkip()) {
                return;
            }



            RaftMember  localMember =
                    // TODO 自定义
                    new RaftMember(IDGenerator.newUnsecureUuidString(),raftConfig.getLocalAddress());
            Set<Address> address = raftConfig.getAddress();

            if (!commitMetadataRaftGroupInit(localMember, address)) {
                handleDiscoveryFailure();
                return;
            }

            discoveryCompleted.set(true);

        }


        private boolean commitMetadataRaftGroupInit(RaftMember  localMember,Set<Address> address) {

            return true;
        }


        private void handleDiscoveryFailure() {
            if (terminateOnDiscoveryFailure) {
                logger.warn("Terminating because of raft discovery failure...");
                terminateNode();
            } else {
                logger.warn("Cancelling raft Subsystem discovery...");
                discoveryCompleted.set(true);
            }
        }

        private void terminateNode() {
            raftService.shutdown();
        }



        private boolean shouldRescheduleOrSkip() {
            if (cancelled) {
                return true;
            }

            return discoveryCompleted.get();
        }

    }
}
