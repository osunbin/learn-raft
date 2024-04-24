package com.bin.raft.common.utils;

public class OsHelper {

    public static final String OS = StringUtil.lowerCaseInternal(System.getProperty("os.name"));



    private OsHelper() {
    }


    public static boolean isLinux() {
        return OS.contains("nux");
    }



    public static boolean isUnixFamily() {
        return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix"));
    }



    public static boolean isMac() {
        return (OS.contains("mac") || OS.contains("darwin"));
    }


    public static boolean isWindows() {
        return OS.contains("windows");
    }

}
