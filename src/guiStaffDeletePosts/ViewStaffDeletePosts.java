package guiStaffDeletePosts;

import javafx.stage.Stage;
import entityClasses.Post;
import entityClasses.User;

/*******
 * Minimal view wrapper for the delete helper. This keeps the package an MVC trio
 * and provides a single entry point other views can call to delete a post. The
 * actual confirmation and delete work is performed by the controller.
 */
public class ViewStaffDeletePosts {
    public ViewStaffDeletePosts() {}

    public static void displayStaffDeletePost(Stage stage, User user, Post post) {
        // Delegate to controller which performs confirmation and deletion
        ControllerStaffDeletePosts.performDelete(post, stage, user);
    }
}
