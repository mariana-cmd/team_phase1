package guiStaffCreateReply;

import entityClasses.Reply;

/*******
 * Controller for staff create reply page — mirrors student controller but returns
 * to the staff view post page on completion.
 */
public class ControllerStaffCreateReply {
    public ControllerStaffCreateReply() {}

    protected static void goBack() {
        guiStaffViewPost.ViewStaffViewPost.currentPost = ViewStaffCreateReply.parentPost;
        guiStaffViewPost.ViewStaffViewPost.displayStaffViewPost(
            ViewStaffCreateReply.theStage, ViewStaffCreateReply.theUser);
    }

    protected static void submitReply(String body) {
        try {
            Reply newReply = new Reply(
                ViewStaffCreateReply.parentPost.getPostId(),
                ViewStaffCreateReply.theUser.getUserName(),
                body);
            applicationMain.FoundationsMain.replyList.addReply(newReply);
            goBack();
        } catch (IllegalArgumentException e) {
            ViewStaffCreateReply.showError(e.getMessage());
        }
    }
}
