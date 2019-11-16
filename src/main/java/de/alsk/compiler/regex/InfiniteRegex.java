package de.alsk.compiler.regex;

public class InfiniteRegex extends Regex {
    private Regex regex;

    InfiniteRegex(Regex regex) {
        super(Type.INFINITE);
        this.regex = regex;
    }

    public Regex getRegex() {
        return regex;
    }

    @Override
    public String getValue() {
        return getRegex().getValue() + INFINITE_SYMBOL;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof InfiniteRegex)) {
            return false;
        }
        return this.regex.equals(((InfiniteRegex) other).regex);
    }
}
