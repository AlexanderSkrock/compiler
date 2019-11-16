package de.alsk.compiler.regex;

import java.util.List;

public class Regexes {
    private Regexes() {
    }

    public static AtomarRegex atomar(Character character) {
        return new AtomarRegex(character);
    }

    public static GroupRegex group(Regex... regexes) {
        return new GroupRegex(List.of(regexes));
    }

    public static GroupRegex group(List<Regex> regexes) {
        return new GroupRegex(regexes);
    }

    public static RangeRegex range(RangeRegex.CharRange... ranges) {
        return new RangeRegex(List.of(ranges));
    }

    public static RangeRegex range(List<RangeRegex.CharRange> ranges) {
        return new RangeRegex(ranges);
    }

    public static InfiniteRegex infinite(Regex regex) {
        return new InfiniteRegex(regex);
    }

    public static OneOrMoreRegex oneOrMore(Regex regex) {
        return new OneOrMoreRegex(regex);
    }

    public static OptionalRegex optional(Regex regex) {
        return new OptionalRegex(regex);
    }

    public static OrRegex or(Regex... regexes) {
        return new OrRegex(List.of(regexes));
    }

    public static OrRegex or(List<Regex> regexes) {
        return new OrRegex(regexes);
    }

    public static Regex parse(String regexString) throws Exception {
        return new RegexParser().parse(regexString);
    }
}
