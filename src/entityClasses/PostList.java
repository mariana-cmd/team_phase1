package entityClasses;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: PostList Class </p>
 *
 * <p> Description: Manages the full collection of Post objects and a dynamic subset
 *  used for search results. Provides full CRUD: add, retrieve by ID, update title/body
 *  by ID, soft delete by ID, and keyword search. Both lists are encapsulated , callers
 *  receive defensive copies so they cannot mutate internal state. </p>
 *
 * @author Gunbir Singh
 */
public class PostList {

    // -------------------------------------------------------------------------
    // Internal lists
    // -------------------------------------------------------------------------

    /** Every post ever added, including soft-deleted ones. */
    private final List<Post> allPosts;

    /**
     * Current search-result subset. Empty until a search is performed; cleared and
     * repopulated on each new search call.
     */
    private final List<Post> subsetPosts;

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: PostList() </p>
     *
     * <p> Description: Initialises both internal lists as empty ArrayLists. </p>
     */
    public PostList() {
        this.allPosts    = new ArrayList<>();
        this.subsetPosts = new ArrayList<>();
    }

    // -------------------------------------------------------------------------
    // CRUD operations
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: void addPost(Post post) </p>
     *
     * <p> Description: Adds a fully constructed Post to the master list. </p>
     *
     * @param post the Post to add; must not be null
     * @throws IllegalArgumentException if post is null
     */
    public void addPost(Post post) {
        if (post == null) {
            throw new IllegalArgumentException(
                "Cannot add a null Post to the list. Please provide a valid Post object.");
        }
        allPosts.add(post);
    }

    /*****
     * <p> Method: Post getPostById(String postId) </p>
     *
     * <p> Description: Retrieves a post from the master list by its unique ID,
     *  regardless of its deleted status. </p>
     *
     * @param postId the UUID string of the post to find; must not be null or blank
     * @return the matching Post
     * @throws IllegalArgumentException if postId is null/blank or no match is found
     */
    public Post getPostById(String postId) {
        if (postId == null || postId.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Post ID cannot be null or blank. Please provide a valid post ID to retrieve.");
        }
        for (Post p : allPosts) {
            if (p.getPostId().equals(postId)) {
                return p;
            }
        }
        throw new IllegalArgumentException(
            "No post found with ID: \"" + postId + "\". "
            + "The post may not exist or the ID may be incorrect.");
    }

    /*****
     * <p> Method: void updatePost(String postId, String newTitle, String newBody) </p>
     *
     * <p> Description: Updates the title and/or body of an existing, non-deleted post.
     *  Pass null for either field to leave it unchanged. At least one of the two must
     *  be non-null. Validation of the new values is handled inside Post's setters. </p>
     *
     * @param postId   the ID of the post to update; must not be null or blank
     * @param newTitle the replacement title, or null to leave unchanged
     * @param newBody  the replacement body, or null to leave unchanged
     * @throws IllegalArgumentException if postId is invalid, the post does not exist,
     *                                  the post is deleted, both newTitle and newBody
     *                                  are null, or either value fails Post validation
     */
    public void updatePost(String postId, String newTitle, String newBody) {
        if (newTitle == null && newBody == null) {
            throw new IllegalArgumentException(
                "Cannot update post with ID: \"" + postId + "\" — "
                + "at least one of newTitle or newBody must be provided (both are null).");
        }

        Post post = getPostById(postId); // validates postId and existence

        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Cannot update post with ID: \"" + postId + "\" because it has been "
                + "soft-deleted. Restore the post before attempting to edit it.");
        }

        if (newTitle != null) {
            post.setTitle(newTitle); // Post.setTitle validates length and blank
        }
        if (newBody != null) {
            post.setBody(newBody); // Post.setBody validates length and blank
        }
    }

    /*****
     * <p> Method: void softDeletePost(String postId) </p>
     *
     * <p> Description: Marks a post as deleted without removing it from storage.
     *  The post remains in allPosts and can still be retrieved by ID, but it will be
     *  excluded from keyword search results. </p>
     *
     * @param postId the ID of the post to soft-delete; must not be null or blank
     * @throws IllegalArgumentException if postId is invalid, the post does not exist,
     *                                  or it has already been deleted
     */
    public void softDeletePost(String postId) {
        Post post = getPostById(postId); // validates postId and existence

        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Post with ID: \"" + postId + "\" has already been deleted. "
                + "No further action is needed.");
        }

        post.setDeleted(true);
    }
    /*****
     * <p> Method: void assignGradeToPost(String postId, Double grade,
     * String feedback, String graderUsername) </p>
     *
     * <p> Description: Assigns or updates a grade and private feedback for a
     * non-deleted post. Records which staff member performed the grading. </p>
     *
     * @param postId the ID of the post to grade
     * @param grade the numeric grade to assign
     * @param feedback private feedback for the author
     * @param graderUsername username of the staff member assigning grade
     *
     * @throws IllegalArgumentException if post does not exist or is deleted
     */
    public void assignGradeToPost(String postId,
                                  Double grade,
                                  String feedback,
                                  String graderUsername) {

        Post post = getPostById(postId);

        if (post.isDeleted()) {
            throw new IllegalArgumentException(
                "Cannot grade a deleted post.");
        }

        post.assignGrade(grade, feedback, graderUsername);
    }

    /*****
     * <p> Method: boolean isPostGraded(String postId) </p>
     *
     * <p> Description: Returns true if the post has already been graded. </p>
     *
     * @param postId the ID of the post
     * @return true if graded, false otherwise
     */
    public boolean isPostGraded(String postId) {
        Post post = getPostById(postId);
        return post.isGraded();
    }

    /*****
     * <p> Method: List<Post> getPostsByAuthor(String username) </p>
     *
     * <p> Description: Returns all posts created by the specified user. </p>
     *
     * @param username the username to search for
     * @return list of posts authored by that user
     */
    public List<Post> getPostsByAuthor(String username) {

        List<Post> results = new ArrayList<>();

        if (username == null) return results;

        for (Post p : allPosts) {
            if (username.equals(p.getAuthorUsername())) {
                results.add(p);
            }
        }

        return results;
    }
    /*****
     * <p> Method: void searchByKeyword(String keyword) </p>
     *
     * <p> Description: Searches all non-deleted posts whose title, body, or thread name
     *  contains the keyword (case-insensitive). Clears and repopulates subsetPosts with
     *  matching results. If no posts match, subsetPosts will be empty. </p>
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

        subsetPosts.clear();
        String lowerKeyword = keyword.toLowerCase();

        for (Post p : allPosts) {
            if (!p.isDeleted()
                    && (p.getTitle().toLowerCase().contains(lowerKeyword)
                        || p.getBody().toLowerCase().contains(lowerKeyword)
                        || p.getThreadName().toLowerCase().contains(lowerKeyword))) {
                subsetPosts.add(p);
            }
        }
    }

    // -------------------------------------------------------------------------
    // List accessors
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: List&lt;Post&gt; getAllPosts() </p>
     *
     * <p> Description: Returns a defensive copy of the full post list, including
     *  soft-deleted entries. </p>
     *
     * @return a new List containing all posts
     */
    public List<Post> getAllPosts() {
        return new ArrayList<>(allPosts);
    }

    /*****
     * <p> Method: List&lt;Post&gt; getCurrentSubset() </p>
     *
     * <p> Description: Returns a defensive copy of the current search-result subset.
     *  Empty if no search has been performed yet, or if the last search returned no
     *  matches. </p>
     *
     * @return a new List containing the current subset of posts
     */
    public List<Post> getCurrentSubset() {
        return new ArrayList<>(subsetPosts);
    }
}