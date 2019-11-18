import de.alsk.compiler.regex.Regex;
import de.alsk.compiler.regex.RegexMatcher;
import org.junit.Test;

import static de.alsk.compiler.regex.Regexes.*;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RegexMatcherTest {
    @Test
    public void testAtomarRegex() {
        Regex regex = atomar('A');
        RegexMatcher matcher = new RegexMatcher(regex);

        assertTrue(matcher.matches("A"));
        assertFalse(matcher.matches(""));
        assertFalse(matcher.matches("AA"));
    }

    @Test
    public void testOptionalRegex() {
        Regex regex = optional(atomar('A'));
        RegexMatcher matcher = new RegexMatcher(regex);

        assertTrue(matcher.matches(""));
        assertTrue(matcher.matches("A"));
        assertFalse(matcher.matches("AA"));
    }

    @Test
    public void testOrRegex() {
        Regex regex = or(atomar('B'), atomar('A'));
        RegexMatcher matcher = new RegexMatcher(regex);

        assertTrue(matcher.matches("A"));
        assertTrue(matcher.matches("B"));
        assertFalse(matcher.matches(""));
        assertFalse(matcher.matches("AA"));
        assertFalse(matcher.matches("AB"));
        assertFalse(matcher.matches("AB"));
    }

    @Test
    public void testOneOrMoreRegex() {
        Regex regex = oneOrMore(atomar('A'));
        RegexMatcher matcher = new RegexMatcher(regex);

        assertTrue(matcher.matches("AAA"));
        assertFalse(matcher.matches(""));
    }

    @Test
    public void testInfiniteRegex() {
        Regex regex = infinite(atomar('A'));
        RegexMatcher matcher = new RegexMatcher(regex);

        assertTrue(matcher.matches("AAA"));
        assertTrue(matcher.matches(""));
    }

    @Test
    public void testComplexRegex() {
        Regex regex = infinite(or(group(atomar('A'), atomar('B')), atomar('C')));
        RegexMatcher matcher = new RegexMatcher(regex);

        assertTrue(matcher.matches(""));
        assertTrue(matcher.matches("AB"));
        assertTrue(matcher.matches("C"));
        assertTrue(matcher.matches("ABC"));
        assertTrue(matcher.matches("CAB"));
        assertTrue(matcher.matches("CCABC"));
        assertTrue(matcher.matches("ABCCABC"));
        assertFalse(matcher.matches("ACB"));
        assertFalse(matcher.matches("BCA"));
    }
}
