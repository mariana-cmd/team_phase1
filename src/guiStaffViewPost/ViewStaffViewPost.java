package guiStaffViewPost;

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

public class ViewStaffViewPost {

    public static Post currentPost;

    protected static Stage theStage;
    protected static User  theUser;

    private static final double WIDTH  = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static ViewStaffViewPost theView;

    private static Label    label_Thread    = new Label();
    private static Label    label_Author    = new Label();
    private static Label    label_Title     = new Label();
    private static Label    label_Body      = new Label();
    private static Label    label_Replies   = new Label("Replies:");
    private static ListView<String> listView_Replies = new ListView<>();
    private static Button   button_Reply    = new Button("Reply");
    private static Button   button_Update   = new Button("Update Post");
    private static Button   button_Delete   = new Button("Delete Post");
    private static Button   button_Back     = new Button("Back");

    private static Scene theScene;

    public static void displayStaffViewPost(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffViewPost();

        // Mark post as read by current user
        currentPost.markAsRead(theUser.getUserName());

        label_Thread.setText("Thread: " + currentPost.getThreadName());
        label_Author.setText("By: " + currentPost.getAuthorUsername());
        label_Title.setText(currentPost.getTitle());
        label_Body.setText(currentPost.getBody());

        // Reply button only if not deleted
        button_Reply.setVisible(!currentPost.isDeleted());

        // Update button visible only to author and if not deleted
        boolean isAuthor = currentPost.getAuthorUsername().equals(theUser.getUserName());
        button_Update.setVisible(isAuthor && !currentPost.isDeleted());

        // Delete button visible if user has role1 (staff) OR is author, and post not deleted
        boolean isStaff = theUser.getNewRole1();
        button_Delete.setVisible((isStaff || isAuthor) && !currentPost.isDeleted());

        refreshReplies();

        theStage.setTitle("CSE 360 Foundations: View Post (Staff)");
        theStage.setScene(theScene);
        theStage.show();
    }

    private ViewStaffViewPost() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_Thread, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, 5);
        setupLabelUI(label_Author, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, 25);
        setupLabelUI(label_Title,  "Arial", 20, WIDTH - 40, Pos.BASELINE_LEFT, 20, 48);
        setupLabelUI(label_Body,   "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 80);
        label_Body.setWrapText(true);
        label_Body.setMaxHeight(150);

        setupLabelUI(label_Replies, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 240);

        listView_Replies.setLayoutX(20);
        listView_Replies.setLayoutY(260);
        listView_Replies.setPrefWidth(WIDTH - 40);
        listView_Replies.setPrefHeight(HEIGHT - 380);

        setupButtonUI(button_Reply,  "Dialog", 15, 130, Pos.CENTER, 20,          HEIGHT - 55);
        button_Reply.setOnAction((_) -> ControllerStaffViewPost.goToReply());

        setupButtonUI(button_Update, "Dialog", 15, 140, Pos.CENTER, 170,         HEIGHT - 55);
        button_Update.setOnAction((_) -> ControllerStaffViewPost.goToUpdate());

        setupButtonUI(button_Delete, "Dialog", 15, 140, Pos.CENTER, 330,         HEIGHT - 55);
        button_Delete.setOnAction((_) -> ControllerStaffViewPost.performDelete());

        setupButtonUI(button_Back,   "Dialog", 15, 120, Pos.CENTER, WIDTH - 150, HEIGHT - 55);
        button_Back.setOnAction((_) -> ControllerStaffViewPost.goBack());

        root.getChildren().addAll(
            label_Thread, label_Author, label_Title, label_Body,
            label_Replies, listView_Replies,
            button_Reply, button_Update, button_Delete, button_Back);
    }

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
