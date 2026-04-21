package entityClasses;

import java.util.regex.Pattern;

/*******
 * <p> Title: InputValidator Class </p>
 *
 * <p> Description: A utility class that provides a single static method for
 *  validating free-text input before it is stored as a post title, post body,
 *  or reply body. The validator checks for null, blank content, length limits,
 *  and HTML tags in that order. </p>
 *
 * <p> This class is not meant to be instantiated — all logic is in the static
 *  validate() method. </p>
 */
public class InputValidator {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Compiled regex pattern used to detect any HTML tags in the input string. */
    private static final Pattern HTML_TAG_PATTERN = Pattern.compile("<[^>]*>");

    // -------------------------------------------------------------------------
    // Constructor (private — not instantiable)
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: InputValidator() </p>
     *
     * <p> Description: Private constructor — this class is a utility class and
     *  should never be instantiated. </p>
     */
    private InputValidator() {
        // not used
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: String validate(String input, int maxLength) </p>
     *
     * <p> Description: Validates a user-supplied string against four rules in
     *  order: null, blank/whitespace only, exceeds maxLength, and contains HTML
     *  tags. If the input passes all four checks it is returned unchanged.
     *  Each check is ordered intentionally — null must come first because later
     *  checks would throw NullPointerException on a null value, and the length
     *  check comes before the HTML scan so oversized input is rejected cheaply
     *  before the more expensive regex runs. HTML tags are rejected entirely
     *  rather than stripped, because stripping can be bypassed with malformed
     *  nested tags. </p>
     *
     * @param input     the text to validate; may be null (will be rejected)
     * @param maxLength the maximum number of characters allowed
     * @return the original input string if all checks pass
     * @throws IllegalArgumentException if input is null, blank, too long, or
     *                                  contains HTML tags
     */
    public static String validate(String input, int maxLength) {

        // Null must be caught first — every other check calls a method on input
        // and would throw NullPointerException before we could give a useful message.
        if (input == null) {
            throw new IllegalArgumentException(
                "Input cannot be null. Please provide actual text content.");
        }

        // Trim before checking emptiness so that a string of only spaces is treated
        // the same as an empty string — both carry no meaningful content.
        if (input.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Input cannot be blank or consist only of whitespace. "
                + "Please provide meaningful text content.");
        }

        // Length is checked before the regex because String.length() is cheap.
        // No point running the HTML scan on input that will be rejected anyway.
        if (input.length() > maxLength) {
            throw new IllegalArgumentException(
                "Input is too long: " + input.length() + " characters provided, "
                + "but the maximum allowed is " + maxLength + " characters. "
                + "Please shorten your text.");
        }

        // Reject any input that contains an HTML tag. Stripping tags is not used
        // here because malformed or nested tags can survive naive stripping and
        // still be rendered as a script by the browser. Rejection is the safer choice.
        if (HTML_TAG_PATTERN.matcher(input).find()) {
            throw new IllegalArgumentException(
                "Input contains HTML or script tags, which are not permitted. "
                + "Please remove all angle-bracket markup from your text.");
        }

        // All checks passed — return the original string unchanged.
        return input;
    }
}
