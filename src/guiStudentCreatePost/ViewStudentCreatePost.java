package guiStudentCreatePost;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

/*******
 * <p> Title: ViewStudentCreatePost Class. </p>
 *
 * <p> Description: Provides a form for a student to create a new discussion post.
 * The form includes a title TextField (max 200 chars), a body TextArea
 * (max 10,000 chars), and a thread ComboBox. Validation errors are shown in red. </p>
 *
 * Student US-6: Create a new discussion post.
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ViewStudentCreatePost {

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
    private static ViewStudentCreatePost theView;

    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    private static Label     label_PageTitle = new Label("Create New Post");
    private static Label     label_Title     = new Label("Title (max 200 chars):");
    private static TextField field_Title     = new TextField();
    private static Label     label_Thread    = new Label("Thread:");
    private static ComboBox<String> combo_Thread = new ComboBox<>();
    private static Label     label_Body      = new Label("Body (max 10,000 chars):");
    private static TextArea  area_Body       = new TextArea();
    private static Label     label_Error     = new Label();
    private static Button    button_Submit   = new Button("Submit Post");
    private static Button    button_Back     = new Button("Back");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**********
     * <p> Method: displayStudentCreatePost(Stage ps, User user) </p>
     *
     * <p> Description: Single external entry point. Clears the form fields and
     * error label on each call so the student starts fresh. </p>
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStudentCreatePost(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStudentCreatePost();

        // Reset form
        field_Title.clear();
        area_Body.clear();
        combo_Thread.getSelectionModel().selectFirst();
        label_Error.setText("");

        theStage.setTitle("CSE 360 Foundations: Create Post");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton)
    // -------------------------------------------------------------------------
    private ViewStudentCreatePost() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        // Title label
        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);

        // Title field
        setupLabelUI(label_Title, "Arial", 14, 300, Pos.BASELINE_LEFT, 20, 50);
        field_Title.setLayoutX(20);
        field_Title.setLayoutY(72);
        field_Title.setPrefWidth(WIDTH - 40);

        // Thread ComboBox
        setupLabelUI(label_Thread, "Arial", 14, 100, Pos.BASELINE_LEFT, 20, 108);
        combo_Thread.getItems().addAll(
            "General", "Homework", "Project", "Exam", "Other");
        combo_Thread.getSelectionModel().selectFirst();
        combo_Thread.setEditable(false);
        combo_Thread.setLayoutX(120);
        combo_Thread.setLayoutY(105);
        combo_Thread.setPrefWidth(200);

        // Body text area
        setupLabelUI(label_Body, "Arial", 14, 300, Pos.BASELINE_LEFT, 20, 145);
        area_Body.setLayoutX(20);
        area_Body.setLayoutY(167);
        area_Body.setPrefWidth(WIDTH - 40);
        area_Body.setPrefHeight(HEIGHT - 310);
        area_Body.setWrapText(true);

        // Error label (red)
        setupLabelUI(label_Error, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, HEIGHT - 130);
        label_Error.setTextFill(Color.RED);
        label_Error.setWrapText(true);

        // Buttons
        setupButtonUI(button_Submit, "Dialog", 16, 150, Pos.CENTER, WIDTH - 190, HEIGHT - 55);
        button_Submit.setOnAction((_) -> ControllerStudentCreatePost.submitPost(
            field_Title.getText(), area_Body.getText(),
            combo_Thread.getSelectionModel().getSelectedItem()));

        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 55);
        button_Back.setOnAction((_) -> ControllerStudentCreatePost.goBack());

        root.getChildren().addAll(
            label_PageTitle,
            label_Title, field_Title,
            label_Thread, combo_Thread,
            label_Body, area_Body,
            label_Error,
            button_Submit, button_Back);
    }

    // -------------------------------------------------------------------------
    // Package-visible helpers
    // -------------------------------------------------------------------------

    /**
     * Displays the given error message in red beneath the form.
     *
     * @param message the error text to display
     */
    protected static void showError(String message) {
        label_Error.setText(message);
    }

    // -------------------------------------------------------------------------
    // UI helpers
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
