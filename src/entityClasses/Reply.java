package entityClasses;

import java.time.LocalDateTime;
import java.util.UUID;

/*******
 * <p> Title: Reply Class </p>
 *
 * <p> Description: Represents a reply to a discussion post. Each reply has a unique
 *  generated ID, a reference to the parent post ID, an author username, body
 *  content, and a creation timestamp. </p>
 *
 * <p> Character limit: body max 5,000 chars. </p>
 *
 * @author Gunbir Singh
 */
public class Reply {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Maximum number of characters allowed in a reply body (~1,000 words). */
    public static final int MAX_BODY_LENGTH = 5_000;

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** Globally unique identifier for this reply (UUID string). */
    private final String replyId;

    /** The post ID of the post this reply is responding to. Cannot be null. */
    private String parentPostId;

    /** Username of the student who wrote this reply. Cannot be null. */
    private String authorUsername;

    /** The reply content. 1–5,000 characters, cannot be blank. */
    private String body;

    /** Timestamp recording when the reply was created. */
    private final LocalDateTime createdAt;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: Reply(String parentPostId, String authorUsername, String body) </p>
     *
     * <p> Description: Creates a new Reply. The reply ID and creation timestamp are
     *  assigned automatically. All supplied fields are passed through their validated
     *  setters. </p>
     *
     * @param parentPostId   the ID of the post being replied to; must not be null
     * @param authorUsername the username of the reply author; must not be null
     * @param body           the reply content; 1–5,000 non-blank characters required
     *
     * @throws IllegalArgumentException if any validation rule is violated
     */
    public Reply(String parentPostId, String authorUsername, String body) {
        this.replyId   = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();

        // Delegate to validated setters so rules are enforced in one place
        setParentPostId(parentPostId);
        setAuthorUsername(authorUsername);
        setBody(body);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: String getReplyId() </p>
     * @return the unique reply ID (UUID string)
     */
    public String getReplyId() { return replyId; }

    /*****
     * <p> Method: String getParentPostId() </p>
     * @return the ID of the post this reply belongs to
     */
    public String getParentPostId() { return parentPostId; }

    /*****
     * <p> Method: String getAuthorUsername() </p>
     * @return the username of the reply author
     */
    public String getAuthorUsername() { return authorUsername; }

    /*****
     * <p> Method: String getBody() </p>
     * @return the reply body content
     */
    public String getBody() { return body; }

    /*****
     * <p> Method: LocalDateTime getCreatedAt() </p>
     * @return the timestamp when this reply was created
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    // -------------------------------------------------------------------------
    // Setters with validation
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: void setParentPostId(String parentPostId) </p>
     *
     * <p> Description: Sets the parent post ID. Must not be null because every reply
     *  must belong to an existing post. </p>
     *
     * @param parentPostId the ID of the parent post
     * @throws IllegalArgumentException if parentPostId is null
     */
    public void setParentPostId(String parentPostId) {
        if (parentPostId == null) {
            throw new IllegalArgumentException(
                "Parent post ID cannot be null. A reply must be associated with an existing post.");
        }
        this.parentPostId = parentPostId;
    }

    /*****
     * <p> Method: void setAuthorUsername(String authorUsername) </p>
     *
     * <p> Description: Sets the author username. Must not be null. </p>
     *
     * @param authorUsername the author's username
     * @throws IllegalArgumentException if authorUsername is null
     */
    public void setAuthorUsername(String authorUsername) {
        if (authorUsername == null) {
            throw new IllegalArgumentException(
                "Author username cannot be null. Every reply must have an identifiable author.");
        }
        this.authorUsername = authorUsername;
    }

    /*****
     * <p> Method: void setBody(String body) </p>
     *
     * <p> Description: Sets the reply body. Must not be null, must not be blank,
     *  and must not exceed MAX_BODY_LENGTH characters. </p>
     *
     * @param body the reply content
     * @throws IllegalArgumentException if body is null, blank, or too long
     */
    public void setBody(String body) {
        if (body == null) {
            throw new IllegalArgumentException(
                "Reply body cannot be null. A reply must contain actual content.");
        }
        if (body.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Reply body cannot be empty or consist only of whitespace. "
                + "Please write something in your reply.");
        }
        if (body.length() > MAX_BODY_LENGTH) {
            throw new IllegalArgumentException(
                "Reply body is too long: " + body.length() + " characters provided, "
                + "but the maximum allowed is " + MAX_BODY_LENGTH + " characters. "
                + "Please shorten your reply.");
        }
        this.body = body;
    }
}
