package de.alsk.compiler.regex;

import java.util.List;
import java.util.Objects;

import static de.alsk.compiler.regex.Regex.*;

class RegexScanner {
    private final String regexString;
    private int lastProcessedIndex = -1;

    RegexScanner(String regexString) {
        this.regexString = Objects.requireNonNull(regexString);
    }

    RegexToken nextToken() throws Exception {
        int startIndex = lastProcessedIndex + 1;
        char startingChar = regexString.charAt(startIndex);

        if(List.of(GROUP_END_SYMBOL, RANGE_END_SYMBOL).contains(startingChar)) {
            throw new Exception(String.format("Unexpected input char %s at position %d", regexString.charAt(startIndex), startIndex));
        }

        Type tokenType = getTokenTypeForStartingChar(startingChar);
        if(tokenType == Type.GROUP) {
            int matchingEndSymbolIndex = scanFromMatchingClosure(GROUP_START_SYMBOL, GROUP_END_SYMBOL, regexString, startIndex);
            if(matchingEndSymbolIndex < 0) {
                throw new Exception(String.format("Group started at position %d was never closed", startIndex));
            }
            lastProcessedIndex = matchingEndSymbolIndex;
            return new RegexToken(tokenType, regexString.substring(startIndex, matchingEndSymbolIndex + 1));
        } else if(tokenType == Type.RANGE) {
            int matchingEndSymbolIndex = scanFromMatchingClosure(RANGE_START_SYMBOL, RANGE_END_SYMBOL, regexString, startIndex);
            if(matchingEndSymbolIndex < 0) {
                throw new Exception(String.format("Range started at position %d was never closed", startIndex));
            }
            lastProcessedIndex = matchingEndSymbolIndex;
            return new RegexToken(tokenType, regexString.substring(startIndex , matchingEndSymbolIndex + 1));
        } else {
            lastProcessedIndex++;
            return new RegexToken(tokenType, regexString.substring(startIndex, startIndex + 1));
        }
    }

    boolean hasNext() {
        return lastProcessedIndex + 1 < regexString.length();
    }

    private int scanFromMatchingClosure(char start, char end, String string, int startingIndex) {
        int openedGroups = 1;
        for(int i = startingIndex + 1; i < string.length(); i++) {
            if(string.charAt(i) == start) openedGroups++;
            if(string.charAt(i) == end) openedGroups--;
            if(openedGroups == 0) {
                return i;
            }
        }
        return -1;
    }

    private Type getTokenTypeForStartingChar(char startingChar) {
        switch (startingChar) {
            case INFINITE_SYMBOL: return Type.INFINITE;
            case NOT_SYMBOL: return Type.NOT;
            case ANY_SYMBOL: return Type.ANY;
            case ONE_OR_MORE_SYMBOL: return Type.ONE_OR_MORE;
            case OPTIONAL_SYMBOL: return Type.OPTIONAL;
            case OR_SYMBOL: return Type.OR;
            case GROUP_START_SYMBOL: return Type.GROUP;
            case RANGE_START_SYMBOL: return Type.RANGE;
            default: return Type.ATOMAR;
        }
    }

    public static class RegexToken {
        private final Type type;
        private final String content;

        RegexToken(Type type, String content) {
            this.type = type;
            this.content = content;
        }

        public Type getType() {
            return type;
        }

        String getContent() {
            return content;
        }
    }
}
