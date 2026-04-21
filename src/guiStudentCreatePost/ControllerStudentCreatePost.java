package guiStudentCreatePost;

import entityClasses.Post;

/*******
 * <p> Title: ControllerStudentCreatePost Class. </p>
 *
 * <p> Description: Controller for the Create Post page. Validates input by
 * delegating to the Post constructor and adds the new post to the shared
 * PostList on success. </p>
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ControllerStudentCreatePost {

    /** Default constructor — not used directly. */
    public ControllerStudentCreatePost() {}

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Returns the user to the Student Home Page without saving. </p>
     */
    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStudentCreatePost.theStage, ViewStudentCreatePost.theUser);
    }

    /**********
     * <p> Method: submitPost(String title, String body, String thread) </p>
     *
     * <p> Description: Attempts to create a new Post and add it to the shared
     * postList. On success, navigates back. On failure, the error message from
     * the IllegalArgumentException is displayed in the View's error label. </p>
     *
     * Student US-6: Create a new discussion post.
     *
     * @param title  the post title entered by the student
     * @param body   the post body entered by the student
     * @param thread the thread name chosen from the ComboBox
     */
    protected static void submitPost(String title, String body, String thread) {
        try {
            Post newPost = new Post(
                ViewStudentCreatePost.theUser.getUserName(), title, body, thread);
            applicationMain.FoundationsMain.postList.addPost(newPost);
            goBack();
        } catch (IllegalArgumentException e) {
            ViewStudentCreatePost.showError(e.getMessage());
        }
    }
}
