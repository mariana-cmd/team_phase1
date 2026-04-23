package guiStaffCreateRequest;

import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewStaffCreateRequest Class. </p>
 *
 * <p> Description: Form for a Role2 user to create a new staff request.
 * Contains a large TextArea for the description, a red error label,
 * and Submit / Cancel buttons. </p>
 */
public class ViewStaffCreateRequest {

    // -------------------------------------------------------------------------
    // Shared references
    // -------------------------------------------------------------------------
    protected static Stage theStage;
    protected static User  theUser;

    // -------------------------------------------------------------------------
    // Layout constants
    // -------------------------------------------------------------------------
    private static final double WIDTH  = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    // -------------------------------------------------------------------------
    // Singleton guard
    // -------------------------------------------------------------------------
    private static ViewStaffCreateRequest theView;

    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    private static Label    label_Title  = new Label("New Staff Request");
    private static Label    label_Prompt = new Label("Describe what you need the admin to do:");
    static         TextArea text_Description = new TextArea();
    static         Label    label_Error  = new Label();
    private static Button   button_Submit = new Button("Submit");
    private static Button   button_Cancel = new Button("Cancel");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**
     * Single external entry point. Clears the form and displays the page.
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStaffCreateRequest(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffCreateRequest();

        // Clear state on each visit
        text_Description.clear();
        label_Error.setText("");

        theStage.setTitle("CSE 360 Foundations: New Staff Request");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton init)
    // -------------------------------------------------------------------------
    private ViewStaffCreateRequest() {
        theScene = new Scene(buildPane(), WIDTH, HEIGHT);
    }

    private static Pane buildPane() {
        Pane pane = new Pane();

        setupLabelUI(label_Title,  "Arial", 26, WIDTH, Pos.CENTER, 0, 5);
        setupLabelUI(label_Prompt, "Arial", 16, WIDTH - 40, Pos.BASELINE_LEFT, 20, 55);

        text_Description.setLayoutX(20);
        text_Description.setLayoutY(80);
        text_Description.setPrefWidth(760);
        text_Description.setPrefHeight(420);
        text_Description.setWrapText(true);
        text_Description.setFont(Font.font("Arial", 14));

        label_Error.setLayoutX(20);
        label_Error.setLayoutY(515);
        label_Error.setMinWidth(580);
        label_Error.setTextFill(Color.RED);
        label_Error.setFont(Font.font("Arial", 13));

        setupButtonUI(button_Submit, "Dialog", 16, 150, Pos.CENTER, 630, 543);
        setupButtonUI(button_Cancel, "Dialog", 16, 130, Pos.CENTER, 20,  543);

        button_Submit.setOnAction(_ -> ControllerStaffCreateRequest.submitRequest());
        button_Cancel.setOnAction(_ -> ControllerStaffCreateRequest.cancel());

        pane.getChildren().addAll(
                label_Title, label_Prompt,
                text_Description,
                label_Error,
                button_Submit, button_Cancel);

        return pane;
    }

    // -------------------------------------------------------------------------
    // Package-private helpers used by Controller
    // -------------------------------------------------------------------------

    /** Displays an error message in red. */
    static void showError(String msg) {
        label_Error.setText(msg);
    }

    // -------------------------------------------------------------------------
    // UI helper methods
    // -------------------------------------------------------------------------
    private static void setupLabelUI(Label l, String ff, double f,
            double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private static void setupButtonUI(Button b, String ff, double f,
            double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}
