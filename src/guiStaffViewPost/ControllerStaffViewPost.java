package guiStaffViewPost;

/*******
 * Controller for Staff View Post page. Allows reply, update (author only), and
 * delete (staff or author) operations.
 */
public class ControllerStaffViewPost {
    public ControllerStaffViewPost() {}

    protected static void goBack() {
        guiStaffAllPosts.ViewStaffAllPosts.displayStaffAllPosts(
            ViewStaffViewPost.theStage, ViewStaffViewPost.theUser);
    }

    protected static void goToReply() {
        guiStaffCreateReply.ViewStaffCreateReply.parentPost = ViewStaffViewPost.currentPost;
        guiStaffCreateReply.ViewStaffCreateReply.displayStaffCreateReply(
            ViewStaffViewPost.theStage, ViewStaffViewPost.theUser);
    }

    protected static void goToUpdate() {
        guiStaffUpdatePosts.ViewStaffUpdatePosts.currentPost = ViewStaffViewPost.currentPost;
        guiStaffUpdatePosts.ViewStaffUpdatePosts.displayStaffUpdatePost(
            ViewStaffViewPost.theStage, ViewStaffViewPost.theUser);
    }

    protected static void performDelete() {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Post");
        confirm.setHeaderText("Delete this post?");
        confirm.setContentText("This action cannot be undone. The post will be marked as deleted.");
        java.util.Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            try {
                applicationMain.FoundationsMain.postList.softDeletePost(
                    ViewStaffViewPost.currentPost.getPostId());
            } catch (IllegalArgumentException e) {
                // Already deleted — ignore
            }
            goBack();
        }
    }
}
