import de.alsk.compiler.regex.RangeRegex;
import de.alsk.compiler.regex.Regex;
import de.alsk.compiler.regex.RegexParser;
import org.junit.Before;
import org.junit.Test;

import static de.alsk.compiler.regex.Regexes.*;
import static org.junit.Assert.assertEquals;

public class RegexParserTest {
    private RegexParser regexParser;

    @Before
    public void setup() {
        regexParser = new RegexParser();
    }

    @Test
    public void testParseOptional() throws Exception {
        Regex expectedResult = optional(atomar('A'));
        Regex parsedResult = regexParser.parse("A?");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseInfinite() throws Exception {
        Regex expectedResult = infinite(atomar('A'));
        Regex parsedResult = regexParser.parse("A*");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseOneOrMore() throws Exception {
        Regex expectedResult = oneOrMore(atomar('A'));
        Regex parsedResult = regexParser.parse("A+");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseMultipleOr() throws Exception {
        Regex expectedResult = or(atomar('A'), atomar('B'), atomar('C'));
        Regex parsedResult = regexParser.parse("A|B|C");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseOr() throws Exception {
        Regex expectedResult = or(atomar('A'), atomar('B'));
        Regex parsedResult = regexParser.parse("A|B");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseGroup() throws Exception {
        Regex expectedResult = optional(group(atomar('A'), atomar('B'), atomar('C')));
        Regex parsedResult = regexParser.parse("(ABC)?");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseNestedGroups() throws Exception {
        Regex expectedResult = optional(group(
                atomar('A'),
                group(atomar('B'), atomar('C')),
                atomar('E')));
        Regex parsedResult = regexParser.parse("(A(BC)E)?");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseRange() throws Exception {
        Regex expectedResult = range(new RangeRegex.CharRange('A', 'Z'));
        Regex parsedResult = regexParser.parse("[A-Z]");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseMultipleRanges() throws Exception {
        Regex expectedResult = range(
                new RangeRegex.CharRange('a', 'z'),
                new RangeRegex.CharRange('A', 'Z'),
                new RangeRegex.CharRange('0', '9'));
        Regex parsedResult = regexParser.parse("[a-zA-Z0-9]");
        assertEquals(expectedResult, parsedResult);
    }

    @Test
    public void testParseComplex() throws Exception {
        Regex expectedResult = group(
                atomar('A'),
                infinite(group(
                        or(atomar('B'), optional(atomar('D'))),
                        atomar('E'))),
                atomar('A'),
                atomar('C'),
                oneOrMore(range(new RangeRegex.CharRange('0', '9'))),
                atomar('K'),
                or(
                    atomar('L'),
                    infinite(group(
                                atomar('A'),
                                atomar('D'),
                                atomar('C')))));

        Regex parsedResult  = regexParser.parse("A(B|D?E)*AC[0-9]+KL|(ADC)*");
        assertEquals(expectedResult, parsedResult);
    }
}
