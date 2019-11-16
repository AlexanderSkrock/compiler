package de.alsk.compiler.regex;

public class OptionalRegex extends Regex {
    private Regex regex;

    OptionalRegex(Regex regex) {
        super(Type.OPTIONAL);
        this.regex = regex;
    }

    public Regex getRegex() {
        return regex;
    }

    @Override
    public String getValue() {
        return getRegex().getValue() + OPTIONAL_SYMBOL;
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof OptionalRegex)) {
            return false;
        }
        return this.regex.equals(((OptionalRegex) other).regex);
    }
}
