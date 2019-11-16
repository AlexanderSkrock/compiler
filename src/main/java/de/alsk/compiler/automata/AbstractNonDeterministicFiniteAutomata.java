package de.alsk.compiler.automata;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractNonDeterministicFiniteAutomata<T> implements Automata<T> {
    private State<T> startingState;
    private Set<State<T>> currentStates;

    public AbstractNonDeterministicFiniteAutomata(State<T> startingState) {
        this.startingState = startingState;
        currentStates = new HashSet<>();
        currentStates.add(startingState);
        currentStates.addAll(calculateAllStatesReachableWithEmptyInput(currentStates));
    }

    State<T> getStartingState() {
        return startingState;
    }

    @Override
    public void process(T input) {
        Set<State<T>> startingStates = new HashSet<>(currentStates);
        startingStates.addAll(calculateAllStatesReachableWithEmptyInput(currentStates));

        Set<State<T>> targetStates = calculatePossibleStatesFor(startingStates, input);
        targetStates.addAll(calculateAllStatesReachableWithEmptyInput(targetStates));

        currentStates = targetStates;
    }

    private Set<State<T>> calculateAllStatesReachableWithEmptyInput(Set<State<T>> startingStates) {
        boolean statesWereAdded = startingStates.addAll(calculatePossibleStatesFor(startingStates, getEmptyInput()));
        if(statesWereAdded) {
            return calculateAllStatesReachableWithEmptyInput(startingStates);
        } else {
            return startingStates;
        }
    }

    private Set<State<T>> calculatePossibleStatesFor(Set<State<T>> startingStates, T input) {
        return startingStates.stream()
                .map(state -> Objects.requireNonNullElse(state.getTransitionsFor(input), new LinkedList<State<T>>()))
                .flatMap(Collection::stream)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isInAcceptingState() {
        return currentStates.stream().anyMatch(State::isAccepting);
    }

    @Override
    public boolean isInErrorState() {
        return currentStates.isEmpty() || currentStates.stream().allMatch(State::isError);
    }

    public abstract T getEmptyInput();
}
