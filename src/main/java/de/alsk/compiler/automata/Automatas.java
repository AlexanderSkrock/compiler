package de.alsk.compiler.automata;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Automatas {
    private Automatas() {
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> atomar(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, T value) {
        State<T> startingState = State.normal();
        State<T> acceptingState = State.accepting();
        startingState.addTransition(value, acceptingState);
        return supplier.apply(startingState);
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> and(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T>... automatas) {
        return and(supplier, List.of(automatas));
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> and(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, List<AbstractNonDeterministicFiniteAutomata<T>> automatas) {
        return supplier.apply(automatas.stream()
                .reduce((a, b) -> Automatas.and(supplier, a, b)).get()
                .getStartingState());
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> and(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T> a, AbstractNonDeterministicFiniteAutomata<T> b) {
        Set<State<T>> acceptingStatesOfA = queryForAcceptingStates(a.getStartingState());
        State<T> startingStateOfB = b.getStartingState();
        acceptingStatesOfA.forEach(acceptingState -> acceptingState.addTransition(a.getEmptyInput(), startingStateOfB));
        return supplier.apply(a.getStartingState());
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> or(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T>... automatas) {
        return or(supplier, List.of(automatas));
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> or(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, List<AbstractNonDeterministicFiniteAutomata<T>> automatas) {
        return supplier.apply(automatas.stream()
                .reduce((a, b) -> Automatas.or(supplier, a, b)).get()
                .getStartingState());
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> or(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T> a, AbstractNonDeterministicFiniteAutomata<T> b) {
        State<T> newStartingState = State.normal();
        newStartingState.addTransition(a.getEmptyInput(), a.getStartingState());
        newStartingState.addTransition(b.getEmptyInput(), b.getStartingState());

        State<T> newEndState = State.normal();
        queryForAcceptingStates(newStartingState).forEach(state -> state.addTransition(a.getEmptyInput(), newEndState));
        return supplier.apply(newStartingState);
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> optional(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T> automata) {
        return or(supplier, automata, atomar(supplier, automata.getEmptyInput()));
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> infinite(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T> automata) {
        return optional(supplier, oneOrMore(supplier, automata));
    }

    public static<T> AbstractNonDeterministicFiniteAutomata<T> oneOrMore(Function<State<T>, ? extends AbstractNonDeterministicFiniteAutomata<T>> supplier, AbstractNonDeterministicFiniteAutomata<T> automata) {
        State<T> startingState = automata.getStartingState();
        Set<State<T>> acceptingStates = queryForAcceptingStates(automata.getStartingState());
        acceptingStates.forEach(state -> state.addTransition(automata.getEmptyInput(), startingState));
        return supplier.apply(startingState);
    }

    private static<T> Set<State<T>> queryForAcceptingStates(State<T> startingState) {
        return queryForAcceptingStates(startingState, new HashSet<>());
    }

    private static<T> Set<State<T>> queryForAcceptingStates(State<T> startingState, Set<State<T>> visitedStates) {
        if(visitedStates.contains(startingState)) {
            return Collections.emptySet();
        }
        visitedStates.add(startingState);

        Set<State<T>> acceptingStates = new HashSet<>();
        if(startingState.isAccepting()) {
            acceptingStates.add(startingState);
        }

        startingState.getTransitions().entries().stream()
                .map(Map.Entry::getValue)
                .forEach(state -> acceptingStates.addAll(queryForAcceptingStates(state, visitedStates)));
        return acceptingStates;
    }
}
