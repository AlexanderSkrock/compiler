package de.alsk.compiler.automata;

public interface Automata<T> {
    void process(T input);

    boolean isInAcceptingState();
    boolean isInErrorState();
}
