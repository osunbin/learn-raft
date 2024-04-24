package com.bin.raft.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public enum JavaVersion {

    FUTURE_JAVA(0),
    JAVA_8(8),
    JAVA_9(9),
    JAVA_10(10),
    JAVA_11(11),
    JAVA_12(12),
    JAVA_13(13),
    JAVA_14(14),
    JAVA_15(15),
    JAVA_16(16),
    JAVA_17(17);

    private final static Logger logger = LoggerFactory.getLogger(JavaVersion.class);


    public static final JavaVersion CURRENT_VERSION = detectCurrentVersion();

    private int majorVersion;

    JavaVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }


    public int getMajorVersion() {
        return majorVersion;
    }

    public static boolean isAtLeast(JavaVersion version) {
        return isAtLeast(CURRENT_VERSION, version);
    }

    public void setMajorVersion(int majorVersion) {
        this.majorVersion = majorVersion;
    }

    public static boolean isAtMost(JavaVersion version) {
        return isAtMost(CURRENT_VERSION, version);
    }

    static boolean isAtLeast(JavaVersion currentVersion, JavaVersion minVersion) {
        return currentVersion.getMajorVersion() >= minVersion.getMajorVersion();
    }

    static boolean isAtMost(JavaVersion currentVersion, JavaVersion maxVersion) {
        return currentVersion.getMajorVersion() <= maxVersion.getMajorVersion();
    }

    private static JavaVersion valueOf(int majorVersion) {
        for (JavaVersion version : values()) {
            if (version.majorVersion == majorVersion) {
                return version;
            }
        }
        return null;
    }


    private static JavaVersion detectCurrentVersion() {

        final Class runtimeClass = Runtime.class;
        final Class versionClass;

        try {
            versionClass = Class.forName("java.lang.Runtime$Version");
        } catch (ClassNotFoundException e) {
            if (logger.isDebugEnabled()) {
                logger.debug("Detected runtime version: Java 8");
            }
            return JAVA_8;
        }

        try {
            Method versionMethod = runtimeClass.getDeclaredMethod("version");
            Object versionObj = versionMethod.invoke(Runtime.getRuntime());
            Method majorMethod = versionClass.getDeclaredMethod("major");
            int majorVersion = (int) majorMethod.invoke(versionObj);

            if (logger.isDebugEnabled()) {
                logger.debug("Detected runtime version: Java " + majorVersion);
            }

            JavaVersion foundVersion = valueOf(majorVersion);
            if (foundVersion != null) {
                return foundVersion;
            }

            logger.info("Detected runtime future version: Java " + majorVersion);

            FUTURE_JAVA.setMajorVersion(majorVersion);
            return FUTURE_JAVA;
        } catch (Exception e) {
            logger.warn("Unable to detect Java version, falling back to Java 8", e);
            return JAVA_8;
        }
    }


    public static void main(String[] args) {

        System.out.println(detectCurrentVersion().getMajorVersion());
    }


}
