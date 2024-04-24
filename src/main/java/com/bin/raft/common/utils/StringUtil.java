package com.bin.raft.common.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.Character.isLowerCase;
import static java.lang.Character.toLowerCase;

public class StringUtil {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final Locale LOCALE_INTERNAL = Locale.ROOT;


    public static String bytesToString(byte[] bytes, int offset, int length) {
        return new String(bytes, offset, length, StandardCharsets.UTF_8);
    }

    public static String bytesToString(byte[] bytes) {

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] stringToBytes(String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }



    public static boolean isNullOrEmpty(String s) {
        if (s == null) {
            return true;
        }
        return s.trim().isEmpty();
    }


    public static String upperCaseInternal(String s) {
        if (isNullOrEmpty(s)) {
            return s;
        }
        return s.toUpperCase(LOCALE_INTERNAL);
    }

    public static String lowerCaseFirstChar(String s) {
        if (s.isEmpty()) {
            return s;
        }

        char first = s.charAt(0);
        if (isLowerCase(first)) {
            return s;
        }

        return toLowerCase(first) + s.substring(1);
    }


    public static String lowerCaseInternal(String s) {
        if (isNullOrEmpty(s)) {
            return s;
        }
        return s.toLowerCase(LOCALE_INTERNAL);
    }



    public static <T> String toString(Collection<T> collection) {
        return collection.stream()
                .map(Objects::toString)
                .collect(Collectors.joining(LINE_SEPARATOR));
    }



}
