/**
 * Module descriptor for the FoundationsSP26 application.
 * Requires JavaFX controls for the GUI and java.sql for the H2 database.
 */
module FoundationsSP26 {
	requires javafx.controls;
	requires java.sql;

	opens applicationMain to javafx.graphics, javafx.fxml;
}
