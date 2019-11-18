package de.alsk.compiler.automata;

import de.alsk.compiler.regex.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class CharAutomatas {
    private static Function<State<Character>, ? extends AbstractNonDeterministicFiniteAutomata<Character>> getSupplier() {
        return NonDeterministicFiniteAutomata::new;
    }
    private CharAutomatas() {
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> fromRegexString(String regexString, Set<Character> alphabet) throws Exception {
        Regex regex = Regexes.parse(regexString);
        return fromRegex(regex, alphabet);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> fromRegex(Regex regex, Set<Character> alphabet) {
        switch (regex.getType()) {
            case ATOMAR: {
                State<Character> startingSate = State.normal();
                startingSate.addTransition(((AtomarRegex) regex).getCharacter(), State.accepting());
                return getSupplier().apply(startingSate);
            }
            case INFINITE: return infinite(fromRegex(((InfiniteRegex) regex).getRegex(), alphabet));
            case ONE_OR_MORE: return oneOrMore(fromRegex(((OneOrMoreRegex) regex).getRegex(), alphabet));
            case OPTIONAL: return optional(fromRegex(((OptionalRegex) regex).getRegex(), alphabet));
            case GROUP: {
                List<AbstractNonDeterministicFiniteAutomata<Character>> subAutomatas = new LinkedList<>();
                for(Regex subRegex : ((GroupRegex) regex).getRegexes()) {
                    subAutomatas.add(fromRegex(subRegex, alphabet));
                }
                return and(subAutomatas);
            }
            case RANGE: {
                List<AbstractNonDeterministicFiniteAutomata<Character>> subAutomatas = new LinkedList<>();
                for(RangeRegex.CharRange charRange : ((RangeRegex) regex).getRanges()) {
                    for(char c = charRange.getRangeStart(); c <= charRange.getRangeEnd(); c++) {
                        if(alphabet.contains(c)) {
                            subAutomatas.add(fromRegex(Regexes.atomar(c), alphabet));
                        }
                    }
                }
                return or(subAutomatas);
            }
            case OR: {
                List<AbstractNonDeterministicFiniteAutomata<Character>> subAutomatas = new LinkedList<>();
                for(Regex subRegex : ((OrRegex) regex).getRegexes()) {
                    subAutomatas.add(fromRegex(subRegex, alphabet));
                }
                return or(subAutomatas);
            }
            case ANY: {
                List<AbstractNonDeterministicFiniteAutomata<Character>> subAutomatas = new LinkedList<>();
                for(char c : alphabet) {
                    subAutomatas.add(atomar(c));
                }
                return or(subAutomatas);
            }
            case NOT: {
                Regex regexToNegate = ((NotRegex) regex).getNegatedRegex();
                if(regexToNegate instanceof AtomarRegex) {
                    List<AbstractNonDeterministicFiniteAutomata<Character>> subAutomatas = new LinkedList<>();
                    for(char c : alphabet) {
                        if (c != ((AtomarRegex) regexToNegate).getCharacter()) {
                            subAutomatas.add(atomar(c));
                        }
                    }
                    return or(subAutomatas);
                }
                // TODO Add implementation for negation (NFA to DFA -> Complement)
                return fromRegex(regexToNegate, alphabet);
            }
            default:
                throw new RuntimeException("unknown regex type: " + regex.getType());
        }
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> atomar(Character value) {
        return Automatas.atomar(getSupplier(), value);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> and(AbstractNonDeterministicFiniteAutomata<Character>... automatas) {
        return Automatas.and(getSupplier(), automatas);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> and(List<AbstractNonDeterministicFiniteAutomata<Character>> automatas) {
        return Automatas.and(getSupplier(), automatas);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> and(AbstractNonDeterministicFiniteAutomata<Character> a, AbstractNonDeterministicFiniteAutomata<Character> b) {
        return Automatas.and(getSupplier(), a, b);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> or(AbstractNonDeterministicFiniteAutomata<Character>... automatas) {
        return Automatas.or(getSupplier(), List.of(automatas));
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> or(List<AbstractNonDeterministicFiniteAutomata<Character>> automatas) {
        return Automatas.or(getSupplier(), automatas);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> or(AbstractNonDeterministicFiniteAutomata<Character> a, AbstractNonDeterministicFiniteAutomata<Character> b) {
        return Automatas.or(getSupplier(), a, b);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> optional(AbstractNonDeterministicFiniteAutomata<Character> automata) {
        return Automatas.optional(getSupplier(), automata);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> infinite(AbstractNonDeterministicFiniteAutomata<Character> automata) {
        return Automatas.infinite(getSupplier(), automata);
    }

    public static AbstractNonDeterministicFiniteAutomata<Character> oneOrMore(AbstractNonDeterministicFiniteAutomata<Character> automata) {
        return Automatas.oneOrMore(getSupplier(), automata);
    }
}
