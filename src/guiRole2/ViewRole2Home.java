package guiRole2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;

/*******
 * <p> Title: ViewRole2Home Class. </p>
 *
 * <p> Description: The Java/FX-based Role2 Home Page. This page supports
 * account updates and grading student posts with private feedback. </p>
 *
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 *
 * @author Lynn Robert Carter
 *
 * @version 1.00 2025-04-20 Initial version
 */
public class ViewRole2Home {

    private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    protected static Label label_PageTitle = new Label();
    protected static Label label_UserDetails = new Label();
    protected static Button button_UpdateThisUser = new Button("Account Update");

    protected static Line line_Separator1 = new Line(20, 95, width - 20, 95);

    protected static Label label_GradeSection = new Label("Grade Student Posts");
    protected static Button button_GradePosts = new Button("Load Posts");

    protected static ListView<String> listView_Posts = new ListView<>();
    protected static Label label_SelectedPost = new Label("Selected Post: None");

    protected static Label label_Grade = new Label("Grade:");
    protected static TextField textField_Grade = new TextField();

    protected static Label label_Feedback = new Label("Private Feedback:");
    protected static TextArea textArea_Feedback = new TextArea();

    protected static Button button_SaveGrade = new Button("Save Grade");
    protected static Label label_Status = new Label("");

    protected static Line line_Separator4 = new Line(20, 525, width - 20, 525);
    protected static Button button_Logout = new Button("Logout");
    protected static Button button_Quit = new Button("Quit");

    private static ViewRole2Home theView;
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    protected static Stage theStage;
    protected static Pane theRootPane;
    protected static User theUser;

    private static Scene theRole2HomeScene;
    protected static final int theRole = 3;

    /**********
     * <p> Method: displayRole2Home(Stage ps, User user) </p>
     *
     * <p> Description: External entry point to display the Role2 Home Page. </p>
     *
     * @param ps the JavaFX Stage
     * @param user the current logged-in user
     */
    public static void displayRole2Home(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        if (theView == null) theView = new ViewRole2Home();

        theDatabase.getUserAccountDetails(user.getUserName());
        applicationMain.FoundationsMain.activeHomePage = theRole;

        label_UserDetails.setText("User: " + theUser.getUserName());

        theStage.setTitle("CSE 360 Foundations: Role2 Home Page");
        theStage.setScene(theRole2HomeScene);
        theStage.show();
    }

    /**********
     * <p> Method: ViewRole2Home() </p>
     *
     * <p> Description: Initializes all GUI widgets and event handlers. </p>
     */
    private ViewRole2Home() {
        theRootPane = new Pane();
        theRole2HomeScene = new Scene(theRootPane, width, height);

        label_PageTitle.setText("Role2 Home Page");
        setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        label_UserDetails.setText("User:");
        setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);

        setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
        button_UpdateThisUser.setOnAction(evt -> {
            evt.consume();
            ControllerRole2Home.performUpdate();
        });

        setupLabelUI(label_GradeSection, "Arial", 22, width - 40, Pos.BASELINE_LEFT, 20, 110);

        setupButtonUI(button_GradePosts, "Dialog", 16, 140, Pos.CENTER, 640, 108);
        button_GradePosts.setOnAction(evt -> {
            evt.consume();
            ControllerRole2Home.performGradePosts();
        });

        listView_Posts.setLayoutX(20);
        listView_Posts.setLayoutY(150);
        listView_Posts.setPrefWidth(460);
        listView_Posts.setPrefHeight(250);
        listView_Posts.setOnMouseClicked(evt -> {
            evt.consume();
            ControllerRole2Home.performSelectPost();
        });

        setupLabelUI(label_SelectedPost, "Arial", 14, width - 40, Pos.BASELINE_LEFT, 20, 410);

        setupLabelUI(label_Grade, "Arial", 16, 100, Pos.BASELINE_LEFT, 510, 160);

        textField_Grade.setLayoutX(580);
        textField_Grade.setLayoutY(155);
        textField_Grade.setPrefWidth(180);

        setupLabelUI(label_Feedback, "Arial", 16, 200, Pos.BASELINE_LEFT, 510, 205);

        textArea_Feedback.setLayoutX(510);
        textArea_Feedback.setLayoutY(235);
        textArea_Feedback.setPrefWidth(250);
        textArea_Feedback.setPrefHeight(120);
        textArea_Feedback.setWrapText(true);

        setupButtonUI(button_SaveGrade, "Dialog", 16, 150, Pos.CENTER, 560, 370);
        button_SaveGrade.setOnAction(evt -> {
            evt.consume();
            ControllerRole2Home.performSaveGrade();
        });

        setupLabelUI(label_Status, "Arial", 13, width - 40, Pos.BASELINE_LEFT, 20, 485);

        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction(evt -> {
            evt.consume();
            ControllerRole2Home.performLogout();
        });

        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction(evt -> {
            evt.consume();
            ControllerRole2Home.performQuit();
        });

        theRootPane.getChildren().addAll(
            label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
            label_GradeSection, button_GradePosts, listView_Posts, label_SelectedPost,
            label_Grade, textField_Grade, label_Feedback, textArea_Feedback,
            button_SaveGrade, label_Status,
            line_Separator4, button_Logout, button_Quit
        );
    }

    private static void setupLabelUI(Label l, String ff, double f, double w,
                                     Pos p, double x, double y) {
        l.setFont(Font.font(ff, f));
        l.setMinWidth(w);
        l.setAlignment(p);
        l.setLayoutX(x);
        l.setLayoutY(y);
    }

    private static void setupButtonUI(Button b, String ff, double f, double w,
                                      Pos p, double x, double y) {
        b.setFont(Font.font(ff, f));
        b.setMinWidth(w);
        b.setAlignment(p);
        b.setLayoutX(x);
        b.setLayoutY(y);
    }
}
