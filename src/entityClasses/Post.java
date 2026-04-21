package entityClasses;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/*******
 * <p> Title: Post Class </p>
 *
 * <p> Description: Represents a discussion post made by a student. Each post has a unique
 *  auto-generated ID, an author, a title, a body, an optional thread name (defaults to
 *  "General"), a creation timestamp, and a soft delete flag. </p>
 *
 * <p> Character limits: title max 200 chars, body max 10,000 chars. </p>
 */
public class Post {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Maximum number of characters allowed in a post title. */
    public static final int MAX_TITLE_LENGTH = 200;

    /** Maximum number of characters allowed in a post body (~2,000 words). */
    public static final int MAX_BODY_LENGTH = 10_000;

    /** Default thread name used when none is supplied. */
    public static final String DEFAULT_THREAD = "General";

    /** Minimum allowed grade. */
    public static final double MIN_GRADE = 0.0;

    /** Maximum allowed grade. */
    public static final double MAX_GRADE = 100.0;

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** Globally unique identifier for this post (UUID string). */
    private final String postId;

    /** Username of the student who created the post. Cannot be null. */
    private String authorUsername;

    /** Short descriptive title. 1–200 characters, cannot be blank. */
    private String title;

    /** Full content of the post. 1–10,000 characters, cannot be blank. */
    private String body;

    /** Discussion thread this post belongs to. Defaults to "General". */
    private String threadName;

    /** Timestamp recording when the post was first created. */
    private final LocalDateTime createdAt;

    /** Soft-delete flag. True means deleted; the post is retained in storage. */
    private boolean isDeleted;

    /** Tracks which users have read this post (by username). */
    private Set<String> readByUsers = new HashSet<>();

    /** Numeric mark assigned by staff. Null means not yet graded. */
    private Double grade;

    /** Private feedback written by staff for the post author. */
    private String staffFeedback;

    /** Username of the staff member who assigned the grade/feedback. */
    private String gradedBy;

