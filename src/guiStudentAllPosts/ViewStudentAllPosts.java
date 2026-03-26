package guiStudentAllPosts;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.Post;
import entityClasses.User;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: ViewStudentAllPosts Class. </p>
 *
 * <p> Description: Displays all non-deleted discussion posts. Each row shows
 * the read/unread status, author, thread name, and number of replies.
 * Clicking a post navigates to the View Post page. </p>
 *
 * Student US-1: View all posts.
 * Student US-2: Read/unread indicator.
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ViewStudentAllPosts {

    // -------------------------------------------------------------------------
    // Shared references (set before each display call)
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
    private static ViewStudentAllPosts theView;

    // -------------------------------------------------------------------------
    // Widgets (declared here; positioned in constructor)
    // -------------------------------------------------------------------------
    private static Label    label_PageTitle   = new Label("All Discussion Posts");
    private static Label    label_Instruction = new Label("Click a post to read it.");
    private static ListView<String> listView_Posts = new ListView<>();
    private static Button   button_Back       = new Button("Back");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**********
     * <p> Method: displayStudentAllPosts(Stage ps, User user) </p>
     *
     * <p> Description: Single external entry point. Initialises the singleton on
     * first call, then refreshes the post list and makes the page visible. </p>
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStudentAllPosts(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStudentAllPosts();

        refreshPostList();

        theStage.setTitle("CSE 360 Foundations: All Posts");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton — called once)
    // -------------------------------------------------------------------------
    private ViewStudentAllPosts() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        // Title
        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);

        // Instruction
        setupLabelUI(label_Instruction, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 45);

        // Post list view
        listView_Posts.setLayoutX(20);
        listView_Posts.setLayoutY(70);
        listView_Posts.setPrefWidth(WIDTH - 40);
        listView_Posts.setPrefHeight(HEIGHT - 140);
        listView_Posts.setOnMouseClicked((_) -> {
            int idx = listView_Posts.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                List<Post> posts = getVisiblePosts();
                if (idx < posts.size()) {
                    ControllerStudentAllPosts.openPost(posts.get(idx));
                }
            }
        });

        // Back button
        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 50);
        button_Back.setOnAction((_) -> ControllerStudentAllPosts.goBack());

        root.getChildren().addAll(
            label_PageTitle, label_Instruction, listView_Posts, button_Back);
    }

    // -------------------------------------------------------------------------
    // Refresh helpers
    // -------------------------------------------------------------------------

    /**
     * Rebuilds the ListView from the current postList state.
     * Called each time the page is displayed.
     */
    protected static void refreshPostList() {
        listView_Posts.getItems().clear();
        for (Post p : getVisiblePosts()) {
            String readFlag = p.isReadBy(theUser.getUserName()) ? "[READ]  " : "[UNREAD]";
            int replyCount = applicationMain.FoundationsMain.replyList
                .getRepliesForPost(p.getPostId()).size();
            listView_Posts.getItems().add(
                readFlag + " | " + p.getAuthorUsername()
                + " | Thread: " + p.getThreadName()
                + " | Replies: " + replyCount
                + " | " + p.getTitle());
        }
    }

    /** Returns only non-deleted posts in insertion order. */
    private static List<Post> getVisiblePosts() {
        List<Post> result = new ArrayList<>();
        for (Post p : applicationMain.FoundationsMain.postList.getAllPosts()) {
            if (!p.isDeleted()) result.add(p);
        }
        return result;
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
