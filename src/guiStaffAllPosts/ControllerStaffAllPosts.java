package guiStaffAllPosts;

/*******
 * Controller for Staff All Posts page.
 */
public class ControllerStaffAllPosts {
    public ControllerStaffAllPosts() {}

    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStaffAllPosts.theStage, ViewStaffAllPosts.theUser);
    }

    protected static void openPost(entityClasses.Post post) {
        guiStaffViewPost.ViewStaffViewPost.currentPost = post;
        guiStaffViewPost.ViewStaffViewPost.displayStaffViewPost(
            ViewStaffAllPosts.theStage, ViewStaffAllPosts.theUser);
    }
}
