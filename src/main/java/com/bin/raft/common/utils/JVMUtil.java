package com.bin.raft.common.utils;

import java.lang.management.ManagementFactory;

public class JVMUtil {

    public static long getPid() {
        String name = ManagementFactory.getRuntimeMXBean().getName();

        if (name == null) {
            return -1;
        }
        int separatorIndex = name.indexOf("@");
        if (separatorIndex < 0) {
            return -1;
        }
        String potentialPid = name.substring(0, separatorIndex);
        try {
            return Long.parseLong(potentialPid);
        } catch (NumberFormatException e) {
            return -1;
        }
    }


}
