package com.bin.raft.core.persistence;

import com.bin.raft.config.RaftConfig;

import java.io.File;

public class RaftPersistenceServiceImpl implements RaftPersistenceService{

    private  File dir;

    private RaftMetadataStore metadataStore;


    public RaftPersistenceServiceImpl(RaftConfig raftConfig) {

    }

}
