package de.alsk.compiler.regex;

public class AtomarRegex extends Regex {
    private Character regex;

    AtomarRegex(Character regex) {
        super(Type.ATOMAR);
        this.regex = regex;
    }

    public Character getCharacter() {
        return regex;
    }

    @Override
    public String getValue() {
        return getCharacter().toString();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof AtomarRegex)) {
            return false;
        }
        return this.regex.equals(((AtomarRegex) other).regex);
    }
}
