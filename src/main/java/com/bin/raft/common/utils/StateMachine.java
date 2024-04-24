package com.bin.raft.common.utils;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.bin.raft.common.utils.Preconditions.checkNotNull;
import static com.bin.raft.common.utils.Preconditions.checkState;

public class StateMachine<T extends Enum<T>> {

    private Map<T, Set<T>> transitions = new HashMap<T, Set<T>>();
    private T currentState;

    public StateMachine(T initialState) {
        currentState = initialState;
    }

    public static <T extends Enum<T>> StateMachine<T> of(T initialState) {
        return new StateMachine<T>(initialState);
    }


    public StateMachine<T> withTransition(T from, T to, T... moreTo) {
        transitions.put(from, EnumSet.of(to, moreTo));
        return this;
    }



    public StateMachine<T> next(T nextState) throws IllegalStateException {
        Set<T> allowed = transitions.get(currentState);
        checkNotNull(allowed, "No transitions from state " + currentState);
        checkState(allowed.contains(nextState), "Transition not allowed from state " + currentState + " to " + nextState);
        currentState = nextState;
        return this;
    }


    public void nextOrStay(T nextState) {
        if (!is(nextState)) {
            next(nextState);
        }
    }


    public boolean is(T state, T... otherStates) {
        return EnumSet.of(state, otherStates).contains(currentState);
    }

    @Override
    public String toString() {
        return "StateMachine{state=" + currentState + "}";
    }


}
