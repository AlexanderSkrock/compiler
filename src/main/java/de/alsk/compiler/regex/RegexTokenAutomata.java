package de.alsk.compiler.regex;

import de.alsk.compiler.automata.AbstractNonDeterministicFiniteAutomata;
import de.alsk.compiler.automata.State;

import static de.alsk.compiler.automata.Automatas.*;

public class RegexTokenAutomata extends AbstractNonDeterministicFiniteAutomata<Regex.Type> {
    static AbstractNonDeterministicFiniteAutomata<Regex.Type> get() {
        return and(RegexTokenAutomata::new,
                getElementWithOptionalPrefixAndModifier(),
                infinite(RegexTokenAutomata::new,
                    and(RegexTokenAutomata::new,
                            atomar(RegexTokenAutomata::new, Regex.Type.OR),
                            getElementWithOptionalPrefixAndModifier()))
                );
    }

    private static AbstractNonDeterministicFiniteAutomata<Regex.Type> getElementWithOptionalPrefixAndModifier() {
        return and(RegexTokenAutomata::new,
                optional(RegexTokenAutomata::new, getPrefix()),
                getElement(),
                optional(RegexTokenAutomata::new, getModifier()));
    }

    private static AbstractNonDeterministicFiniteAutomata<Regex.Type> getElement() {
        return or(RegexTokenAutomata::new,
                atomar(RegexTokenAutomata::new, Regex.Type.RANGE),
                atomar(RegexTokenAutomata::new, Regex.Type.GROUP),
                atomar(RegexTokenAutomata::new, Regex.Type.ANY),
                atomar(RegexTokenAutomata::new, Regex.Type.ATOMAR));
    }

    private static AbstractNonDeterministicFiniteAutomata<Regex.Type> getPrefix() {
        return atomar(RegexTokenAutomata::new, Regex.Type.NOT);
    }

    private static AbstractNonDeterministicFiniteAutomata<Regex.Type> getModifier() {
        return or(RegexTokenAutomata::new,
                atomar(RegexTokenAutomata::new, Regex.Type.OPTIONAL),
                atomar(RegexTokenAutomata::new, Regex.Type.INFINITE),
                atomar(RegexTokenAutomata::new, Regex.Type.ONE_OR_MORE));
    }

    private RegexTokenAutomata(State<Regex.Type> startingState) {
        super(startingState);
    }

    @Override
    public Regex.Type getEmptyInput() {
        return null;
    }
}
