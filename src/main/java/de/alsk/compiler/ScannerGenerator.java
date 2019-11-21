package de.alsk.compiler;

import de.alsk.compiler.regex.Regex;
import de.alsk.compiler.regex.Regexes;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.Set;

public class ScannerGenerator<TokenType> {
    private OrderedMap<TokenType, String> tokenDefinitions;

    public ScannerGenerator() {
        tokenDefinitions = new ListOrderedMap<>();
    }

    public void addToken(TokenType type, String regexString) {
        tokenDefinitions.put(type, regexString);
    }

    public void removeToken(TokenType type) {
        tokenDefinitions.remove(type);
    }

    public SimpleScanner<TokenType> generate(Set<Character> alphabet) {
        OrderedMap<TokenType, Regex> tokenAutomataMap = new ListOrderedMap<>();
        tokenDefinitions.forEach((tokenType, regexString) -> {
            try {
                Regex regex = Regexes.parse(regexString);
                tokenAutomataMap.put(tokenType, regex);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new SimpleScanner<>(tokenAutomataMap, alphabet);
    }
}
