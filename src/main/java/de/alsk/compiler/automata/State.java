package de.alsk.compiler.automata;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.Collection;
import java.util.Objects;

public class State<T> {
    static<T> State<T> normal() {
        return new State<>(Type.NORMAL, new ArrayListValuedHashMap<>());
    }

    static<T> State<T> normal(MultiValuedMap<T, State<T>> transitions) {
        return new State<>(Type.NORMAL, transitions);
    }

    static<T> State<T> accepting() {
        return new State<>(Type.ACCEPTING, new ArrayListValuedHashMap<>());
    }

    static<T> State<T> accepting(MultiValuedMap<T, State<T>> transitions) {
        return new State<>(Type.ACCEPTING, transitions);
    }

    static<T> State<T> error() {
        return new State<>(Type.ERROR, new ArrayListValuedHashMap<>());
    }

    static<T> State<T> error(MultiValuedMap<T, State<T>> transitions) {
        return new State<>(Type.ERROR, transitions);
    }

    static<T> State<T> toNormal(State<T> state) {
        state.type = Type.NORMAL;
        return state;
    }

    static<T> State<T> toAccepting(State<T> state) {
        state.type = Type.ACCEPTING;
        return state;
    }

    static<T> State<T> toError(State<T> state) {
        state.type = Type.ERROR;
        state.transitions = new ArrayListValuedHashMap<>();
        return state;
    }

    private enum Type {
        NORMAL,
        ACCEPTING,
        ERROR
    }

    private MultiValuedMap<T, State<T>> transitions;
    private Type type;

    private State(Type type) {
        this(type, new ArrayListValuedHashMap<>());
    }

    private State(Type type, MultiValuedMap<T, State<T>> transitions) {
        this.transitions = Objects.requireNonNullElse(transitions, new ArrayListValuedHashMap<>());
        this.type = type;
    }

    boolean isNormal() {
        return Type.NORMAL.equals(type);
    }

    boolean isAccepting() {
        return Type.ACCEPTING.equals(type);
    }

    boolean isError() {
        return Type.ERROR.equals(type);
    }

    MultiValuedMap<T, State<T>> getTransitions() {
        return transitions;
    }

    Collection<State<T>> getTransitionsFor(T input) {
        return transitions.get(input);
    }

    void addTransition(T input, State<T> targetState) {
        transitions.put(input, targetState);
    }

    void removeTransition(T input, State<T> targetState) {
        transitions.removeMapping(input, targetState);
    }

    void removeTransitionsFor(T input) {
        transitions.remove(input);
    }
}
