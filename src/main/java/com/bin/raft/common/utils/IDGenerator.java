package com.bin.raft.common.utils;

import java.security.SecureRandom;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class IDGenerator {





    public static String newUnsecureUuidString(){
        return newUnsecureUUID().toString();
    }

    public static String newSecureUuidString() {
        return newSecureUUID().toString();
    }

    public static UUID newUnsecureUUID() {
        return getUUID(ThreadLocalRandom.current());
    }


    public static UUID newSecureUUID() {
        return getUUID(new SecureRandom());
    }


    private static UUID getUUID(Random random) {
        byte[] data = new byte[16];
        random.nextBytes(data);

        // clear version
        data[6] &= 0x0f;
        // set to version 4
        data[6] |= 0x40;
        // clear variant
        data[8] &= 0x3f;
        // set to IETF variant
        data[8] |= 0x80;

        long mostSigBits = 0;
        long leastSigBits = 0;
        assert data.length == 16 : "data must be 16 bytes in length";
        for (int i = 0; i < 8; i++) {
            mostSigBits = (mostSigBits << 8) | (data[i] & 0xff);
        }
        for (int i = 8; i < 16; i++) {
            leastSigBits = (leastSigBits << 8) | (data[i] & 0xff);
        }
        return new UUID(mostSigBits, leastSigBits);
    }
}
