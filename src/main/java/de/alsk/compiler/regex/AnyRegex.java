package de.alsk.compiler.regex;

public class AnyRegex extends Regex {
    AnyRegex() {
        super(Type.ANY);
    }

    @Override
    public String getValue() {
        return String.valueOf(Regex.ANY_SYMBOL);
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof AnyRegex;
    }
}
