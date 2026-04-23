package guiUserLogin;

import database.Database;

import entityClasses.User;
import javafx.stage.Stage;
import java.util.*;

/*******
 * <p> Title: ControllerUserLogin Class. </p>
 * 
 * <p> Description: The Java/FX-based User Login Page.  This class provides the controller
 * actions basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This controller determines if the log in is valid.  If so set up the link to the database, 
 * determines how many roles this user is authorized to play, and the calls one the of the array of
 * role home pages if there is only one role.  If there are more than one role, it setup up and
 * calls the multiple roles dispatch page for the user to determine which role the user wants to
 * play.
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

public class ControllerUserLogin {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	/**
	 * Default constructor is not used.
	 */
	public ControllerUserLogin() {
	}

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	private static Stage theStage;	
	

	// Mapping number of incorrect attempts to users
	private final static Map<String, Integer> failCount = new HashMap<>();
	
	//Mapping lock time for users
	private final static Map<String, Long> lockTime = new HashMap<>();
	
	/**********
	 * <p> Method: public doLogin() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Login button. This
	 * method checks the username and password to see if they are valid.  If so, it then logs that
	 * user in my determining which role to use.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	/*******
	 * <p> Method: doLogin(Stage ts) </p>
	 *
	 * <p> Description: Called when the user clicks the Login button. Validates credentials
	 * and navigates to the appropriate home page.</p>
	 *
	 * @param ts the JavaFX Stage used for navigation
	 */
	protected static void doLogin(Stage ts) {
		theStage = ts;
		String username = ViewUserLogin.text_Username.getText();
		String password = ViewUserLogin.text_Password.getText();
    	boolean loginResult = false;

    	//Checking if the account is locked 
    	if (lockTime.containsKey(username)) {
    		long elapsed = System.currentTimeMillis() - lockTime.get(username); // Time elapsed
    		long lockDuration = 60_000; // Account is locked for 1 minute
    		
    		if (elapsed < lockDuration) {
    			long secondsLeft = (lockDuration - elapsed) / 1000;
    			ViewUserLogin.alertUsernamePasswordError.setContentText(
    	                "Account is locked. Try again in " + secondsLeft + " second(s)");
    	            ViewUserLogin.alertUsernamePasswordError.showAndWait();
    	            return;
    		}
    		else {
    			// Account is unlocked, so reset the attempt count and lock time 
    			lockTime.remove(username);
    			lockTime.remove(username);
    		}
    	}
    	
		// Fetch the user and verify the username
     	if (theDatabase.getUserAccountDetails(username) == false) {
     		// Don't provide too much information.  Don't say the username is invalid or the
     		// password is invalid.  Just say the pair is invalid.
    		ViewUserLogin.alertUsernamePasswordError.setContentText(
    				"Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		return;
    	}
		// System.out.println("*** Username is valid");
		
		// Check to see that the login password matches the account password
     	// If the password is incorrect, a failed attempt will be added to the 
     	// HashMap and when it reaches 3 the user will be blocked for a specified
     	// amount of time from logging in. 
    	String actualPassword = theDatabase.getCurrentPassword();

    	if (password.compareTo(actualPassword) != 0) {
    		
    		//Get the number of failed login attempts. Either 0, which is default, or a number.
    		int currAttempts = failCount.getOrDefault(username, 0);
    		
    		failCount.put(username, currAttempts+1);
    		
    		// User has 3 attempts (0, 1, 2). Locked at 3 
    		if (currAttempts <= 1) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText(
    				"Incorrect username/password. Try again!");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		}
    		
    		//Warning that the user has inputed the wrong password twice 
    		else if (currAttempts == 2) {
    			ViewUserLogin.alertUsernamePasswordError.setContentText(
        				"Incorrect username/password. Account will be locked at one more incorrect attempt.");
        		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		}
    		
    		// Account is locked after the 3rd failed attempt
    		else if (currAttempts == 3) {
    			lockAccount(username);
    			ViewUserLogin.alertUsernamePasswordError.setContentText(
        				"Too many failed attempts. Account will be unlocked in 1 minute.");
        		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		}
    		return;
    	}
    	
    	
		// System.out.println("*** Password is valid for this user");

		// Establish this user's details
    	User user = new User(username, password, theDatabase.getCurrentFirstName(),
    			theDatabase.getCurrentMiddleName(), theDatabase.getCurrentLastName(),
    			theDatabase.getCurrentPreferredFirstName(), theDatabase.getCurrentEmailAddress(),
    			theDatabase.getCurrentAdminRole(),
    			theDatabase.getCurrentNewRole1(), theDatabase.getCurrentNewRole2());

    	// Check if user is using a one-time password
    	if (theDatabase.getCurrentOneTimePassword()) {
    		ViewUserLogin.alertUsernamePasswordError.setContentText(
    				"You are using a one-time password. You must change your password before continuing.");
    		ViewUserLogin.alertUsernamePasswordError.showAndWait();
    		guiUserUpdate.ViewUserUpdate.displayUserUpdate(theStage, user);
    		return;
    	}

    	// See which home page dispatch to use
		int numberOfRoles = theDatabase.getNumberOfRoles(user);		
		System.out.println("*** The number of roles: "+ numberOfRoles);
		if (numberOfRoles == 1) {
			// Single Account Home Page - The user has no choice here
			
			// Admin role
			if (user.getAdminRole()) {
				loginResult = theDatabase.loginAdmin(user);
				if (loginResult) {
					guiAdminHome.ViewAdminHome.displayAdminHome(theStage, user);
				}
			} else if (user.getNewRole1()) {
				loginResult = theDatabase.loginRole1(user);
				if (loginResult) {
					guiRole1.ViewRole1Home.displayRole1Home(theStage, user);
				}
			} else if (user.getNewRole2()) {
				loginResult = theDatabase.loginRole2(user);
				if (loginResult) {
					guiRole2.ViewRole2Home.displayRole2Home(theStage, user);
				}
				// Other roles
			} else {
				System.out.println("***** UserLogin goToUserHome request has an invalid role");
			}
		} else if (numberOfRoles > 1) {
			// Multiple Account Home Page - The user chooses which role to play
			// System.out.println("*** Going to displayMultipleRoleDispatch");
			guiMultipleRoleDispatch.ViewMultipleRoleDispatch.
				displayMultipleRoleDispatch(theStage, user);
		}
	}
	
	/**
	 * <p> Method: lockAccount() </p>
	 * 
	 * <p> Description: This method will lock an account if the number of 
	 *  failed login attempts exceeds 3. After some time, which depends on how
	 *  many times an account as been locked, it will unlock. </p>
	 *  
	 *  @param username - the user name of the account to be locked 
	 *  
	 */
	
	protected static void lockAccount(String username) {
		lockTime.put(username, System.currentTimeMillis());
	}


	/**********
	 * <p> Method: doSetupAccount(Stage theStage, String invitationCode) </p>
	 *
	 * <p> Description: This method is called to reset the page and then populate it with new
	 * content for the new user.</p>
	 *
	 * @param theStage       the JavaFX Stage used for navigation
	 * @param invitationCode the invitation code provided to the new user
	 *
	 */
	protected static void doSetupAccount(Stage theStage, String invitationCode) {
		guiNewAccount.ViewNewAccount.displayNewAccount(theStage, invitationCode);
	}

	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * 
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	

}
