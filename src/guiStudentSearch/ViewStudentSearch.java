package guiStudentSearch;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.Post;
import entityClasses.User;

import java.util.List;

/*******
 * <p> Title: ViewStudentSearch Class. </p>
 *
 * <p> Description: Provides a keyword search over all non-deleted posts. The
 * search is triggered by the Search button or by pressing Enter in the text
 * field. Results are displayed in a ListView; clicking a result opens the post. </p>
 *
 * Student US-5: Search posts by keyword.
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ViewStudentSearch {

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
    private static ViewStudentSearch theView;

    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    private static Label     label_PageTitle = new Label("Search Posts");
    private static Label     label_Keyword   = new Label("Keyword:");
    private static TextField field_Keyword   = new TextField();
    private static Button    button_Search   = new Button("Search");
    private static Label     label_Results   = new Label("Results:");
    private static ListView<String> listView_Results = new ListView<>();
    private static Label     label_Error     = new Label();
    private static Button    button_Back     = new Button("Back");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**********
     * <p> Method: displayStudentSearch(Stage ps, User user) </p>
     *
     * <p> Description: Single external entry point. Clears previous search
     * state on each call. </p>
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStudentSearch(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStudentSearch();

        field_Keyword.clear();
        listView_Results.getItems().clear();
        label_Error.setText("");

        theStage.setTitle("CSE 360 Foundations: Search Posts");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton)
    // -------------------------------------------------------------------------
    private ViewStudentSearch() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);

        setupLabelUI(label_Keyword, "Arial", 16, 90, Pos.BASELINE_LEFT, 20, 50);

        field_Keyword.setLayoutX(120);
        field_Keyword.setLayoutY(47);
        field_Keyword.setPrefWidth(WIDTH - 290);
        // Pressing Enter triggers the search — Student US-5
        field_Keyword.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                ControllerStudentSearch.performSearch(field_Keyword.getText());
            }
        });

        setupButtonUI(button_Search, "Dialog", 15, 120, Pos.CENTER, WIDTH - 155, 44);
        button_Search.setOnAction((_) ->
            ControllerStudentSearch.performSearch(field_Keyword.getText()));

        setupLabelUI(label_Results, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 85);

        listView_Results.setLayoutX(20);
        listView_Results.setLayoutY(108);
        listView_Results.setPrefWidth(WIDTH - 40);
        listView_Results.setPrefHeight(HEIGHT - 230);
        listView_Results.setOnMouseClicked((_) -> {
            int idx = listView_Results.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                List<Post> subset = applicationMain.FoundationsMain.postList.getCurrentSubset();
                if (idx < subset.size()) {
                    ControllerStudentSearch.openPost(subset.get(idx));
                }
            }
        });

        setupLabelUI(label_Error, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, HEIGHT - 110);
        label_Error.setTextFill(Color.RED);
        label_Error.setWrapText(true);

        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 50);
        button_Back.setOnAction((_) -> ControllerStudentSearch.goBack());

        root.getChildren().addAll(
            label_PageTitle,
            label_Keyword, field_Keyword, button_Search,
            label_Results, listView_Results,
            label_Error, button_Back);
    }

    // -------------------------------------------------------------------------
    // Package-visible helpers
    // -------------------------------------------------------------------------

    /**
     * Rebuilds the results ListView from the current postList subset.
     * Called by the controller after a successful search.
     */
    protected static void refreshResults() {
        listView_Results.getItems().clear();
        for (Post p : applicationMain.FoundationsMain.postList.getCurrentSubset()) {
            int replyCount = applicationMain.FoundationsMain.replyList
                .getRepliesForPost(p.getPostId()).size();
            listView_Results.getItems().add(
                p.getTitle()
                + " | By: " + p.getAuthorUsername()
                + " | Thread: " + p.getThreadName()
                + " | Replies: " + replyCount);
        }
        if (listView_Results.getItems().isEmpty()) {
            label_Error.setText("No posts matched your search.");
        }
    }

    /**
     * Displays an error or info message in red.
     *
     * @param message the message to show (empty string clears it)
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
