package guiStaffMyPosts;

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

import java.util.ArrayList;
import java.util.List;

public class ViewStaffMyPosts {

    protected static Stage theStage;
    protected static User  theUser;

    private static final double WIDTH  = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static ViewStaffMyPosts theView;

    private static Label    label_PageTitle = new Label("My Posts");
    private static Label    label_Info      = new Label("Posts you have authored. Click one to open it.");
    private static ListView<String> listView_MyPosts = new ListView<>();
    private static Button   button_Back     = new Button("Back");

    private static Scene theScene;

    public static void displayStaffMyPosts(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffMyPosts();

        refreshMyPosts();

        theStage.setTitle("CSE 360 Foundations: My Posts (Staff)");
        theStage.setScene(theScene);
        theStage.show();
    }

    private ViewStaffMyPosts() {
        Pane root = new Pane();
        theScene  = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);
        setupLabelUI(label_Info, "Arial", 14, WIDTH - 40, Pos.BASELINE_LEFT, 20, 45);

        listView_MyPosts.setLayoutX(20);
        listView_MyPosts.setLayoutY(70);
        listView_MyPosts.setPrefWidth(WIDTH - 40);
        listView_MyPosts.setPrefHeight(HEIGHT - 140);
        listView_MyPosts.setOnMouseClicked((_) -> {
            int idx = listView_MyPosts.getSelectionModel().getSelectedIndex();
            if (idx >= 0) {
                List<Post> myPosts = getMyPosts();
                if (idx < myPosts.size()) {
                    ControllerStaffMyPosts.openPost(myPosts.get(idx));
                }
            }
        });

        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 50);
        button_Back.setOnAction((_) -> ControllerStaffMyPosts.goBack());

        root.getChildren().addAll(label_PageTitle, label_Info, listView_MyPosts, button_Back);
    }

    protected static void refreshMyPosts() {
        listView_MyPosts.getItems().clear();
        for (Post p : getMyPosts()) {
            List<Reply> replies = applicationMain.FoundationsMain.replyList
                .getRepliesForPost(p.getPostId());
            int totalReplies = replies.size();
            long unreadReplies = replies.stream()
                .filter(r -> !r.isReadBy(theUser.getUserName()))
                .count();
            String deletedFlag = p.isDeleted() ? " [DELETED]" : "";
            listView_MyPosts.getItems().add(
                p.getTitle() + deletedFlag
                + " | Thread: " + p.getThreadName()
                + " | Replies: " + totalReplies
                + " | Unread: " + unreadReplies);
        }
    }

    private static List<Post> getMyPosts() {
        List<Post> result = new ArrayList<>();
        for (Post p : applicationMain.FoundationsMain.postList.getAllPosts()) {
            if (p.getAuthorUsername().equals(theUser.getUserName())) {
                result.add(p);
            }
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
