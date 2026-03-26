package guiStudentCreateReply;

import entityClasses.Reply;

/*******
 * <p> Title: ControllerStudentCreateReply Class. </p>
 *
 * <p> Description: Controller for the Create Reply page. Validates input by
 * delegating to the Reply constructor and adds the new reply to the shared
 * ReplyList on success. </p>
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ControllerStudentCreateReply {

    /** Default constructor — not used directly. */
    public ControllerStudentCreateReply() {}

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Returns the user to the View Post page without saving. </p>
     */
    protected static void goBack() {
        guiStudentViewPost.ViewStudentViewPost.currentPost =
            ViewStudentCreateReply.parentPost;
        guiStudentViewPost.ViewStudentViewPost.displayStudentViewPost(
            ViewStudentCreateReply.theStage, ViewStudentCreateReply.theUser);
    }

    /**********
     * <p> Method: submitReply(String body) </p>
     *
     * <p> Description: Attempts to create a new Reply and add it to the shared
     * replyList. On success, navigates back to the View Post page. On failure,
     * the error message is shown in the View's error label. </p>
     *
     * Student US-4: Reply to a discussion post.
     *
     * @param body the reply content entered by the student
     */
    protected static void submitReply(String body) {
        try {
            Reply newReply = new Reply(
                ViewStudentCreateReply.parentPost.getPostId(),
                ViewStudentCreateReply.theUser.getUserName(),
                body);
            applicationMain.FoundationsMain.replyList.addReply(newReply);
            goBack();
        } catch (IllegalArgumentException e) {
            ViewStudentCreateReply.showError(e.getMessage());
        }
    }
}
