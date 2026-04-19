/**
 * Module descriptor for the FoundationsSP26 application.
 * Requires JavaFX controls for the GUI and java.sql for the H2 database.
 */
module FoundationsSP26 {
	requires javafx.controls;
	requires java.sql;
<<<<<<< HEAD
=======
	// Add JUnit 5 API so test annotations/types are accessible when tests are run on the module path
	requires org.junit.jupiter.api;
>>>>>>> 0c228c3 (dfjsd)

	opens applicationMain to javafx.graphics, javafx.fxml;
	opens guiStudentAllPosts to javafx.graphics, javafx.fxml;
	opens guiStudentCreatePost to javafx.graphics, javafx.fxml;
	opens guiStudentViewPost to javafx.graphics, javafx.fxml;
	opens guiStudentMyPosts to javafx.graphics, javafx.fxml;
	opens guiStudentSearch to javafx.graphics, javafx.fxml;
	opens guiStudentCreateReply to javafx.graphics, javafx.fxml;
<<<<<<< HEAD
=======
	// Open admin GUI package for reflection from tests and frameworks.
	// Using a plain "opens" (no target) opens to all modules (including the unnamed module)
	// and avoids the special ALL-UNNAMED token which can cause parser issues in some IDEs.
	opens guiAdminHome;
>>>>>>> 0c228c3 (dfjsd)
}
