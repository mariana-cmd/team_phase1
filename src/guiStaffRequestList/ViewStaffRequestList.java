package guiStaffRequestList;

import java.time.format.DateTimeFormatter;
import java.util.List;

import entityClasses.StaffRequest;
import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewStaffRequestList Class. </p>
 *
 * <p> Description: Displays a list of staff requests with Open/Closed toggle.
 * Admins see all requests; Role2 users see only their own.
 * Clicking a row navigates to the request detail screen.
 * Role2-only users (not admin) see a "New Request" button. </p>
 */
public class ViewStaffRequestList {

    // -------------------------------------------------------------------------
    // Shared references (set by displayStaffRequestList)
    // -------------------------------------------------------------------------
    protected static Stage theStage;
    protected static User  theUser;

    // -------------------------------------------------------------------------
    // Layout constants
    // -------------------------------------------------------------------------
    private static final double WIDTH  = applicationMain.FoundationsMain.WINDOW_WIDTH;
    private static final double HEIGHT = applicationMain.FoundationsMain.WINDOW_HEIGHT;

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // -------------------------------------------------------------------------
    // Singleton guard
    // -------------------------------------------------------------------------
    private static ViewStaffRequestList theView;

    // -------------------------------------------------------------------------
    // State — which tab is active
    // -------------------------------------------------------------------------
    /** true = showing OPEN requests, false = showing CLOSED requests */
    public static boolean showingOpen = true;

    // -------------------------------------------------------------------------
    // Widgets
    // -------------------------------------------------------------------------
    private static Label    label_Title        = new Label("Staff Requests");
    private static Button   button_Open        = new Button("Open Requests");
    private static Button   button_Closed      = new Button("Closed Requests");
    private static Line     line_Separator      = new Line(0, 70, WIDTH, 70);
    static ListView<String> listView_Requests  = new ListView<>();
    private static Button   button_NewRequest  = new Button("New Request");
    private static Button   button_Back        = new Button("Back");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**
     * Single external entry point. Sets up the singleton if needed, then
     * refreshes the list and displays the page.
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStaffRequestList(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffRequestList();

        refreshList();

        // Show [New Request] only for Role2 users who are NOT admins
        button_NewRequest.setVisible(theUser.getNewRole2() && !theUser.getAdminRole());

        theStage.setTitle("CSE 360 Foundations: Staff Requests");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton init)
    // -------------------------------------------------------------------------
    private ViewStaffRequestList() {
        theScene = new Scene(buildPane(), WIDTH, HEIGHT);
    }

    private static Pane buildPane() {
        Pane pane = new Pane();

        setupLabelUI(label_Title, "Arial", 26, WIDTH, Pos.CENTER, 0, 5);

        setupButtonUI(button_Open,   "Dialog", 16, 160, Pos.CENTER, 20,  42);
        setupButtonUI(button_Closed, "Dialog", 16, 160, Pos.CENTER, 190, 42);

        button_Open.setOnAction(_   -> ControllerStaffRequestList.showOpen());
        button_Closed.setOnAction(_ -> ControllerStaffRequestList.showClosed());

        listView_Requests.setLayoutX(20);
        listView_Requests.setLayoutY(80);
        listView_Requests.setPrefWidth(760);
        listView_Requests.setPrefHeight(440);
        listView_Requests.setOnMouseClicked(e -> {
            if (e.getClickCount() == 2) {
                ControllerStaffRequestList.openSelected();
            }
        });

        setupButtonUI(button_NewRequest, "Dialog", 16, 160, Pos.CENTER, 20,  550);
        setupButtonUI(button_Back,       "Dialog", 16, 130, Pos.CENTER, 650, 550);

        button_NewRequest.setOnAction(_ -> ControllerStaffRequestList.goToNewRequest());
        button_Back.setOnAction(_       -> ControllerStaffRequestList.goBack());

        pane.getChildren().addAll(
                label_Title,
                button_Open, button_Closed,
                line_Separator,
                listView_Requests,
                button_NewRequest,
                button_Back);

        return pane;
    }

    // -------------------------------------------------------------------------
    // List population
    // -------------------------------------------------------------------------

    /**
     * Rebuilds the ListView contents based on the current tab (open/closed)
     * and the user's role (admin sees all, Role2 sees own).
     */
    static void refreshList() {
        listView_Requests.getItems().clear();

        List<StaffRequest> source = showingOpen
                ? applicationMain.FoundationsMain.staffRequestList.getOpenRequests()
                : applicationMain.FoundationsMain.staffRequestList.getClosedRequests();

        int rowNum = 1;
        for (StaffRequest r : source) {
            // Admins see all; Role2 users only see their own
            if (!theUser.getAdminRole() &&
                    !r.getCreatedByUsername().equals(theUser.getUserName())) {
                continue;
            }
            String date = r.getCreatedAt().format(DATE_FMT);
            String desc = r.getDescription();
            String preview = desc.length() > 60 ? desc.substring(0, 60) + "..." : desc;
            String row = "[#" + rowNum + "] " + date
                    + " | by: " + r.getCreatedByUsername()
                    + " | " + preview;
            listView_Requests.getItems().add(row);
            rowNum++;
        }
    }

    // -------------------------------------------------------------------------
    // Package-private helpers used by Controller
    // -------------------------------------------------------------------------

    /** @return the index selected in the list (-1 if none) */
    static int getSelectedIndex() {
        return listView_Requests.getSelectionModel().getSelectedIndex();
    }

    // -------------------------------------------------------------------------
    // UI helper methods
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
