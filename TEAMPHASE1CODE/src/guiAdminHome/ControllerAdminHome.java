package guiAdminHome;

import database.Database;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
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

public class ControllerAdminHome {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	/**
	 * Default constructor is not used.
	 */
	public ControllerAdminHome() {
	}
	
	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	/**********
	 * <p> 
	 * 
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
	 */
	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewAdminHome.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewAdminHome.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewAdminHome.alertEmailError.showAndWait();
			return;
		}
		
		// Inform the user that the invitation has been sent and display the invitation code
		String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String invitationCode = theDatabase.generateInvitationCode(emailAddress,
				theSelectedRole);
		String msg = "Code: " + invitationCode + " for role " + theSelectedRole + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewAdminHome.alertEmailSent.setContentText(msg);
		ViewAdminHome.alertEmailSent.showAndWait();
		
		// Update the Admin Home pages status
		ViewAdminHome.text_InvitationEmailAddress.setText("");
		ViewAdminHome.label_NumberOfInvitations.setText("Number of outstanding invitations: " + 
				theDatabase.getNumberOfInvitations());
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: manageInvitations () Method. </p>
	 * 
	 * <p> Description: Protected method that is currently a stub informing the user that
	 * this function has not yet been implemented. </p>
	 */
	protected static void manageInvitations () {
		System.out.println("\n*** WARNING ***: Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.setTitle("*** WARNING ***");
		ViewAdminHome.alertNotImplemented.setHeaderText("Manage Invitations Issue");
		ViewAdminHome.alertNotImplemented.setContentText("Manage Invitations Not Yet Implemented");
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p>
	 *
	 * Title: setOnetimePassword () Method. </p>
	 *
	 * <p> Description: Protected method that allows an admin to set a one-time password
	 * for a user account. The user will be required to change this password on next login. </p>
	 */
	protected static void setOnetimePassword () {
		// Get list of users
		java.util.List<String> userList = theDatabase.getUserList();

		// Remove the placeholder
		if (!userList.isEmpty() && userList.get(0).equals("<Select a User>")) {
			userList.remove(0);
		}

		if (userList.isEmpty()) {
			ViewAdminHome.alertNotImplemented.setTitle("No Users");
			ViewAdminHome.alertNotImplemented.setHeaderText("Cannot Set Password");
			ViewAdminHome.alertNotImplemented.setContentText("No users available.");
			ViewAdminHome.alertNotImplemented.showAndWait();
			return;
		}

		// Create selection dialog
		javafx.scene.control.ChoiceDialog<String> dialog =
			new javafx.scene.control.ChoiceDialog<>(userList.get(0), userList);
		dialog.setTitle("Set One-Time Password");
		dialog.setHeaderText("Select User");
		dialog.setContentText("Choose user:");

		java.util.Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String selectedUser = result.get();

			// Generate one-time password
			String oneTimePass = theDatabase.generateOneTimePassword(selectedUser);

			if (oneTimePass != null) {
				ViewAdminHome.alertNotImplemented.setTitle("Success");
				ViewAdminHome.alertNotImplemented.setHeaderText("One-Time Password Set");
				ViewAdminHome.alertNotImplemented.setContentText(
					"One-time password for " + selectedUser + ": " + oneTimePass +
					"\n\nThe user will be required to change this password upon next login.");
				System.out.println("One-time password set for " + selectedUser + ": " + oneTimePass);
			} else {
				ViewAdminHome.alertNotImplemented.setTitle("Error");
				ViewAdminHome.alertNotImplemented.setHeaderText("Failed");
				ViewAdminHome.alertNotImplemented.setContentText(
					"Could not set one-time password.");
			}
			ViewAdminHome.alertNotImplemented.showAndWait();
		}
	}
	
	/**********
	 * <p>
	 *
	 * Title: deleteUser () Method. </p>
	 *
	 * <p> Description: Protected method that allows an admin to delete a user account
	 * with confirmation and safeguards. </p>
	 */
	protected static void deleteUser() {
		// Get list of users
		java.util.List<String> userList = theDatabase.getUserList();

		// Remove the placeholder
		if (!userList.isEmpty() && userList.get(0).equals("<Select a User>")) {
			userList.remove(0);
		}

		if (userList.isEmpty()) {
			ViewAdminHome.alertNotImplemented.setTitle("No Users");
			ViewAdminHome.alertNotImplemented.setHeaderText("Cannot Delete");
			ViewAdminHome.alertNotImplemented.setContentText("No users available to delete.");
			ViewAdminHome.alertNotImplemented.showAndWait();
			return;
		}

		// Create selection dialog
		javafx.scene.control.ChoiceDialog<String> dialog =
			new javafx.scene.control.ChoiceDialog<>(userList.get(0), userList);
		dialog.setTitle("Delete User");
		dialog.setHeaderText("Select User to Delete");
		dialog.setContentText("Choose user:");

		java.util.Optional<String> result = dialog.showAndWait();
		if (result.isPresent()) {
			String selectedUser = result.get();

			// Confirmation dialog
			javafx.scene.control.Alert confirmAlert =
				new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
			confirmAlert.setTitle("Confirm Delete");
			confirmAlert.setHeaderText("Delete User: " + selectedUser);
			confirmAlert.setContentText("Are you sure you want to delete this user?");

			java.util.Optional<javafx.scene.control.ButtonType> confirmResult = confirmAlert.showAndWait();
			if (confirmResult.isPresent() &&
				confirmResult.get() == javafx.scene.control.ButtonType.OK) {

				boolean success = theDatabase.deleteUser(selectedUser);

				if (success) {
					ViewAdminHome.alertNotImplemented.setTitle("Success");
					ViewAdminHome.alertNotImplemented.setHeaderText("User Deleted");
					ViewAdminHome.alertNotImplemented.setContentText(
						"User " + selectedUser + " has been deleted.");
					ViewAdminHome.label_NumberOfUsers.setText(
						"Number of users: " + theDatabase.getNumberOfUsers());
				} else {
					ViewAdminHome.alertNotImplemented.setTitle("Error");
					ViewAdminHome.alertNotImplemented.setHeaderText("Delete Failed");
					ViewAdminHome.alertNotImplemented.setContentText(
						"Could not delete user. You cannot delete your own account or the last admin.");
				}
				ViewAdminHome.alertNotImplemented.showAndWait();
			}
		}
	}
	
	/**********
	 * <p>
	 *
	 * Title: listUsers () Method. </p>
	 *
	 * <p> Description: Protected method that displays all users in the system with their
	 * details including username, name, email, and roles. </p>
	 */
	protected static void listUsers() {
		java.util.List<Database.UserDetails> users = theDatabase.getAllUserDetails();

		StringBuilder userList = new StringBuilder();
		userList.append("Total Users: ").append(users.size()).append("\n\n");

		for (Database.UserDetails user : users) {
			userList.append("Username: ").append(user.username).append("\n");
			userList.append("Name: ").append(user.firstName).append(" ");
			if (user.middleName != null && !user.middleName.isEmpty()) {
				userList.append(user.middleName).append(" ");
			}
			userList.append(user.lastName).append("\n");
			userList.append("Email: ").append(user.email).append("\n");

			java.util.List<String> roles = new java.util.ArrayList<>();
			if (user.adminRole) roles.add("Admin");
			if (user.role1) roles.add("Student");
			if (user.role2) roles.add("Staff");
			userList.append("Roles: ").append(String.join(", ", roles)).append("\n");
			userList.append("----------------------------------------\n");
		}

		ViewAdminHome.alertNotImplemented.setTitle("User List");
		ViewAdminHome.alertNotImplemented.setHeaderText("All Users in System");
		ViewAdminHome.alertNotImplemented.setContentText(userList.toString());
		ViewAdminHome.alertNotImplemented.showAndWait();
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: addRemoveRoles () Method. </p>
	 * 
	 * <p> Description: Protected method that allows an admin to add and remove roles for any of
	 * the users currently in the system.  This is done by invoking the AddRemoveRoles Page. There
	 * is no need to specify the home page for the return as this can only be initiated by and
	 * Admin.</p>
	 */
	protected static void addRemoveRoles() {
		guiAddRemoveRoles.ViewAddRemoveRoles.displayAddRemoveRoles(ViewAdminHome.theStage, 
				ViewAdminHome.theUser);
	}
	
	/**********
	 * <p>
	 *
	 * Title: invalidEmailAddress () Method. </p>
	 *
	 * <p> Description: Protected method that validates an email address using the EmailRecognizer
	 * to ensure proper format before use.</p>
	 *
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		String validationError = emailRecognizer.EmailRecognizer.checkEmailAddress(emailAddress);

		if (!validationError.isEmpty()) {
			ViewAdminHome.alertEmailError.setContentText(
					"Invalid email address:\n" + validationError);
			ViewAdminHome.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewAdminHome.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
}
