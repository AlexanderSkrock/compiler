package de.alsk.compiler.regex;

import de.alsk.compiler.automata.AbstractNonDeterministicFiniteAutomata;
import de.alsk.compiler.automata.CharAutomatas;

import java.util.HashSet;
import java.util.Set;

public class RegexMatcher {
    private static final Set<Character> COMPLETE_ALPHABET;
    static {
        COMPLETE_ALPHABET = new HashSet<>();
        for(char c = 0; c < Short.MAX_VALUE; c++) {
            COMPLETE_ALPHABET.add(c);
        }
    }

    private AbstractNonDeterministicFiniteAutomata<Character> automata;

    public RegexMatcher(Regex regex) {
        this(regex, COMPLETE_ALPHABET);
    }

    public RegexMatcher(Regex regex, Set<Character> alphabet) {
        this.automata = CharAutomatas.fromRegex(regex, alphabet);
    }

    public boolean matches(String input) {
        automata.reset();
        for(char c : input.toCharArray()) {
            automata.process(c);
        }
        return automata.isInAcceptingState();
    }
}
