package de.alsk.compiler.regex;

public class OneOrMoreRegex extends Regex {
    private Regex regex;

    OneOrMoreRegex(Regex regex) {
        super(Type.ONE_OR_MORE);
        this.regex = regex;
    }

    public Regex getRegex() {
        return regex;
    }

    @Override
    public String getValue() {
        return getRegex().getValue() + ONE_OR_MORE_SYMBOL;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof OneOrMoreRegex)) {
            return false;
        }
        return this.regex.equals(((OneOrMoreRegex) other).regex);
    }
}
