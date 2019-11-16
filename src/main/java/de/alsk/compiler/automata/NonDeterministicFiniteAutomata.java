package de.alsk.compiler.automata;

public class NonDeterministicFiniteAutomata extends AbstractNonDeterministicFiniteAutomata<Character> {
    private static final char EMPTY_CHAR = '\uffff';

    NonDeterministicFiniteAutomata(State<Character> startingState) {
        super(startingState);
    }

    @Override
    public Character getEmptyInput() {
        return EMPTY_CHAR;
    }
}
