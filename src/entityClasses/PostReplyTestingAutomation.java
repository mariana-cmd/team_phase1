package entityClasses;

/*******
 * <p> Title: PostReplyTestingAutomation Class. </p>
 *
 * <p> Description: A Java demonstration for automated tests for the Post, Reply,
 *  PostList, and ReplyList classes. Covers all CRUD operations with both positive
 *  (valid input, correct behavior verified) and negative (invalid input, exception
 *  expected) test cases. </p>
 *
 * <p> Copyright: Team Phase 1 © 2026 </p>
 *
 * @version 1.00 2026-02-25 Initial automated test suite
 */
public class PostReplyTestingAutomation {

    /** Default constructor — not instantiated; all methods are static. */
    public PostReplyTestingAutomation() {}

    static int numPassed = 0;   // Counter of the number of passed tests
    static int numFailed = 0;   // Counter of the number of failed tests

    /*****
     * <p> Method: main(String[] args) </p>
     *
     * <p> Description: Entry point for the automated test suite. Displays a header,
     * runs all test cases for Post, Reply, PostList, and ReplyList, then prints a
     * summary of passed and failed tests. </p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(String[] args) {

        /************** Test cases semi-automation report header **************/
        System.out.println("______________________________________");
        System.out.println("\nTesting Automation for Post, Reply, PostList, and ReplyList");

        // ===========================================================================
        // Shared setup — PostList with three posts used across Post/PostList tests
        // ===========================================================================
        PostList postList = new PostList();
        Post p1 = new Post("alice", "Java Basics",    "Java is a compiled language.",   "CS101");
        Post p2 = new Post("bob",   "Pre-Deleted",    "This post is deleted up front.", "General");
        Post p3 = new Post("carol", "Search Target",  "Find me with a keyword search.", "General");
        postList.addPost(p1);
        postList.addPost(p2);
        postList.addPost(p3);

        // Soft-delete p2 now so it is ready for TC06 (negative update-on-deleted test)
        postList.softDeletePost(p2.getPostId());

        // Shared setup — ReplyList used across Reply/ReplyList tests
        ReplyList replyList = new ReplyList();
        Reply r1 = new Reply("post-001", "alice", "Great explanation!");
        Reply r2 = new Reply("post-001", "bob",   "This reply will be hard-deleted.");
        replyList.addReply(r1);
        replyList.addReply(r2);

        // ===========================================================================
        // TC01 — Positive: create a valid Post with all fields supplied
        // ===========================================================================
        Post tc01Post = null;
        try { tc01Post = new Post("alice", "Hello World", "Body content here.", "CS101"); }
        catch (IllegalArgumentException e) { /* failure reported below */ }
        final Post capturedTC01 = tc01Post;
        performPositiveTest("TC01", "Create a valid Post with all fields supplied",
                capturedTC01 != null
                && capturedTC01.getAuthorUsername().equals("alice")
                && capturedTC01.getTitle().equals("Hello World")
                && capturedTC01.getBody().equals("Body content here.")
                && capturedTC01.getThreadName().equals("CS101")
                && !capturedTC01.isDeleted());

        // ===========================================================================
        // TC02 — Negative: create a Post with null authorUsername
        // ===========================================================================
        performNegativeTest("TC02", "Create a Post with null authorUsername",
                () -> new Post(null, "Title", "Body.", "General"));

        // ===========================================================================
        // TC03 — Positive: retrieve a post by valid ID from a PostList
        // ===========================================================================
        performPositiveTest("TC03", "Retrieve a post by valid ID from a PostList",
                postList.getPostById(p1.getPostId()).getTitle().equals("Java Basics"));

        // ===========================================================================
        // TC04 — Negative: retrieve a post using a non-existent ID
        // ===========================================================================
        performNegativeTest("TC04", "Retrieve a post using a non-existent ID",
                () -> postList.getPostById("no-such-id-xyz-999"));

        // ===========================================================================
        // TC05 — Positive: update a post's title using a valid ID
        // ===========================================================================
        postList.updatePost(p1.getPostId(), "Java Advanced", null);
        performPositiveTest("TC05", "Update a post's title using a valid ID",
                postList.getPostById(p1.getPostId()).getTitle().equals("Java Advanced"));

        // ===========================================================================
        // TC06 — Negative: attempt to update a soft-deleted post
        // ===========================================================================
        performNegativeTest("TC06", "Attempt to update a soft-deleted post",
                () -> postList.updatePost(p2.getPostId(), "New Title", null));

        // ===========================================================================
        // TC07 — Positive: soft-delete a post, verify isDeleted is true
        //        and post is excluded from search results
        // ===========================================================================
        String p3Id = p3.getPostId();
        postList.softDeletePost(p3Id);
        postList.searchByKeyword("Search Target");
        performPositiveTest("TC07", "Soft-delete a post: isDeleted is true and post is excluded from search results",
                postList.getPostById(p3Id).isDeleted()
                && postList.getCurrentSubset().isEmpty());

        // ===========================================================================
        // TC08 — Negative: soft-delete using a non-existent ID
        // ===========================================================================
        performNegativeTest("TC08", "Soft-delete using a non-existent ID",
                () -> postList.softDeletePost("ghost-id-000"));

