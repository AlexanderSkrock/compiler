package de.alsk.compiler.regex;

import java.util.List;

public class GroupRegex extends Regex {
    private List<Regex> regexes;

    GroupRegex(List<Regex> regexes) {
        super(Type.GROUP);
        this.regexes = regexes;
    }

    public List<Regex> getRegexes() {
        return regexes;
    }

    @Override
    public String getValue() {
        StringBuilder regexStringBuilder = new StringBuilder();
        regexStringBuilder.append(GROUP_START_SYMBOL);
        getRegexes().forEach(regexStringBuilder::append);
        regexStringBuilder.append(GROUP_END_SYMBOL);
        return regexStringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof GroupRegex)) {
            return false;
        }
        return this.regexes.equals(((GroupRegex) other).regexes);
    }
}
