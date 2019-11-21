package de.alsk.compiler;

import de.alsk.compiler.automata.AbstractNonDeterministicFiniteAutomata;
import de.alsk.compiler.automata.Automata;
import de.alsk.compiler.automata.CharAutomatas;
import de.alsk.compiler.regex.Regex;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.OrderedMapIterator;
import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.*;
import java.util.function.Predicate;

public class SimpleScanner<TokenType> implements Scanner<TokenType> {
    private final OrderedMap<TokenType, AbstractNonDeterministicFiniteAutomata<Character>> automataToTokenTypeMap;

    private String input;
    private int lastProcessedIndex = -1;

    SimpleScanner(OrderedMap<TokenType, Regex> regexesToTokenType, Set<Character> alphabet) {
        automataToTokenTypeMap = new ListOrderedMap<>();
        regexesToTokenType.forEach((tokenType, regex) -> {
            AbstractNonDeterministicFiniteAutomata<Character> tokenAutomata = CharAutomatas.fromRegex(regex, alphabet);
            automataToTokenTypeMap.put(tokenType, tokenAutomata);
        });
    }

    public void setStringToScan(String input) {
        this.input = input;
    }

    @Override
    public boolean hasNext() {
        Token<TokenType> nextToken = findNext();
        return Objects.nonNull(nextToken);
    }

    @Override
    public Token<TokenType> next() {
        Token<TokenType> nextToken = findNext();
        lastProcessedIndex += nextToken.getContent().length();
        return nextToken;
    }

    private Token<TokenType> findNext() {
        automataToTokenTypeMap.forEach((tokenType, automata) -> automata.reset());

        int longestMatchLength = -1;
        LinkedList<TokenType> typeWithLongestMatch = new LinkedList<>();

        int currentIndex = lastProcessedIndex +1;
        for(int offset = 0; currentIndex + offset < input.length() && automataToTokenTypeMap.values().stream().anyMatch(Predicate.not(AbstractNonDeterministicFiniteAutomata::isInErrorState)); offset++) {
            char currentCharacter = input.charAt(currentIndex + offset);
            int currentLength = offset +1;

            OrderedMapIterator<TokenType, AbstractNonDeterministicFiniteAutomata<Character>> iterator = automataToTokenTypeMap.mapIterator();
            while(iterator.hasNext()) {
                TokenType type = iterator.next();
                AbstractNonDeterministicFiniteAutomata<Character> automata = automataToTokenTypeMap.get(type);
                if(automata.isInErrorState()) {
                    continue;
                }
                automata.process(currentCharacter);
                if(automata.isInAcceptingState()) {
                    if(currentLength != longestMatchLength) {
                        longestMatchLength = currentLength;
                        typeWithLongestMatch = new LinkedList<>();
                    }
                    typeWithLongestMatch.add(type);
                }
            }
        }

        if(longestMatchLength < 0) {
            return null;
        }
        return new Token<>(typeWithLongestMatch.getFirst(), input.substring(currentIndex, currentIndex + longestMatchLength));
    }
}
