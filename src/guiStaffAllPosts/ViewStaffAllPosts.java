package guiStaffAllPosts;

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

public class ViewStaffAllPosts {

    protected static Stage theStage;
    protected static User  theUser;

    private static final double WIDTH  = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static ViewStaffAllPosts theView;

    private static Label    label_PageTitle   = new Label("All Discussion Posts (Staff)");
    private static Label    label_Instruction = new Label("Click a post to read it. Staff can delete any post.");
    private static ListView<String> listView_Posts = new ListView<>();
    private static Button   button_Back       = new Button("Back");

    private static Scene theScene;

    public static void displayStaffAllPosts(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffAllPosts();

        refreshPostList();

        theStage.setTitle("CSE 360 Foundations: All Posts (Staff)");
        theStage.setScene(theScene);
        theStage.show();
    }

    private ViewStaffAllPosts() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);
        setupLabelUI(label_Instruction, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 45);

        listView_Posts.setLayoutX(20);
        listView_Posts.setLayoutY(70);
        listView_Posts.setPrefWidth(WIDTH - 40);
        listView_Posts.setPrefHeight(HEIGHT - 140);
        listView_Posts.setOnMouseClicked((_) -> {
            int idx = listView_Posts.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                List<Post> posts = getVisiblePosts();
                if (idx < posts.size()) {
                    ControllerStaffAllPosts.openPost(posts.get(idx));
                }
            }
        });

        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 50);
        button_Back.setOnAction((_) -> ControllerStaffAllPosts.goBack());

        root.getChildren().addAll(label_PageTitle, label_Instruction, listView_Posts, button_Back);
    }

    protected static void refreshPostList() {
        listView_Posts.getItems().clear();
        for (Post p : getVisiblePosts()) {
            String readFlag = p.isReadBy(theUser.getUserName()) ? "[READ]  " : "[UNREAD]";
            int replyCount = applicationMain.FoundationsMain.replyList
                .getRepliesForPost(p.getPostId()).size();
            String deletedFlag = p.isDeleted() ? " [DELETED]" : "";
            listView_Posts.getItems().add(
                readFlag + " | " + p.getAuthorUsername()
                + " | Thread: " + p.getThreadName()
                + " | Replies: " + replyCount
                + " | " + p.getTitle() + deletedFlag);
        }
    }

    private static List<Post> getVisiblePosts() {
        List<Post> result = new ArrayList<>();
        for (Post p : applicationMain.FoundationsMain.postList.getAllPosts()) {
            if (!p.isDeleted()) result.add(p);
        }
        return result;
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
