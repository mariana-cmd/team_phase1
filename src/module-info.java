/**
 * Module descriptor for the FoundationsSP26 application.
 * Requires JavaFX controls for the GUI and java.sql for the H2 database.
 */
module FoundationsSP26 {
	requires javafx.controls;

	// Add JUnit 5 API so test annotations/types are accessible when tests are run on the module path
	requires org.junit.jupiter.api;
	requires org.junit.platform.commons;
	requires java.sql;


	opens applicationMain to javafx.graphics, javafx.fxml;
	opens guiStudentAllPosts to javafx.graphics, javafx.fxml;
	opens guiStudentCreatePost to javafx.graphics, javafx.fxml;
	opens guiStudentViewPost to javafx.graphics, javafx.fxml;
	opens guiStudentMyPosts to javafx.graphics, javafx.fxml;
	opens guiStudentSearch to javafx.graphics, javafx.fxml;
	opens guiStudentCreateReply to javafx.graphics, javafx.fxml;

}
