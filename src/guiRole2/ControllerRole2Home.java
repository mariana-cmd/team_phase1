package guiRole2;

import guiRole1.ViewRole1Home;

/*******
 * <p> Title: ControllerRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Staff Home Page.  This class provides the controller
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
	protected static void performUpdate () {
		guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewRole2Home.theStage, ViewRole2Home.theUser);
	}	

	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole2Home.theStage);
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
	 * <p> Method: goToAllPosts() </p>
	 *
	 * <p> Description: Navigates to the Student All Posts page. </p>
	 *
	 * Student US-1: View all discussion posts.
	 */
	protected static void goToAllPosts() {
		// Role1 is staff in this project: navigate to the staff All Posts page
		guiStaffAllPosts.ViewStaffAllPosts.displayStaffAllPosts(
			ViewRole2Home.theStage, ViewRole2Home.theUser);
	}

	/**********
	 * <p> Method: goToMyPosts() </p>
	 *
	 * <p> Description: Navigates to the Student My Posts page. </p>
	 *
	 * Student US-3: View own posts with reply counts.
	 */
	protected static void goToMyPosts() {

		// Navigate to the staff My Posts page
		guiStaffMyPosts.ViewStaffMyPosts.displayStaffMyPosts(
			ViewRole2Home.theStage, ViewRole2Home.theUser);
	}

	/**********
	 * <p> Method: goToSearch() </p>
	 *
	 * <p> Description: Navigates to the Student Search page. </p>
	 *
	 * Student US-5: Search posts by keyword.
	 */
	protected static void goToSearch() {
		guiStudentSearch.ViewStudentSearch.displayStudentSearch(
			ViewRole2Home.theStage, ViewRole2Home.theUser);
	}

	/**********
	 * <p> Method: goToCreatePost() </p>
	 *
	 * <p> Description: Navigates to the Student Create Post page. </p>
	 *
	 * Student US-6: Create a new discussion post.
	 */
	protected static void goToCreatePost() {

		// Navigate to the staff Create Post page
		guiStaffCreatePost.ViewStaffCreatePost.displayStaffCreatePost(
			ViewRole2Home.theStage, ViewRole2Home.theUser);
	}

	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}
}
