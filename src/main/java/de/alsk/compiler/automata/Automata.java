package de.alsk.compiler.automata;

public interface Automata<T> {
    void process(T input);
    void reset();

    boolean isInAcceptingState();
    boolean isInErrorState();
}
