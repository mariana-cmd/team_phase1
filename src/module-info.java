/**
 * Module descriptor for the FoundationsSP26 application.
 * Requires JavaFX controls for the GUI and java.sql for the H2 database.
 *
 * Every GUI package must be opened to javafx.graphics so that JavaFX can
 * reflectively instantiate View classes when running as a named module
 * (which takes effect when the Eclipse source root is set to "src").
 */
module FoundationsSP26 {
	requires javafx.controls;
	requires java.sql;
	requires org.junit.jupiter.api;

	// Core
	opens applicationMain            to javafx.graphics, javafx.fxml;

	// Auth / account flows
	opens guiFirstAdmin              to javafx.graphics, javafx.fxml;
	opens guiUserLogin               to javafx.graphics, javafx.fxml;
	opens guiNewAccount              to javafx.graphics, javafx.fxml;
	opens guiUserUpdate              to javafx.graphics, javafx.fxml;
	opens guiMultipleRoleDispatch    to javafx.graphics, javafx.fxml;
	opens guiAddRemoveRoles          to javafx.graphics, javafx.fxml;
	opens guiTools                   to javafx.graphics, javafx.fxml;

	// Role home pages
	opens guiAdminHome               to javafx.graphics, javafx.fxml;
	opens guiRole1                   to javafx.graphics, javafx.fxml;
	opens guiRole2                   to javafx.graphics, javafx.fxml;

	// Student discussion features
	opens guiStudentAllPosts         to javafx.graphics, javafx.fxml;
	opens guiStudentCreatePost       to javafx.graphics, javafx.fxml;
	opens guiStudentViewPost         to javafx.graphics, javafx.fxml;
	opens guiStudentMyPosts          to javafx.graphics, javafx.fxml;
	opens guiStudentSearch           to javafx.graphics, javafx.fxml;
	opens guiStudentCreateReply      to javafx.graphics, javafx.fxml;

	// Staff request feature
	opens guiStaffRequestList        to javafx.graphics, javafx.fxml;
	opens guiStaffCreateRequest      to javafx.graphics, javafx.fxml;
	opens guiStaffRequestDetail      to javafx.graphics, javafx.fxml;
}
