package guiStaffDeletePosts;

/*******
 * Controller helper for staff delete actions. Provides a shared delete method
 * that other staff views can call if desired.
 */
public class ControllerStaffDeletePosts {
    public ControllerStaffDeletePosts() {}

    protected static void performDelete(entityClasses.Post post, javafx.stage.Stage stage, entityClasses.User currentUser) {
        javafx.scene.control.Alert confirm = new javafx.scene.control.Alert(
            javafx.scene.control.Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete Post");
        confirm.setHeaderText("Delete this post?");
        confirm.setContentText("This action cannot be undone. The post will be marked as deleted.");
        java.util.Optional<javafx.scene.control.ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.OK) {
            try {
                applicationMain.FoundationsMain.postList.softDeletePost(post.getPostId());
            } catch (IllegalArgumentException e) {
                // Already deleted — ignore
            }
            // After deletion, return to staff all posts page
            guiStaffAllPosts.ViewStaffAllPosts.displayStaffAllPosts(stage, currentUser);
        }
    }
}