        // ===========================================================================
        // TC09 — Positive: create a valid Reply with all fields supplied
        // ===========================================================================
        Reply tc09Reply = null;
        try { tc09Reply = new Reply("post-id-001", "bob", "Great question!"); }
        catch (IllegalArgumentException e) { /* failure reported below */ }
        final Reply capturedTC09 = tc09Reply;
        performPositiveTest("TC09", "Create a valid Reply with all fields supplied",
                capturedTC09 != null
                && capturedTC09.getParentPostId().equals("post-id-001")
                && capturedTC09.getAuthorUsername().equals("bob")
                && capturedTC09.getBody().equals("Great question!"));

        // ===========================================================================
        // TC10 — Negative: create a Reply with null body
        // ===========================================================================
        performNegativeTest("TC10", "Create a Reply with null body",
                () -> new Reply("post-id-001", "bob", null));

        // ===========================================================================
        // TC11 — Positive: retrieve a reply by valid ID from a ReplyList
        // ===========================================================================
        performPositiveTest("TC11", "Retrieve a reply by valid ID from a ReplyList",
                replyList.getReplyById(r1.getReplyId()).getBody().equals("Great explanation!"));

        // ===========================================================================
        // TC12 — Negative: retrieve a reply using a non-existent ID
        // ===========================================================================
        performNegativeTest("TC12", "Retrieve a reply using a non-existent ID",
                () -> replyList.getReplyById("no-such-reply-abc-999"));

        // ===========================================================================
        // TC13 — Positive: update a reply body using a valid ID
        // ===========================================================================
        replyList.updateReply(r1.getReplyId(), "Updated: Great explanation, now corrected.");
        performPositiveTest("TC13", "Update a reply body using a valid ID",
                replyList.getReplyById(r1.getReplyId()).getBody()
                         .equals("Updated: Great explanation, now corrected."));

        // ===========================================================================
        // TC14 — Negative: update a reply with a body exceeding 5000 characters
        // ===========================================================================
        performNegativeTest("TC14", "Update a reply with a body exceeding " + Reply.MAX_BODY_LENGTH + " characters",
                () -> replyList.updateReply(r1.getReplyId(), "X".repeat(Reply.MAX_BODY_LENGTH + 1)));

        // ===========================================================================
        // TC15 — Positive: hard-delete a reply, verify it is completely unretrievable
        // ===========================================================================
        String r2Id = r2.getReplyId();
        replyList.deleteReply(r2Id);
        boolean unretrievable = false;
        try { replyList.getReplyById(r2Id); }
        catch (IllegalArgumentException e) { unretrievable = true; }
        performPositiveTest("TC15", "Hard-delete a reply: reply is completely unretrievable after deletion",
                unretrievable && replyList.getAllReplies().size() == 1);

        // ===========================================================================
        // TC16 — Negative: hard-delete using a non-existent ID
        // ===========================================================================
        performNegativeTest("TC16", "Hard-delete using a non-existent ID",
                () -> replyList.deleteReply("ghost-reply-000"));

        // ===========================================================================
        // TC17 — Negative: create a Post with a title exceeding 200 characters
        // ===========================================================================
        performNegativeTest("TC17", "Create a Post with a title exceeding " + Post.MAX_TITLE_LENGTH + " characters",
                () -> new Post("alice", "A".repeat(Post.MAX_TITLE_LENGTH + 1), "Body.", "General"));

        // ===========================================================================
        // TC18 — Negative: create a Reply with a body exceeding 5000 characters
        // ===========================================================================
        performNegativeTest("TC18", "Create a Reply with a body exceeding " + Reply.MAX_BODY_LENGTH + " characters",
                () -> new Reply("post-id-001", "bob", "A".repeat(Reply.MAX_BODY_LENGTH + 1)));

        /************** Test cases semi-automation report footer **************/
        System.out.println("____________________________________________________________________________");
        System.out.println();
        System.out.println("Number of tests passed: " + numPassed);
        System.out.println("Number of tests failed: " + numFailed);
    }

    /*
     * Positive test helper: checks a boolean condition and reports pass/fail.
     * Used when the operation is expected to succeed and we verify the result value.
     * Mirrors the professor's performTestCase() style for the expectedPass=true branch.
     */
    private static void performPositiveTest(String testCase, String description, boolean passed) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Action: \"" + description + "\"");
        System.out.println("______________\n");

        if (passed) {
            System.out.println("***Success*** The operation produced the expected result, so this is a pass!");
            numPassed++;
        } else {
            System.out.println("***Failure*** The operation did NOT produce the expected result, so this is a failure!");
            numFailed++;
        }
    }

    /*
     * Negative test helper: runs the action inside a try/catch and expects an
     * IllegalArgumentException to be thrown. Pass if the exception fires; fail if
     * no exception is thrown. Also prints the exception message so the tester can
     * confirm it is descriptive.
     * Mirrors the professor's performTestCase() style for the expectedPass=false branch.
     */
    private static void performNegativeTest(String testCase, String description, Runnable action) {
        System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
        System.out.println("Action: \"" + description + "\"");
        System.out.println("______________\n");

        try {
            action.run();
            System.out.println("***Failure*** No IllegalArgumentException was thrown, but one was expected, so this is a failure!");
            numFailed++;
        } catch (IllegalArgumentException e) {
            System.out.println("***Success*** An IllegalArgumentException was thrown as expected, so this is a pass!\n");
            System.out.println("Error message: " + e.getMessage());
            numPassed++;
        }
    }
}
