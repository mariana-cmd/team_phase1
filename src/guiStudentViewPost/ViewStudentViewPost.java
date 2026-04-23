package guiStudentViewPost;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.Post;
import entityClasses.Reply;
import entityClasses.User;

import java.util.List;

/*******
 * <p> Title: ViewStudentViewPost Class. </p>
 *
 * <p> Description: Displays the full content of a single post and its replies.
 * Marks the post as read by the current user on display. Shows a Reply button
 * (hidden if the post is deleted) and a Delete button (visible only to the
 * post author on a non-deleted post). Displays grade and private feedback only
 * to the author of the post. </p>
 *
 * Student US-2: Mark post as read.
 * Student US-4: Reply to a post.
 * Student US-7: Delete own post.
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ViewStudentViewPost {

    // -------------------------------------------------------------------------
    // Navigation state — set by caller before invoking displayStudentViewPost
    // -------------------------------------------------------------------------
    /** The post to display. Must be set before calling displayStudentViewPost. */
    public static Post currentPost;

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
    private static ViewStudentViewPost theView;

    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    private static Label label_Thread = new Label();
    private static Label label_Author = new Label();
    private static Label label_Title = new Label();
    private static Label label_Body = new Label();
    private static Label label_Replies = new Label("Replies:");
    private static Label label_Grade = new Label();
    private static Label label_Feedback = new Label();

    private static ListView<String> listView_Replies = new ListView<>();

    private static Button button_Reply = new Button("Reply");
    private static Button button_Delete = new Button("Delete Post");
    private static Button button_Back = new Button("Back");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**********
     * <p> Method: displayStudentViewPost(Stage ps, User user) </p>
     *
     * <p> Description: Single external entry point. Uses currentPost (set by
     * caller). Marks the post as read, populates all dynamic labels, refreshes
     * the reply list, and shows grade/private feedback only to the post author. </p>
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStudentViewPost(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        if (theView == null) theView = new ViewStudentViewPost();

        // Mark post as read by current user — Student US-2
        currentPost.markAsRead(theUser.getUserName());

        // Populate dynamic fields
        label_Thread.setText("Thread: " + currentPost.getThreadName());
        label_Author.setText("By: " + currentPost.getAuthorUsername());
        label_Title.setText(currentPost.getTitle());
        label_Body.setText(currentPost.getBody());

        // Show Reply button only if post is not deleted
        button_Reply.setVisible(!currentPost.isDeleted());

        // Show Delete button only if this user is the author and post is not deleted
        boolean isAuthor = currentPost.getAuthorUsername()
                .equals(theUser.getUserName());

        button_Delete.setVisible(isAuthor && !currentPost.isDeleted());

        // Show grade and private feedback only to the author of the post
        if (isAuthor && currentPost.isGraded()) {
            label_Grade.setText("Grade: " + currentPost.getGrade());

            String feedbackText = currentPost.getStaffFeedback();
            if (feedbackText == null) feedbackText = "";

            label_Feedback.setText("Private Feedback: " + feedbackText);

            label_Grade.setVisible(true);
            label_Feedback.setVisible(true);
        } else {
            label_Grade.setVisible(false);
            label_Feedback.setVisible(false);
        }

        // Populate replies
        refreshReplies();

        theStage.setTitle("CSE 360 Foundations: View Post");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton)
    // -------------------------------------------------------------------------
    private ViewStudentViewPost() {
        Pane root = new Pane();
        theScene = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_Thread, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, 5);
        setupLabelUI(label_Author, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, 25);
        setupLabelUI(label_Title, "Arial", 20, WIDTH - 40, Pos.BASELINE_LEFT, 20, 48);
        setupLabelUI(label_Body, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 80);

        label_Body.setWrapText(true);
        label_Body.setMaxHeight(150);

        setupLabelUI(label_Grade, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 210);
        setupLabelUI(label_Feedback, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 230);
        label_Feedback.setWrapText(true);

        setupLabelUI(label_Replies, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 290);

        listView_Replies.setLayoutX(20);
        listView_Replies.setLayoutY(310);
        listView_Replies.setPrefWidth(WIDTH - 40);
        listView_Replies.setPrefHeight(HEIGHT - 380);

        setupButtonUI(button_Reply, "Dialog", 15, 130, Pos.CENTER, 20, HEIGHT - 55);
        button_Reply.setOnAction((_) -> ControllerStudentViewPost.goToReply());

        setupButtonUI(button_Delete, "Dialog", 15, 140, Pos.CENTER, 170, HEIGHT - 55);
        button_Delete.setOnAction((_) -> ControllerStudentViewPost.performDelete());

        setupButtonUI(button_Back, "Dialog", 15, 120, Pos.CENTER, WIDTH - 150, HEIGHT - 55);
        button_Back.setOnAction((_) -> ControllerStudentViewPost.goBack());

        root.getChildren().addAll(
            label_Thread, label_Author, label_Title, label_Body,
            label_Grade, label_Feedback,
            label_Replies, listView_Replies,
            button_Reply, button_Delete, button_Back);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    /** Rebuilds the reply ListView for the current post. */
    protected static void refreshReplies() {
        listView_Replies.getItems().clear();
        List<Reply> replies = applicationMain.FoundationsMain.replyList
                .getRepliesForPost(currentPost.getPostId());

        for (Reply r : replies) {
            listView_Replies.getItems().add(
                    r.getAuthorUsername() + ": " + r.getBody());
        }
    }

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