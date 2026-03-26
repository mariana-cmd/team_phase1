package guiStudentViewPost;

/*******
 * <p> Title: ControllerStudentViewPost Class. </p>
 *
 * <p> Description: Controller for the View Post page. Handles reply creation,
 * soft-delete with confirmation, and back navigation. </p>
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ControllerStudentViewPost {

    /** Default constructor — not used directly. */
    public ControllerStudentViewPost() {}

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Returns the user to the All Posts page. </p>
     */
    protected static void goBack() {
        guiStudentAllPosts.ViewStudentAllPosts.displayStudentAllPosts(
            ViewStudentViewPost.theStage, ViewStudentViewPost.theUser);
    }

    /**********
     * <p> Method: goToReply() </p>
     *
     * <p> Description: Sets the parent post reference and navigates to the
     * Create Reply page. </p>
     *
     * Student US-4: Reply to a post.
     */
    protected static void goToReply() {
        guiStudentCreateReply.ViewStudentCreateReply.parentPost =
            ViewStudentViewPost.currentPost;
        guiStudentCreateReply.ViewStudentCreateReply.displayStudentCreateReply(
            ViewStudentViewPost.theStage, ViewStudentViewPost.theUser);
    }

    /**********
     * <p> Method: performDelete() </p>
     *
     * <p> Description: Shows a confirmation dialog before soft-deleting the
     * current post. On confirmation, calls postList.softDeletePost and navigates
     * back to All Posts. </p>
     *
     * Student US-7: Delete own post (soft-delete).
     */
    protected static void performDelete() {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Post");
        confirm.setHeaderText("Delete this post?");
        confirm.setContentText(
            "This action cannot be undone. The post will be marked as deleted.");
        java.util.Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
        if (result.isPresent() &&
                result.get() == javafx.scene.control.ButtonType.OK) {
            try {
                applicationMain.FoundationsMain.postList.softDeletePost(
                    ViewStudentViewPost.currentPost.getPostId());
            } catch (IllegalArgumentException e) {
                // Already deleted — ignore
            }
            goBack();
        }
    }
}
