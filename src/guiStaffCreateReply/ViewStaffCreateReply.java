package guiStaffCreateReply;

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
import entityClasses.User;

public class ViewStaffCreateReply {

    // Navigation state
    public static Post parentPost;

    protected static Stage theStage;
    protected static User  theUser;

    private static final double WIDTH  = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static ViewStaffCreateReply theView;

    private static Label label_PageTitle = new Label("Create Reply");
    private static Label label_Error = new Label();
    private static TextArea area_Body = new TextArea();
    private static Button button_Submit = new Button("Submit Reply");
    private static Button button_Back   = new Button("Back");

    private static Scene theScene;

    public static void displayStaffCreateReply(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffCreateReply();

        area_Body.clear();
        label_Error.setText("");

        theStage.setTitle("CSE 360 Foundations: Create Reply (Staff)");
        theStage.setScene(theScene);
        theStage.show();
    }

    private ViewStaffCreateReply() {
        Pane root = new Pane();
        theScene = new Scene(root, WIDTH, HEIGHT);

        setupLabelUI(label_PageTitle, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);

        area_Body.setLayoutX(20);
        area_Body.setLayoutY(72);
        area_Body.setPrefWidth(WIDTH - 40);
        area_Body.setPrefHeight(HEIGHT - 200);
        area_Body.setWrapText(true);

        setupLabelUI(label_Error, "Arial", 13, WIDTH - 40, Pos.BASELINE_LEFT, 20, HEIGHT - 120);
        label_Error.setTextFill(Color.RED);

        setupButtonUI(button_Submit, "Dialog", 16, 150, Pos.CENTER, WIDTH - 190, HEIGHT - 55);
        button_Submit.setOnAction((_) -> ControllerStaffCreateReply.submitReply(area_Body.getText()));

        setupButtonUI(button_Back, "Dialog", 16, 120, Pos.CENTER, 20, HEIGHT - 55);
        button_Back.setOnAction((_) -> ControllerStaffCreateReply.goBack());

        root.getChildren().addAll(label_PageTitle, area_Body, label_Error, button_Submit, button_Back);
    }

    protected static void showError(String message) { label_Error.setText(message); }

    private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}
