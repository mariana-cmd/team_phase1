package guiStudentCreateReply;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.Post;
import entityClasses.Reply;
import entityClasses.User;

/*******
 * <p> Title: ViewStudentCreateReply Class. </p>
 *
 * <p> Description: Provides a form for writing a reply to a discussion post.
 * The parent post title is shown as read-only context at the top. The reply
 * body is limited to Reply.MAX_BODY_LENGTH (5,000) characters. Validation
 * errors are displayed in red. </p>
 *
 * Student US-4: Reply to a discussion post.
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ViewStudentCreateReply {

    // -------------------------------------------------------------------------
    // Navigation state — set by caller before invoking displayStudentCreateReply
    // -------------------------------------------------------------------------
    /** The post being replied to. Must be set before calling displayStudentCreateReply. */
    public static Post parentPost;

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
    private static ViewStudentCreateReply theView;

    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    private static Label   label_PageTitle = new Label("Write a Reply");
    private static Label   label_PostTitle = new Label();
    private static Label   label_Body      = new Label(
        "Reply (max " + Reply.MAX_BODY_LENGTH + " chars):");
    private static TextArea area_Body      = new TextArea();
    private static Label   label_Error     = new Label();
    private static Button  button_Submit   = new Button("Submit Reply");
    private static Button  button_Back     = new Button("Cancel");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**********
     * <p> Method: displayStudentCreateReply(Stage ps, User user) </p>
     *
     * <p> Description: Single external entry point. Clears the body text area
     * and error label on each call. Shows the parent post title as context. </p>
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStudentCreateReply(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStudentCreateReply();

        // Show parent post title as context
        label_PostTitle.setText("Replying to: \"" + parentPost.getTitle() + "\"");

        // Reset form
        area_Body.clear();
        label_Error.setText("");

        theStage.setTitle("CSE 360 Foundations: Create Reply");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton)
    // -------------------------------------------------------------------------
    private ViewStudentCreateReply() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);

        setupLabelUI(label_PostTitle, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 50);
        label_PostTitle.setWrapText(true);

        setupLabelUI(label_Body, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 90);

        area_Body.setLayoutX(20);
        area_Body.setLayoutY(112);
        area_Body.setPrefWidth(WIDTH - 40);
        area_Body.setPrefHeight(HEIGHT - 240);
        area_Body.setWrapText(true);

        setupLabelUI(label_Error, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, HEIGHT - 120);
        label_Error.setTextFill(Color.RED);
        label_Error.setWrapText(true);

        setupButtonUI(button_Submit, "Dialog", 16, 150, Pos.CENTER, WIDTH - 190, HEIGHT - 55);
        button_Submit.setOnAction((_) ->
            ControllerStudentCreateReply.submitReply(area_Body.getText()));

        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 55);
        button_Back.setOnAction((_) -> ControllerStudentCreateReply.goBack());

        root.getChildren().addAll(
            label_PageTitle, label_PostTitle, label_Body, area_Body,
            label_Error, button_Submit, button_Back);
    }

    // -------------------------------------------------------------------------
    // Package-visible helpers
    // -------------------------------------------------------------------------

    /**
     * Displays an error message in red.
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
