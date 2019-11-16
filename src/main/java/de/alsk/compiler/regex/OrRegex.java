package de.alsk.compiler.regex;

import java.util.List;
import java.util.stream.Collectors;

public class OrRegex extends Regex {
    private List<Regex> regexes;

    OrRegex(List<Regex> regexes) {
        super(Type.OR);
        this.regexes = regexes;
    }

    public List<Regex> getRegexes() {
        return regexes;
    }

    @Override
    public String getValue() {
        return getRegexes().stream().map(Object::toString).collect(Collectors.joining(String.valueOf(OR_SYMBOL)));
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof OrRegex)) {
            return false;
        }
        return this.regexes.equals(((OrRegex) other).regexes);
    }
}
