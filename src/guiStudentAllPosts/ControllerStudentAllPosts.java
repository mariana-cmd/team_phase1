package guiStudentAllPosts;

/*******
 * <p> Title: ControllerStudentAllPosts Class. </p>
 *
 * <p> Description: Controller for the Student All Posts page. All methods are
 * protected static so they can only be called by the View or Model in this package.
 * This page shows all non-deleted posts to the student. </p>
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ControllerStudentAllPosts {

    /** Default constructor — not used directly. */
    public ControllerStudentAllPosts() {}

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Returns the user to the Student Home Page. </p>
     *
     * Student US-1: Navigate back from the all-posts view.
     */
    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStudentAllPosts.theStage, ViewStudentAllPosts.theUser);
    }

    /**********
     * <p> Method: openPost(entityClasses.Post post) </p>
     *
     * <p> Description: Sets the current post and navigates to the View Post page. </p>
     *
     * Student US-2: Open a post to read its full content.
     *
     * @param post the Post the student selected
     */
    protected static void openPost(entityClasses.Post post) {
        guiStudentViewPost.ViewStudentViewPost.currentPost = post;
        guiStudentViewPost.ViewStudentViewPost.displayStudentViewPost(
            ViewStudentAllPosts.theStage, ViewStudentAllPosts.theUser);
    }
}
