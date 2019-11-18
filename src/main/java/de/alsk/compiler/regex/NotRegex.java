package de.alsk.compiler.regex;

public class NotRegex extends Regex {
    private Regex negatedRegex;

    NotRegex(Regex negatedRegex) {
        super(Type.NOT);
        this.negatedRegex = negatedRegex;
    }

    public Regex getNegatedRegex() {
        return negatedRegex;
    }

    @Override
    public String getValue() {
        return Regex.NOT_SYMBOL + negatedRegex.getValue();
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof NotRegex;
    }
}
