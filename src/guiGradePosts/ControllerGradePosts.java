package guiGradePosts;

import entityClasses.Post;
import guiStaffViewPost.ViewStaffViewPost;

import java.util.List;
/*******
 * <p> Title: ControllerGradePosts Class. </p>
 * 
 * <p> Description: Controller for the ViewGradePosts class. </p>
 */

public class ControllerGradePosts {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	/**
	 * Default constructor is not used.
	 */
	public ControllerGradePosts() {
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
        		ViewGradePosts.theStage, ViewGradePosts.theUser);
    }

    /**********
     * <p> Method: performGradePosts() </p>
     *
     * <p> Description: Loads all non-deleted posts into the grading list. </p>
     */
    protected static void performGradePosts() {
    	ViewGradePosts.listView_Posts.getItems().clear();

        List<Post> posts = applicationMain.FoundationsMain.postList.getAllPosts();

        for (Post p : posts) {
            if (!p.isDeleted()) {
            	ViewGradePosts.listView_Posts.getItems().add(
                    p.getPostId() + " | " + p.getAuthorUsername() + " | " + p.getTitle());
            }
        }

        ViewGradePosts.label_SelectedPost.setText("Selected Post: None");
        ViewGradePosts.label_Status.setText("Posts loaded for grading.");
        ViewGradePosts.textField_Grade.clear();
        ViewGradePosts.textArea_Feedback.clear();
    }

    /**********
     * <p> Method: performSelectPost() </p>
     *
     * <p> Description: Updates the selected-post label and pre-fills any existing
     * grade/feedback for the selected post. </p>
     */
    protected static void performSelectPost() {
        String selected = ViewGradePosts.listView_Posts
            .getSelectionModel().getSelectedItem();

        if (selected == null) {
        	ViewGradePosts.label_SelectedPost.setText("Selected Post: None");
            return;
        }

        String postId = selected.split("\\|")[0].trim();
        Post post = applicationMain.FoundationsMain.postList.getPostById(postId);

        ViewGradePosts.label_SelectedPost.setText("Selected Post: " + selected);

        if (post.isGraded()) {
        	ViewGradePosts.textField_Grade.setText(String.valueOf(post.getGrade()));
        	ViewGradePosts.textArea_Feedback.setText(
                post.getStaffFeedback() == null ? "" : post.getStaffFeedback());
        	ViewGradePosts.label_Status.setText("Existing grade loaded.");
        } else {
        	ViewGradePosts.textField_Grade.clear();
        	ViewGradePosts.textArea_Feedback.clear();
        	ViewGradePosts.label_Status.setText("Post selected. Enter grade and feedback.");
        }
    }

    /**********
     * <p> Method: performSaveGrade() </p>
     *
     * <p> Description: Saves the grade and private feedback for the selected post. </p>
     */
    protected static void performSaveGrade() {
        String selected = ViewGradePosts.listView_Posts
            .getSelectionModel().getSelectedItem();

        if (selected == null) {
        	ViewGradePosts.label_Status.setText("Please select a post first.");
            return;
        }

        String gradeText = ViewGradePosts.textField_Grade.getText();
        String feedback = ViewGradePosts.textArea_Feedback.getText();

        if (gradeText == null || gradeText.trim().isEmpty()) {
        	ViewGradePosts.label_Status.setText("Please enter a grade.");
            return;
        }

        try {
            Double grade = Double.parseDouble(gradeText.trim());
            String postId = selected.split("\\|")[0].trim();

            applicationMain.FoundationsMain.postList.assignGradeToPost(
                postId,
                grade,
                feedback,
                ViewGradePosts.theUser.getUserName()
            );

            ViewGradePosts.label_Status.setText("Grade and private feedback saved.");
        } catch (NumberFormatException e) {
        	ViewGradePosts.label_Status.setText("Grade must be a valid number.");
        } catch (IllegalArgumentException e) {
        	ViewGradePosts.label_Status.setText(e.getMessage());
        }
    }

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Goes back to the home page. </p>
     */
    protected static void goBack() {
        guiRole2.ViewRole2Home.displayRole2Home(
            ViewGradePosts.theStage, ViewGradePosts.theUser);
    }

 
}