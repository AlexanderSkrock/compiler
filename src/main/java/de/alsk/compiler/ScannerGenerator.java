package de.alsk.compiler;

import de.alsk.compiler.regex.Regex;
import de.alsk.compiler.regex.Regexes;
import org.apache.commons.collections4.OrderedMap;
import org.apache.commons.collections4.map.ListOrderedMap;

import java.util.Set;

public class ScannerGenerator<TokenType> {
    private OrderedMap<String, TokenType> tokenDefinitions;

    public ScannerGenerator() {
        tokenDefinitions = new ListOrderedMap<>();
    }

    public void addToken(String regexString, TokenType type) {
        tokenDefinitions.put(regexString, type);
    }

    public void removeToken(String regexString) {
        tokenDefinitions.remove(regexString);
    }

    public SimpleScanner<TokenType> generate(Set<Character> alphabet) {
        OrderedMap<Regex, TokenType> tokenAutomataMap = new ListOrderedMap<>();
        tokenDefinitions.forEach((regexString, tokenType) -> {
            try {
                Regex regex = Regexes.parse(regexString);
                tokenAutomataMap.put(regex, tokenType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return new SimpleScanner<>(tokenAutomataMap, alphabet);
    }
}
