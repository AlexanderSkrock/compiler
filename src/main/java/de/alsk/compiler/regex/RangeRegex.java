package de.alsk.compiler.regex;

import java.util.List;

public class RangeRegex extends Regex {
    private List<CharRange> ranges;

    RangeRegex(List<CharRange> ranges) {
        super(Type.RANGE);
        this.ranges = ranges;
    }

    public List<CharRange> getRanges() {
        return ranges;
    }

    @Override
    public String getValue() {
        StringBuilder regexStringBuilder = new StringBuilder();
        regexStringBuilder.append(RANGE_START_SYMBOL);
        getRanges().forEach(regexStringBuilder::append);
        regexStringBuilder.append(RANGE_END_SYMBOL);
        return regexStringBuilder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if(!(other instanceof RangeRegex)) {
            return false;
        }
        return this.ranges.equals(((RangeRegex) other).ranges);
    }

    public static class CharRange {
        private char rangeStart;
        private char rangeEnd;

        public CharRange(char rangeStart, char rangeEnd) {
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }

        public char getRangeStart() {
            return rangeStart;
        }

        public char getRangeEnd() {
            return rangeEnd;
        }

        @Override
        public boolean equals(Object other) {
            if(!(other instanceof CharRange)) {
                return false;
            }
            return this.rangeStart == ((CharRange) other).rangeStart
                    && this.rangeEnd == ((CharRange) other).rangeEnd;
        }

        @Override
        public String toString() {
            return new StringBuilder().append(String.valueOf(rangeStart)).append(RANGE_SEPARATOR_SYMBOL).append(String.valueOf(rangeEnd)).toString();
        }
    }
}
