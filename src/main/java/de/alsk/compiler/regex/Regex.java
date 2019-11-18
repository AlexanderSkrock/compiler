package de.alsk.compiler.regex;

public abstract class Regex {
    static final char GROUP_START_SYMBOL = '(';
    static final char GROUP_END_SYMBOL = ')';
    static final char RANGE_START_SYMBOL = '[';
    static final char RANGE_END_SYMBOL = ']';
    static final char RANGE_SEPARATOR_SYMBOL = '-';
    static final char INFINITE_SYMBOL = '*';
    static final char ONE_OR_MORE_SYMBOL = '+';
    static final char OPTIONAL_SYMBOL = '?';
    static final char OR_SYMBOL = '|';
    static final char ANY_SYMBOL = '.';
    static final char NOT_SYMBOL = '^';

    public enum Type {
        GROUP,
        OPTIONAL,
        INFINITE,
        ONE_OR_MORE,
        OR,
        ATOMAR,
        RANGE,
        ANY,
        NOT
    }

    private Type type;

    Regex(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    public abstract String getValue();

    @Override
    public String toString() {
        return getValue();
    }
}
