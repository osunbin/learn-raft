package com.bin.raft.common.utils;

import java.util.Iterator;
import java.util.NoSuchElementException;

public final class Preconditions {

    private Preconditions() {
    }


    public static String checkHasText(String argument, String errorMessage) {
        if (argument == null || argument.isEmpty()) {
            throw new IllegalArgumentException(errorMessage);
        }

        return argument;
    }


    public static <T> T checkNotNull(T argument, String errorMessage) {
        if (argument == null) {
            throw new NullPointerException(errorMessage);
        }
        return argument;
    }


    public static <T> Iterable<T> checkNoNullInside(Iterable<T> argument, String errorMessage) {
        if (argument == null) {
            return argument;
        }
        for (T element : argument) {
            checkNotNull(element, errorMessage);
        }
        return argument;
    }


    public static <T> T checkNotNull(T argument) {
        if (argument == null) {
            throw new NullPointerException();
        }
        return argument;
    }


    public static <E> E isNotNull(E argument, String argName) {
        if (argument == null) {
            throw new IllegalArgumentException(String.format("argument '%s' can't be null", argName));
        }

        return argument;
    }


    public static long checkNotNegative(long value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }


    public static int checkNotNegative(int value, String errorMessage) {
        if (value < 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }


    public static long checkNegative(long value, String errorMessage) {
        if (value >= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }


    public static long checkPositive(String paramName, long value) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " is " + value + " but must be > 0");
        }
        return value;
    }


    public static double checkPositive(double value, String errorMessage) {
        if (value <= 0) {
            throw new IllegalArgumentException(errorMessage);
        }
        return value;
    }


    public static int checkPositive(String paramName, int value) {
        if (value <= 0) {
            throw new IllegalArgumentException(paramName + " is " + value + " but must be > 0");
        }
        return value;
    }


    public static <E> E checkInstanceOf(Class<E> type, Object object, String errorMessage) {
        isNotNull(type, "type");
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException(errorMessage);
        }
        return (E) object;
    }

    public static <E> E checkInstanceOf(Class<E> type, Object object) {
        isNotNull(type, "type");
        if (!type.isInstance(object)) {
            throw new IllegalArgumentException(object + " must be instanceof " + type.getName());
        }
        return (E) object;
    }


    public static <E> E checkNotInstanceOf(Class type, E object, String errorMessage) {
        isNotNull(type, "type");
        if (type.isInstance(object)) {
            throw new IllegalArgumentException(errorMessage);
        }
        return object;
    }


    public static void checkFalse(boolean expression, String errorMessage) {
        if (expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public static void checkTrue(boolean expression, String errorMessage) {
        if (!expression) {
            throw new IllegalArgumentException(errorMessage);
        }
    }


    public static void checkTrueUnsupportedOperation(boolean expression, String errorMessage) {
        if (!expression) {
            throw new UnsupportedOperationException(errorMessage);
        }
    }



    public static <T> Iterator<T> checkHasNext(Iterator<T> iterator, String message) throws NoSuchElementException {
        if (!iterator.hasNext()) {
            throw new NoSuchElementException(message);
        }
        return iterator;
    }

    /**
     * Check the state of a condition
     *
     * @param condition
     * @param message
     * @throws IllegalStateException if condition if false
     */
    public static void checkState(boolean condition, String message) throws IllegalStateException {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
