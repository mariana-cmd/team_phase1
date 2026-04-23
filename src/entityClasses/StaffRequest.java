package entityClasses;

import java.time.LocalDateTime;
import java.util.UUID;

/*******
 * <p> Title: StaffRequest Class </p>
 *
 * <p> Description: Represents a request submitted by a Role2 (Instructor/Staff) user
 * to an admin for an admin-specific action. Requests start OPEN and can be CLOSED
 * by an admin (with an action description). A closed request can be reopened by its
 * owner, which creates a new linked request. </p>
 */
public class StaffRequest {

    // -------------------------------------------------------------------------
    // Constants
    // -------------------------------------------------------------------------

    /** Maximum number of characters allowed in a request description. */
    public static final int MAX_DESCRIPTION_LENGTH = 5_000;

    /** Maximum number of characters allowed in an admin action description. */
    public static final int MAX_ACTION_DESCRIPTION_LENGTH = 5_000;

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** Globally unique identifier for this request (UUID string). */
    private final String requestId;

    /** Username of the Role2 user who created the request. */
    private final String createdByUsername;

    /** Description of what the Role2 user needs the admin to do. */
    private String description;

    /** Current status: "OPEN" or "CLOSED". */
    private String status;

    /** Admin's description of the action taken when closing. Null if still open. */
    private String adminActionDescription;

    /** Username of the admin who closed this request. Null if still open. */
    private String closedByUsername;

    /** Timestamp when the request was created. */
    private final LocalDateTime createdAt;

    /** Timestamp when the request was closed. Null if still open. */
    private LocalDateTime closedAt;

    /**
     * ID of the original request this was reopened from. Null unless this request
     * was created by reopening a closed request.
     */
    private String linkedRequestId;

    // -------------------------------------------------------------------------
    // Public constructor (for new requests)
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: StaffRequest(String createdByUsername, String description) </p>
     *
     * <p> Description: Creates a new OPEN staff request. The request ID and creation
     * timestamp are generated automatically. </p>
     *
     * @param createdByUsername the Role2 username submitting the request; must not be null
     * @param description       what the user needs the admin to do; 1–5,000 chars
     * @throws IllegalArgumentException if any validation rule is violated
     */
    public StaffRequest(String createdByUsername, String description) {
        if (createdByUsername == null || createdByUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Created-by username must not be blank.");
        }
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("Description must not be blank.");
        }
        if (description.length() > MAX_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                "Description must not exceed " + MAX_DESCRIPTION_LENGTH + " characters.");
        }

        this.requestId          = UUID.randomUUID().toString();
        this.createdAt          = LocalDateTime.now();
        this.createdByUsername  = createdByUsername;
        this.description        = description;
        this.status             = "OPEN";
        this.adminActionDescription = null;
        this.closedByUsername   = null;
        this.closedAt           = null;
        this.linkedRequestId    = null;
    }

    // -------------------------------------------------------------------------
    // Package-private reconstruction constructor (for DB loading)
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: StaffRequest(all fields) </p>
     *
     * <p> Description: Reconstructs a StaffRequest from persisted data. Skips
     * UUID generation and timestamp assignment — all values are provided
     * explicitly. Intended for use by Database.loadAllStaffRequests() only. </p>
     */
    public StaffRequest(String requestId, String createdByUsername, String description,
                 String status, String adminActionDescription, String closedByUsername,
                 LocalDateTime createdAt, LocalDateTime closedAt, String linkedRequestId) {
        this.requestId              = requestId;
        this.createdByUsername      = createdByUsername;
        this.description            = description;
        this.status                 = status;
        this.adminActionDescription = adminActionDescription;
        this.closedByUsername       = closedByUsername;
        this.createdAt              = createdAt;
        this.closedAt               = closedAt;
        this.linkedRequestId        = linkedRequestId;
    }

    // -------------------------------------------------------------------------
    // Business methods
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: close(String adminUsername, String actionDescription) </p>
     *
     * <p> Description: Marks this request as CLOSED, recording the admin and
     * action description. Throws if the request is already CLOSED. </p>
     *
     * @param adminUsername     the username of the admin closing this request
     * @param actionDescription description of the action taken; 1–5,000 chars
     * @throws IllegalStateException    if this request is already CLOSED
     * @throws IllegalArgumentException if actionDescription is blank or too long
     */
    public void close(String adminUsername, String actionDescription) {
        if ("CLOSED".equals(this.status)) {
            throw new IllegalStateException("Request is already CLOSED.");
        }
        if (adminUsername == null || adminUsername.trim().isEmpty()) {
            throw new IllegalArgumentException("Admin username must not be blank.");
        }
        if (actionDescription == null || actionDescription.trim().isEmpty()) {
            throw new IllegalArgumentException("Action description must not be blank.");
        }
        if (actionDescription.length() > MAX_ACTION_DESCRIPTION_LENGTH) {
            throw new IllegalArgumentException(
                "Action description must not exceed " + MAX_ACTION_DESCRIPTION_LENGTH + " characters.");
        }

        this.status                 = "CLOSED";
        this.closedByUsername       = adminUsername;
        this.adminActionDescription = actionDescription;
        this.closedAt               = LocalDateTime.now();
    }

    /*****
     * <p> Method: setLinkedRequestId(String id) </p>
     *
     * <p> Description: Records the ID of the original request that this request
     * was reopened from. Called when creating a "reopen" request. </p>
     *
     * @param id the requestId of the original request
     */
    public void setLinkedRequestId(String id) {
        this.linkedRequestId = id;
    }

    // -------------------------------------------------------------------------
    // Getters
    // -------------------------------------------------------------------------

    /** @return the unique request identifier */
    public String getRequestId()              { return requestId; }

    /** @return the username of the user who created this request */
    public String getCreatedByUsername()      { return createdByUsername; }

    /** @return the request description */
    public String getDescription()            { return description; }

    /** @return "OPEN" or "CLOSED" */
    public String getStatus()                 { return status; }

    /** @return the admin's action description, or null if not yet closed */
    public String getAdminActionDescription() { return adminActionDescription; }

    /** @return the username of the admin who closed this, or null if open */
    public String getClosedByUsername()       { return closedByUsername; }

    /** @return the timestamp when this request was created */
    public LocalDateTime getCreatedAt()       { return createdAt; }

    /** @return the timestamp when this request was closed, or null if open */
    public LocalDateTime getClosedAt()        { return closedAt; }

    /** @return the requestId of the original request, or null if not a reopen */
    public String getLinkedRequestId()        { return linkedRequestId; }

    // -------------------------------------------------------------------------
    // Convenience helpers
    // -------------------------------------------------------------------------

    /** @return true if status is "OPEN" */
    public boolean isOpen()   { return "OPEN".equals(status); }

    /** @return true if status is "CLOSED" */
    public boolean isClosed() { return "CLOSED".equals(status); }
}
