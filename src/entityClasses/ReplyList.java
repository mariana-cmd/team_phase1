package entityClasses;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ReplyList Class </p>
 *
 * <p> Description: Manages the full collection of Reply objects and a dynamic subset
 *  used for search results. Provides full CRUD: add, retrieve by ID, update body by ID,
 *  delete by ID, and keyword search. Also includes a convenience method to fetch all
 *  replies belonging to a specific parent post. Both lists are encapsulated , callers
 *  receive defensive copies. </p>
 *
 * @author Gunbir Singh
 */
public class ReplyList {

    // -------------------------------------------------------------------------
    // Internal lists
    // -------------------------------------------------------------------------

    /** Every reply ever added. */
    private final List<Reply> allReplies;

    /**
     * Current search-result subset. Empty until a search is performed; cleared and
     * repopulated on each new search call.
     */
    private final List<Reply> subsetReplies;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: ReplyList() </p>
     *
     * <p> Description: Initialises both internal lists as empty ArrayLists. </p>
     */
    public ReplyList() {
        this.allReplies    = new ArrayList<>();
        this.subsetReplies = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // CRUD operations
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: void addReply(Reply reply) </p>
     *
     * <p> Description: Adds a fully constructed Reply to the master list. </p>
     *
     * @param reply the Reply to add; must not be null
     * @throws IllegalArgumentException if reply is null
     */
    public void addReply(Reply reply) {
        if (reply == null) {
            throw new IllegalArgumentException(
                "Cannot add a null Reply to the list. Please provide a valid Reply object.");
        }
        allReplies.add(reply);
    }

    /*****
     * <p> Method: Reply getReplyById(String replyId) </p>
     *
     * <p> Description: Retrieves a reply from the master list by its unique ID. </p>
     *
     * @param replyId the UUID string of the reply to find; must not be null or blank
     * @return the matching Reply
     * @throws IllegalArgumentException if replyId is null/blank or no match is found
     */
    public Reply getReplyById(String replyId) {
        if (replyId == null || replyId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Reply ID cannot be null or blank. Please provide a valid reply ID to retrieve.");
        }
        for (Reply r : allReplies) {
            if (r.getReplyId().equals(replyId)) {
                return r;
            }
        }
        throw new IllegalArgumentException(
            "No reply found with ID: \"" + replyId + "\". "
            + "The reply may not exist or the ID may be incorrect.");
    }

    /*****
     * <p> Method: void updateReply(String replyId, String newBody) </p>
     *
     * <p> Description: Replaces the body of an existing reply. Validation of the new
     *  body is handled inside Reply's setBody setter. </p>
     *
     * @param replyId the ID of the reply to update; must not be null or blank
     * @param newBody the replacement body content; must not be null, blank, or too long
     * @throws IllegalArgumentException if replyId is invalid, the reply does not exist,
     *                                  newBody is null, or newBody fails Reply validation
     */
    public void updateReply(String replyId, String newBody) {
        if (newBody == null) {
            throw new IllegalArgumentException(
                "Cannot update reply with ID: \"" + replyId + "\" — "
                + "newBody must not be null. Please provide replacement content.");
        }

        Reply reply = getReplyById(replyId); // validates replyId and existence
        reply.setBody(newBody);              // Reply.setBody validates length and blank
    }

    /*****
     * <p> Method: void deleteReply(String replyId) </p>
     *
     * <p> Description: Permanently removes a reply from both the master list and the
     *  current subset. Unlike posts, replies are fully removed rather than soft-deleted
     *  because they do not anchor further thread state. </p>
     *
     * @param replyId the ID of the reply to delete; must not be null or blank
     * @throws IllegalArgumentException if replyId is invalid or the reply does not exist
     */
    public void deleteReply(String replyId) {
        Reply reply = getReplyById(replyId); // validates replyId and existence
        allReplies.remove(reply);
        subsetReplies.remove(reply);
    }

    /*****
     * <p> Method: List&lt;Reply&gt; getRepliesForPost(String parentPostId) </p>
     *
     * <p> Description: Returns all replies that belong to the specified parent post,
     *  in the order they were added. Returns an empty list if there are no replies yet;
     *  never throws because having zero replies is a valid state. </p>
     *
     * @param parentPostId the post ID to filter by; must not be null or blank
     * @return a new List of all replies for that post (may be empty)
     * @throws IllegalArgumentException if parentPostId is null or blank
     */
    public List<Reply> getRepliesForPost(String parentPostId) {
        if (parentPostId == null || parentPostId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Parent post ID cannot be null or blank when retrieving replies for a post.");
        }
        List<Reply> result = new ArrayList<>();
        for (Reply r : allReplies) {
            if (r.getParentPostId().equals(parentPostId)) {
                result.add(r);
            }
        }
        return result;
    }

    /*****
     * <p> Method: void searchByKeyword(String keyword) </p>
     *
     * <p> Description: Searches all replies whose body contains the keyword
     *  (case-insensitive). Clears and repopulates subsetReplies with matching results.
     *  If no replies match, subsetReplies will be empty. </p>
     *
     * @param keyword the search term; must not be null or blank
     * @throws IllegalArgumentException if keyword is null or blank
     */
    public void searchByKeyword(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Search keyword cannot be null or blank. "
                + "Please provide a term to search for.");
        }

        subsetReplies.clear();
        String lowerKeyword = keyword.toLowerCase();

        for (Reply r : allReplies) {
            if (r.getBody().toLowerCase().contains(lowerKeyword)) {
                subsetReplies.add(r);
            }
        }
    }

    // -------------------------------------------------------------------------
    // List accessors
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: List&lt;Reply&gt; getAllReplies() </p>
     *
     * <p> Description: Returns a defensive copy of the full reply list. </p>
     *
     * @return a new List containing all replies
     */
    public List<Reply> getAllReplies() {
        return new ArrayList<>(allReplies);
    }

    /*****
     * <p> Method: List&lt;Reply&gt; getCurrentSubset() </p>
     *
     * <p> Description: Returns a defensive copy of the current search-result subset.
     *  Empty if no search has been performed yet, or if the last search found no
     *  matches. </p>
     *
     * @return a new List containing the current subset of replies
     */
    public List<Reply> getCurrentSubset() {
        return new ArrayList<>(subsetReplies);
    }
}
