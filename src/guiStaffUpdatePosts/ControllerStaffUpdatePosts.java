package guiStaffUpdatePosts;

/*******
 * Controller for staff update posts page.
 */
public class ControllerStaffUpdatePosts {
    public ControllerStaffUpdatePosts() {}

    protected static void goBack() {
        guiStaffViewPost.ViewStaffViewPost.displayStaffViewPost(
            ViewStaffUpdatePosts.theStage, ViewStaffUpdatePosts.theUser);
    }

    protected static void submitUpdate(String postId, String newTitle, String newBody) {
        try {
            applicationMain.FoundationsMain.postList.updatePost(postId,
                newTitle == null ? null : newTitle,
                newBody == null ? null : newBody);
            goBack();
        } catch (IllegalArgumentException e) {
            ViewStaffUpdatePosts.showError(e.getMessage());
        }
    }
}
