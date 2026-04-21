package guiStudentMyPosts;

/*******
 * <p> Title: ControllerStudentMyPosts Class. </p>
 *
 * <p> Description: Controller for the My Posts page. </p>
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ControllerStudentMyPosts {

    /** Default constructor — not used directly. */
    public ControllerStudentMyPosts() {}

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Returns the user to the Student Home Page. </p>
     */
    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStudentMyPosts.theStage, ViewStudentMyPosts.theUser);
    }

    /**********
     * <p> Method: openPost(entityClasses.Post post) </p>
     *
     * <p> Description: Navigates to the View Post page for the selected post. </p>
     *
     * Student US-3: View own posts.
     *
     * @param post the post to open
     */
    protected static void openPost(entityClasses.Post post) {
        guiStudentViewPost.ViewStudentViewPost.currentPost = post;
        guiStudentViewPost.ViewStudentViewPost.displayStudentViewPost(
            ViewStudentMyPosts.theStage, ViewStudentMyPosts.theUser);
    }
}
