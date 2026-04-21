package guiRole2;

import entityClasses.Post;
import java.util.List;
/*******
 * <p> Title: ControllerRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role 2 Home Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page is a stub for establish future roles for the application.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 * @version 1.01		2025-09-16 Update Javadoc documentation *  
 */

public class ControllerRole2Home {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerRole2Home() {
	}

	/**********
	 * <p> Method: performUpdate() </p>
	 * 
	 * <p> Description: This method directs the user to the User Update Page so the user can change
	 * the user account attributes. </p>
	 * 
	 */
	protected static void performUpdate() {
        guiUserUpdate.ViewUserUpdate.displayUserUpdate(
            ViewRole2Home.theStage, ViewRole2Home.theUser);
    }

    /**********
     * <p> Method: performGradePosts() </p>
     *
     * <p> Description: Loads all non-deleted posts into the grading list. </p>
     */
    protected static void performGradePosts() {
        ViewRole2Home.listView_Posts.getItems().clear();

        List<Post> posts = applicationMain.FoundationsMain.postList.getAllPosts();

        for (Post p : posts) {
            if (!p.isDeleted()) {
                ViewRole2Home.listView_Posts.getItems().add(
                    p.getPostId() + " | " + p.getAuthorUsername() + " | " + p.getTitle());
            }
        }

        ViewRole2Home.label_SelectedPost.setText("Selected Post: None");
        ViewRole2Home.label_Status.setText("Posts loaded for grading.");
        ViewRole2Home.textField_Grade.clear();
        ViewRole2Home.textArea_Feedback.clear();
    }

    /**********
     * <p> Method: performSelectPost() </p>
     *
     * <p> Description: Updates the selected-post label and pre-fills any existing
     * grade/feedback for the selected post. </p>
     */
    protected static void performSelectPost() {
        String selected = ViewRole2Home.listView_Posts
            .getSelectionModel().getSelectedItem();

        if (selected == null) {
            ViewRole2Home.label_SelectedPost.setText("Selected Post: None");
            return;
        }

        String postId = selected.split("\\|")[0].trim();
        Post post = applicationMain.FoundationsMain.postList.getPostById(postId);

        ViewRole2Home.label_SelectedPost.setText("Selected Post: " + selected);

        if (post.isGraded()) {
            ViewRole2Home.textField_Grade.setText(String.valueOf(post.getGrade()));
            ViewRole2Home.textArea_Feedback.setText(
                post.getStaffFeedback() == null ? "" : post.getStaffFeedback());
            ViewRole2Home.label_Status.setText("Existing grade loaded.");
        } else {
            ViewRole2Home.textField_Grade.clear();
            ViewRole2Home.textArea_Feedback.clear();
            ViewRole2Home.label_Status.setText("Post selected. Enter grade and feedback.");
        }
    }

    /**********
     * <p> Method: performSaveGrade() </p>
     *
     * <p> Description: Saves the grade and private feedback for the selected post. </p>
     */
    protected static void performSaveGrade() {
        String selected = ViewRole2Home.listView_Posts
            .getSelectionModel().getSelectedItem();

        if (selected == null) {
            ViewRole2Home.label_Status.setText("Please select a post first.");
            return;
        }

        String gradeText = ViewRole2Home.textField_Grade.getText();
        String feedback = ViewRole2Home.textArea_Feedback.getText();

        if (gradeText == null || gradeText.trim().isEmpty()) {
            ViewRole2Home.label_Status.setText("Please enter a grade.");
            return;
        }

        try {
            Double grade = Double.parseDouble(gradeText.trim());
            String postId = selected.split("\\|")[0].trim();

            applicationMain.FoundationsMain.postList.assignGradeToPost(
                postId,
                grade,
                feedback,
                ViewRole2Home.theUser.getUserName()
            );

            ViewRole2Home.label_Status.setText("Grade and private feedback saved.");
        } catch (NumberFormatException e) {
            ViewRole2Home.label_Status.setText("Grade must be a valid number.");
        } catch (IllegalArgumentException e) {
            ViewRole2Home.label_Status.setText(e.getMessage());
        }
    }

    /**********
     * <p> Method: performLogout() </p>
     *
     * <p> Description: Logs out the current user and returns to login. </p>
     */
    protected static void performLogout() {
        guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole2Home.theStage);
    }

    /**********
     * <p> Method: performQuit() </p>
     *
     * <p> Description: Terminates the execution of the program. </p>
     */
    protected static void performQuit() {
        System.exit(0);
    }
}