    /** Timestamp recording when this post was graded. Null means not yet graded. */
    private LocalDateTime gradedAt;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: Post(String authorUsername, String title, String body, String threadName) </p>
     *
     * <p> Description: Creates a new Post. The post ID and creation timestamp are assigned
     *  automatically. All supplied fields are passed through their validated setters. </p>
     *
     * @param authorUsername the username of the post author; must not be null
     * @param title          the post title; 1–200 non-blank characters required
     * @param body           the post body; 1–10,000 non-blank characters required
     * @param threadName     the thread name; null or blank defaults to "General"
     *
     * @throws IllegalArgumentException if any validation rule is violated
     */
    public Post(String authorUsername, String title, String body, String threadName) {
        this.postId    = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
        this.isDeleted = false;
        this.grade = null;
        this.staffFeedback = null;
        this.gradedBy = null;
        this.gradedAt = null;

        // Delegate to validated setters so rules are enforced in one place
        setAuthorUsername(authorUsername);
        setTitle(title);
        setBody(body);
        setThreadName(threadName);
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: String getPostId() </p>
     * @return the unique post ID (UUID string)
     */
    public String getPostId() { return postId; }

    /*****
     * <p> Method: String getAuthorUsername() </p>
     * @return the username of the post author
     */
    public String getAuthorUsername() { return authorUsername; }

    /*****
     * <p> Method: String getTitle() </p>
     * @return the post title
     */
    public String getTitle() { return title; }

    /*****
     * <p> Method: String getBody() </p>
     * @return the post body content
     */
    public String getBody() { return body; }

    /*****
     * <p> Method: String getThreadName() </p>
     * @return the thread this post belongs to
     */
    public String getThreadName() { return threadName; }

    /*****
     * <p> Method: LocalDateTime getCreatedAt() </p>
     * @return the timestamp when this post was created
     */
    public LocalDateTime getCreatedAt() { return createdAt; }

    /*****
     * <p> Method: boolean isDeleted() </p>
     * @return true if the post has been soft-deleted, false otherwise
     */
    public boolean isDeleted() { return isDeleted; }

    /*****
     * <p> Method: Double getGrade() </p>
     * @return the numeric grade for this post, or null if not yet graded
     */
    public Double getGrade() { return grade; }

    /*****
     * <p> Method: String getStaffFeedback() </p>
     * @return the private staff feedback for this post, or null if none exists
     */
    public String getStaffFeedback() { return staffFeedback; }

    /*****
     * <p> Method: String getGradedBy() </p>
     * @return the username of the staff member who graded this post, or null if not yet graded
     */
    public String getGradedBy() { return gradedBy; }

    /*****
     * <p> Method: LocalDateTime getGradedAt() </p>
     * @return the timestamp when the post was graded, or null if not yet graded
     */
    public LocalDateTime getGradedAt() { return gradedAt; }

    /*****
     * <p> Method: boolean isGraded() </p>
     * @return true if the post has been graded, false otherwise
     */
    public boolean isGraded() { return grade != null; }

    // -------------------------------------------------------------------------
    // Setters with validation
    // -------------------------------------------------------------------------

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
                "Author username cannot be null. Every post must have an identifiable author.");
        }
        this.authorUsername = authorUsername;
    }

    /*****
     * <p> Method: void setTitle(String title) </p>
     *
     * <p> Description: Sets the post title. Must not be null, must not be blank,
     *  and must not exceed MAX_TITLE_LENGTH characters. </p>
     *
     * @param title the post title
     * @throws IllegalArgumentException if title is null, blank, or too long
     */
    public void setTitle(String title) {
        if (title == null) {
            throw new IllegalArgumentException(
                "Post title cannot be null. Please provide a descriptive title for your post.");
        }
        if (title.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Post title cannot be empty or consist only of whitespace. "
                + "Please provide a meaningful title.");
        }
        if (title.length() > MAX_TITLE_LENGTH) {
            throw new IllegalArgumentException(
                "Post title is too long: " + title.length() + " characters provided, "
                + "but the maximum allowed is " + MAX_TITLE_LENGTH + " characters. "
                + "Please shorten your title.");
        }
        this.title = title;
    }

    /*****
     * <p> Method: void setBody(String body) </p>
     *
     * <p> Description: Sets the post body. Must not be null, must not be blank,
     *  and must not exceed MAX_BODY_LENGTH characters. </p>
     *
     * @param body the post body content
     * @throws IllegalArgumentException if body is null, blank, or too long
     */
    public void setBody(String body) {
        if (body == null) {
            throw new IllegalArgumentException(
                "Post body cannot be null. A post must contain actual content.");
        }
        if (body.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Post body cannot be empty or consist only of whitespace. "
                + "Please write something in your post.");
        }
        if (body.length() > MAX_BODY_LENGTH) {
            throw new IllegalArgumentException(
                "Post body is too long: " + body.length() + " characters provided, "
                + "but the maximum allowed is " + MAX_BODY_LENGTH + " characters. "
                + "Please shorten your post content.");
        }
        this.body = body;
    }

    /*****
     * <p> Method: void setThreadName(String threadName) </p>
     *
     * <p> Description: Sets the thread name. If null or blank, defaults to "General". </p>
     *
     * @param threadName the thread name; may be null or blank
     */
    public void setThreadName(String threadName) {
        if (threadName == null || threadName.trim().isEmpty()) {
            this.threadName = DEFAULT_THREAD;
        } else {
            this.threadName = threadName;
        }
    }

    /*****
     * <p> Method: void setDeleted(boolean deleted) </p>
     *
     * <p> Description: Sets the soft-delete flag for this post. </p>
     *
     * @param deleted true to mark the post as deleted, false to restore it
     */
    public void setDeleted(boolean deleted) {
        this.isDeleted = deleted;
    }

    /*****
     * <p> Method: void setGrade(Double grade) </p>
     *
     * <p> Description: Sets the numeric grade for this post. A null value clears the grade.
     *  Non-null grades must be between MIN_GRADE and MAX_GRADE inclusive. </p>
     *
     * @param grade the numeric grade, or null to clear it
     * @throws IllegalArgumentException if the grade is outside the valid range
     */
    public void setGrade(Double grade) {
        if (grade == null) {
            this.grade = null;
            return;
        }
        if (grade < MIN_GRADE || grade > MAX_GRADE) {
            throw new IllegalArgumentException(
                "Grade must be between " + MIN_GRADE + " and " + MAX_GRADE + ".");
        }
        this.grade = grade;
    }

    /*****
     * <p> Method: void setStaffFeedback(String staffFeedback) </p>
     *
     * <p> Description: Sets the private feedback written by staff for this post.
     *  A null or blank value clears the feedback. </p>
     *
     * @param staffFeedback the feedback text to store
     */
    public void setStaffFeedback(String staffFeedback) {
        if (staffFeedback == null || staffFeedback.trim().isEmpty()) {
            this.staffFeedback = null;
        } else {
            this.staffFeedback = staffFeedback.trim();
        }
    }

    /*****
     * <p> Method: void setGradedBy(String gradedBy) </p>
     *
     * <p> Description: Sets the username of the staff member who graded this post.
     *  A null or blank value clears the grader. </p>
     *
     * @param gradedBy the staff username
     */
    public void setGradedBy(String gradedBy) {
        if (gradedBy == null || gradedBy.trim().isEmpty()) {
            this.gradedBy = null;
        } else {
            this.gradedBy = gradedBy.trim();
        }
    }

    /*****
     * <p> Method: void setGradedAt(LocalDateTime gradedAt) </p>
     *
     * <p> Description: Sets the grading timestamp for this post. </p>
     *
     * @param gradedAt the grading time, or null if no grading timestamp should exist
     */
    public void setGradedAt(LocalDateTime gradedAt) {
        this.gradedAt = gradedAt;
    }

    /*****
     * <p> Method: void assignGrade(Double grade, String feedback, String graderUsername) </p>
     *
     * <p> Description: Assigns or updates the grade and private staff feedback for this post
     *  and records who performed the grading and when it occurred. </p>
     *
     * @param grade the numeric grade to assign
     * @param feedback the private feedback to assign
     * @param graderUsername the username of the staff member assigning the grade
     */
    public void assignGrade(Double grade, String feedback, String graderUsername) {
        setGrade(grade);
        setStaffFeedback(feedback);
        setGradedBy(graderUsername);
        setGradedAt(LocalDateTime.now());
    }

    /*****
     * <p> Method: void markAsRead(String username) </p>
     *
     * <p> Description: Records that the given user has read this post. </p>
     *
     * @param username the username to mark as having read this post
     */
    public void markAsRead(String username) {
        if (username != null) {
            readByUsers.add(username);
        }
    }

    /*****
     * <p> Method: boolean isReadBy(String username) </p>
     *
     * <p> Description: Returns true if the given user has read this post. </p>
     *
     * @param username the username to check
     * @return true if the user has read this post, false otherwise
     */
    public boolean isReadBy(String username) {
        if (username == null) return false;
        return readByUsers.contains(username);
    }
}
