package guiStaffMyPosts;

/*******
 * Controller for staff "My Posts" page — mirrors student controller but opens
 * staff view post.
 */
public class ControllerStaffMyPosts {
    public ControllerStaffMyPosts() {}

    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStaffMyPosts.theStage, ViewStaffMyPosts.theUser);
    }

    protected static void openPost(entityClasses.Post post) {
        guiStaffViewPost.ViewStaffViewPost.currentPost = post;
        guiStaffViewPost.ViewStaffViewPost.displayStaffViewPost(
            ViewStaffMyPosts.theStage, ViewStaffMyPosts.theUser);
    }
}
