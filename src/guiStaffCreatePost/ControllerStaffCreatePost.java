package guiStaffCreatePost;

import entityClasses.Post;

/*******
 * <p> Title: ControllerStaffCreatePost Class. </p>
 *
 * <p> Description: Controller for the Staff Create Post page. Mirrors the student
 * create post controller but navigates back to the Role1 (staff) home page.
 * </p>
 */
public class ControllerStaffCreatePost {

    /** Default constructor — not used directly. */
    public ControllerStaffCreatePost() {}

    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStaffCreatePost.theStage, ViewStaffCreatePost.theUser);
    }

    protected static void submitPost(String title, String body, String thread) {
        try {
            Post newPost = new Post(
                ViewStaffCreatePost.theUser.getUserName(), title, body, thread);
            applicationMain.FoundationsMain.postList.addPost(newPost);
            goBack();
        } catch (IllegalArgumentException e) {
            ViewStaffCreatePost.showError(e.getMessage());
        }
    }
}
