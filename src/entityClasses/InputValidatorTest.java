package entityClasses;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InputValidatorTest {

    private static final int MAX_TITLE = 200;
    private static final int MAX_BODY = 10_000;

    // -------------------------------------------------------------------------
    // Normal tests -- valid inputs, must return input unchanged
    // -------------------------------------------------------------------------

    @Test
    public void NormalTest1() {
        // Basic clean text with no special characters. Confirms the happy path works.
        try {
            String input = "This is a normal post title.";
            assertEquals(input, InputValidator.validate(input, MAX_TITLE));
        } catch (IllegalArgumentException e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    @Test
    public void NormalTest2() {
        // Input at exactly the character limit. Confirms the boundary is inclusive.
        try {
            String input = "X".repeat(MAX_TITLE);
            assertEquals(input, InputValidator.validate(input, MAX_TITLE));
        } catch (IllegalArgumentException e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    @Test
    public void NormalTest3() {
        // Digits and punctuation are valid content. Confirms the HTML regex does not
        // produce false positives on parentheses, colons, or other common characters.
        try {
            String input = "CSE360 (Spring 2026) Assignment #3: 100 points!";
            assertEquals(input, InputValidator.validate(input, MAX_TITLE));
        } catch (IllegalArgumentException e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    @Test
    public void NormalTest4() {
        // Apostrophes and double quotes are valid English punctuation. Confirms they
        // are not mistakenly flagged as HTML attribute delimiters.
        try {
            String input = "It's the professor's best lecture yet.";
            assertEquals(input, InputValidator.validate(input, MAX_TITLE));
        } catch (IllegalArgumentException e) {
            fail("*** Error*** This is a valid test case");
        }
    }

    // -------------------------------------------------------------------------
    // Robust tests -- invalid inputs, must throw IllegalArgumentException
    // -------------------------------------------------------------------------

    @Test
    public void RobustTest1() {
        // Null must be caught before any other check. Every subsequent String operation
        // would throw NullPointerException instead of the expected IllegalArgumentException.
        try {
            InputValidator.validate(null, MAX_TITLE);
            fail("*** Error*** null was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input cannot be null. Please provide actual text content.");
        }
    }

    @Test
    public void RobustTest2() {
        // Empty string is the zero-length boundary for the blank check.
        // Distinct from the whitespace-only case and must be tested separately.
        try {
            InputValidator.validate("", MAX_TITLE);
            fail("*** Error*** empty string was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input cannot be blank or consist only of whitespace. "
                + "Please provide meaningful text content.");
        }
    }

    @Test
    public void RobustTest3() {
        // Whitespace-only input is non-empty but carries no meaning. Confirms the blank
        // check uses trim() before isEmpty() so spaces are treated the same as empty.
        try {
            InputValidator.validate("   ", MAX_TITLE);
            fail("*** Error*** whitespace-only string was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input cannot be blank or consist only of whitespace. "
                + "Please provide meaningful text content.");
        }
    }

    @Test
    public void RobustTest4() {
        // One character over the title limit. Confirms the check uses strictly-greater-than
        // and catches the tightest possible boundary violation.
        try {
            InputValidator.validate("A".repeat(MAX_TITLE + 1), MAX_TITLE);
            fail("*** Error*** input exceeding title max length was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Input is too long:"));
        }
    }

    @Test
    public void RobustTest5() {
        // Repeats the length boundary test with the body limit. Confirms the check uses
        // the caller-supplied maxLength parameter and not a hard-coded value.
        try {
            InputValidator.validate("B".repeat(MAX_BODY + 1), MAX_BODY);
            fail("*** Error*** input exceeding body max length was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("Input is too long:"));
        }
    }

    @Test
    public void RobustTest6() {
        // Lowercase script tag is the most common stored-XSS payload.
        // It must be the minimum the HTML regex catches.
        try {
            InputValidator.validate("Hello <script>alert('xss')</script>", MAX_TITLE);
            fail("*** Error*** lowercase script tag was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input contains HTML or script tags, which are not permitted. "
                + "Please remove all angle-bracket markup from your text.");
        }
    }

    @Test
    public void RobustTest7() {
        // Uppercase SCRIPT tag confirms the regex is not limited to lowercase tag names.
        // HTML parsers treat tag names as case-insensitive so both are equally dangerous.
        try {
            InputValidator.validate("Hello <SCRIPT>alert('xss')</SCRIPT>", MAX_TITLE);
            fail("*** Error*** uppercase SCRIPT tag was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input contains HTML or script tags, which are not permitted. "
                + "Please remove all angle-bracket markup from your text.");
        }
    }

    @Test
    public void RobustTest8() {
        // Anchor tag with a javascript: href is a common phishing vector that bypasses
        // filters which only block script tags. Confirms any angle-bracket markup is caught.
        try {
            InputValidator.validate("Click <a href=\"javascript:void(0)\">here</a>", MAX_TITLE);
            fail("*** Error*** anchor tag was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input contains HTML or script tags, which are not permitted. "
                + "Please remove all angle-bracket markup from your text.");
        }
    }

    @Test
    public void RobustTest9() {
        // img with onerror executes JavaScript without a script tag, defeating keyword-based
        // filters. Confirms the regex matches any content inside angle brackets.
        try {
            InputValidator.validate("<img src=x onerror=alert(1)>", MAX_TITLE);
            fail("*** Error*** img onerror tag was not recognized as an error");
        } catch (IllegalArgumentException e) {
            assertEquals(e.getMessage(), "Input contains HTML or script tags, which are not permitted. "
                + "Please remove all angle-bracket markup from your text.");
        }
    }
}
