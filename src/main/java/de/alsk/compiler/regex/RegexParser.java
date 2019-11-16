package de.alsk.compiler.regex;

import de.alsk.compiler.automata.AbstractNonDeterministicFiniteAutomata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static de.alsk.compiler.regex.Regex.RANGE_SEPARATOR_SYMBOL;
import static de.alsk.compiler.regex.Regex.Type;
import static de.alsk.compiler.regex.Regexes.*;

public class RegexParser {
    public Regex parse(String regexString) throws Exception {
        List<Regex> subRegexes = parseRegexes(regexString);
        if(subRegexes.size() == 1) {
            return subRegexes.get(0);
        } else {
            return group(subRegexes);
        }
    }

    private List<Regex> parseRegexes(String regexString) throws Exception {
        RegexScanner scanner = new RegexScanner(regexString);
        AbstractNonDeterministicFiniteAutomata<Type> automata = RegexTokenAutomata.get();

        List<Regex> subRegexes = new LinkedList<>();
        LinkedList<RegexScanner.RegexToken> pendingTokens = new LinkedList<>();
        List<RegexScanner.RegexToken> currentTokens = new ArrayList<>();
        LinkedList<RegexScanner.RegexToken> currentAcceptingTokens = new LinkedList<>();
        while(scanner.hasNext() || !pendingTokens.isEmpty()) {
            RegexScanner.RegexToken token = pendingTokens.isEmpty() ? scanner.nextToken() : pendingTokens.removeFirst();
            automata.process(token.getType());
            currentTokens.add(token);
            if(automata.isInAcceptingState()) {
                currentAcceptingTokens.addAll(currentTokens);
                currentTokens.clear();
            } else if(automata.isInErrorState()) {
                automata = RegexTokenAutomata.get();
                if(currentAcceptingTokens.isEmpty()) {
                    throw new Exception("unexpected token: " + token.getType());
                }
                subRegexes.add(parseTokenGroup(currentAcceptingTokens));
                currentAcceptingTokens.clear();
                pendingTokens.addAll(currentTokens);
                currentTokens.clear();
            }
        }

        if(!currentAcceptingTokens.isEmpty()) {
            subRegexes.add(parseTokenGroup(currentAcceptingTokens));
            currentAcceptingTokens.clear();
        }

        return subRegexes;
    }

    private Regex parseTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) throws Exception {
        if(isOrTokenGroup(tokenGroup)) {
            List<String> orStrings = new LinkedList<>();
            int lastProcessedIndex = -1;
            for (int i = 0; i < tokenGroup.size(); i++) {
                if (tokenGroup.get(i).getType() == Type.OR) {
                    StringBuilder sb = new StringBuilder();
                    for (int n = lastProcessedIndex + 1; n < i; n++) {
                        sb.append(tokenGroup.get(n).getContent());
                    }
                    orStrings.add(sb.toString());
                    lastProcessedIndex = i;
                }
            }

            if (lastProcessedIndex + 1 < tokenGroup.size()) {
                StringBuilder sb = new StringBuilder();
                for (int n = lastProcessedIndex + 1; n < tokenGroup.size(); n++) {
                    sb.append(tokenGroup.get(n).getContent());
                }
                orStrings.add(sb.toString());
            }

            List<Regex> orRegexes = new LinkedList<>();
            for (String orString : orStrings) {
                orRegexes.add(parse(orString));
            }
            return or(orRegexes);
        } else if(isInfiniteTokenGroup(tokenGroup)) {
            return infinite(parse(tokenGroup.getFirst().getContent()));
        } else if(isOneOrMoreTokenGroup(tokenGroup)) {
            return oneOrMore(parse(tokenGroup.getFirst().getContent()));
        } else if(isOptionalTokenGroup(tokenGroup)) {
            return optional(parse(tokenGroup.getFirst().getContent()));
        } else if(isGroupTokenGroup(tokenGroup)) {
            String content = tokenGroup.getFirst().getContent();
            return group(parseRegexes(content.substring(1, content.length() - 1)));
        } else if(isRangeTokenGroup(tokenGroup)) {
            String content = tokenGroup.getFirst().getContent();
            return parseRange(content.substring(1, content.length() - 1));
        } else if(isAtomarTokenGroup(tokenGroup)) {
            return atomar(tokenGroup.getFirst().getContent().charAt(0));
        } else {
            throw new Exception("cannot determine type of token group");
        }
    }

    private boolean isOrTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.stream().anyMatch(token -> token.getType() == Type.OR);
    }

    private boolean isGroupTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.size() == 1 && tokenGroup.getFirst().getType() == Type.GROUP;
    }

    private boolean isRangeTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.size() == 1 && tokenGroup.getFirst().getType() == Type.RANGE;
    }

    private boolean isInfiniteTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.size() > 1 && tokenGroup.getLast().getType() == Type.INFINITE;
    }

    private boolean isOneOrMoreTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.size() > 1 && tokenGroup.getLast().getType() == Type.ONE_OR_MORE;
    }

    private boolean isOptionalTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.size() > 1 && tokenGroup.getLast().getType() == Type.OPTIONAL;
    }

    private boolean isAtomarTokenGroup(LinkedList<RegexScanner.RegexToken> tokenGroup) {
        return tokenGroup.size() == 1 && tokenGroup.getFirst().getType() == Type.ATOMAR;
    }

    private RangeRegex parseRange(String rangeString) throws Exception {
        List<RangeRegex.CharRange> charRanges = new LinkedList<>();
        String[] rangeDefinitions = rangeString.split("(?<=\\G.{3})");
        for(String rangeDefinition : rangeDefinitions) {
            if(rangeDefinition.length() != 3 || RANGE_SEPARATOR_SYMBOL != rangeDefinition.charAt(1)) {
                throw new Exception("Every range definition has to consist of 3 character: start and beginning separated by " + RANGE_SEPARATOR_SYMBOL);
            }
            char start = rangeDefinition.charAt(0);
            char end = rangeDefinition.charAt(2);
            charRanges.add(new RangeRegex.CharRange(start, end));
        }
        return range(charRanges);
    }
}
