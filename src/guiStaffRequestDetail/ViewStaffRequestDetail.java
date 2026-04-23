package guiStaffRequestDetail;

import java.time.format.DateTimeFormatter;

import entityClasses.StaffRequest;
import entityClasses.User;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/*******
 * <p> Title: ViewStaffRequestDetail Class. </p>
 *
 * <p> Description: Displays all fields of a single StaffRequest.
 * Context-sensitive buttons:
 *   [Close Request]  — Admin + OPEN only
 *   [Reopen Request] — Role2 owner + CLOSED only
 *   [View Original]  — any user when linkedRequestId != null
 *   [Back]           — always visible
 * </p>
 */
public class ViewStaffRequestDetail {

    // -------------------------------------------------------------------------
    // Navigation state — set by caller before invoking displayStaffRequestDetail
    // -------------------------------------------------------------------------
    /** The request to display. Must be set before calling displayStaffRequestDetail. */
    public static StaffRequest currentRequest;

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

    private static final DateTimeFormatter DATE_FMT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // -------------------------------------------------------------------------
    // Singleton guard
    // -------------------------------------------------------------------------
    private static ViewStaffRequestDetail theView;

    // -------------------------------------------------------------------------
    // Widgets — header
    // -------------------------------------------------------------------------
    private static Label label_PageTitle  = new Label("Request Detail");
    private static Label label_Status     = new Label();
    private static Label label_SubmittedBy = new Label();
    private static Label label_Created    = new Label();
    private static Line  line_Sep1        = new Line(20, 100, WIDTH - 20, 100);
    private static Label label_DescHdr    = new Label("Description:");
    private static Label label_Desc       = new Label();

    // -------------------------------------------------------------------------
    // Widgets — closed section (shown only when CLOSED)
    // -------------------------------------------------------------------------
    private static Line  line_Sep2        = new Line(20, 320, WIDTH - 20, 320);
    private static Label label_ActionHdr  = new Label("Admin Action:");
    private static Label label_Action     = new Label();
    private static Label label_ClosedBy   = new Label();
    private static Label label_ClosedAt   = new Label();

    // -------------------------------------------------------------------------
    // Widgets — action buttons
    // -------------------------------------------------------------------------
    private static Button button_Close    = new Button("Close Request");
    private static Button button_Reopen   = new Button("Reopen Request");
    private static Button button_Original = new Button("View Original");
    private static Button button_Back     = new Button("Back");

    // -------------------------------------------------------------------------
    // Scene
    // -------------------------------------------------------------------------
    private static Scene theScene;

    // -------------------------------------------------------------------------
    // Entry point
    // -------------------------------------------------------------------------

    /**
     * Single external entry point. Populates all dynamic fields from
     * currentRequest and refreshes button visibility based on user role / status.
     *
     * @param ps   the JavaFX Stage
     * @param user the currently logged-in user
     */
    public static void displayStaffRequestDetail(Stage ps, User user) {
        theStage = ps;
        theUser  = user;

        if (theView == null) theView = new ViewStaffRequestDetail();

        refreshDetail();

        theStage.setTitle("CSE 360 Foundations: Request Detail");
        theStage.setScene(theScene);
        theStage.show();
    }

    // -------------------------------------------------------------------------
    // Constructor (singleton init)
    // -------------------------------------------------------------------------
    private ViewStaffRequestDetail() {
        theScene = new Scene(buildPane(), WIDTH, HEIGHT);
    }

    private static Pane buildPane() {
        Pane pane = new Pane();

        setupLabelUI(label_PageTitle,  "Arial", 26, WIDTH, Pos.CENTER, 0, 5);
        setupLabelUI(label_Status,     "Arial", 16, 400, Pos.BASELINE_LEFT, 20, 40);
        setupLabelUI(label_SubmittedBy,"Arial", 16, 400, Pos.BASELINE_LEFT, 20, 60);
        setupLabelUI(label_Created,    "Arial", 16, 400, Pos.BASELINE_LEFT, 20, 80);

        setupLabelUI(label_DescHdr,    "Arial", 15, 200, Pos.BASELINE_LEFT, 20, 110);
        label_Desc.setLayoutX(20);
        label_Desc.setLayoutY(130);
        label_Desc.setMaxWidth(760);
        label_Desc.setMaxHeight(180);
        label_Desc.setWrapText(true);
        label_Desc.setFont(Font.font("Arial", 14));

        // Closed section
        setupLabelUI(label_ActionHdr, "Arial", 15, 200, Pos.BASELINE_LEFT, 20, 330);
        label_Action.setLayoutX(20);
        label_Action.setLayoutY(350);
        label_Action.setMaxWidth(760);
        label_Action.setMaxHeight(100);
        label_Action.setWrapText(true);
        label_Action.setFont(Font.font("Arial", 14));

        setupLabelUI(label_ClosedBy, "Arial", 14, 400, Pos.BASELINE_LEFT, 20, 460);
        setupLabelUI(label_ClosedAt, "Arial", 14, 400, Pos.BASELINE_LEFT, 20, 480);

        // Action buttons
        setupButtonUI(button_Close,    "Dialog", 16, 180, Pos.CENTER, 20,  550);
        setupButtonUI(button_Reopen,   "Dialog", 16, 180, Pos.CENTER, 20,  550);
        setupButtonUI(button_Original, "Dialog", 16, 180, Pos.CENTER, 220, 550);
        setupButtonUI(button_Back,     "Dialog", 16, 130, Pos.CENTER, 650, 550);

        button_Close.setOnAction(   _ -> ControllerStaffRequestDetail.closeRequest());
        button_Reopen.setOnAction(  _ -> ControllerStaffRequestDetail.reopenRequest());
        button_Original.setOnAction(_ -> ControllerStaffRequestDetail.viewOriginal());
        button_Back.setOnAction(    _ -> ControllerStaffRequestDetail.goBack());

        pane.getChildren().addAll(
                label_PageTitle, label_Status, label_SubmittedBy, label_Created,
                line_Sep1,
                label_DescHdr, label_Desc,
                line_Sep2, label_ActionHdr, label_Action,
                label_ClosedBy, label_ClosedAt,
                button_Close, button_Reopen, button_Original, button_Back);

        return pane;
    }

    // -------------------------------------------------------------------------
    // Refresh — called from entry point and after state changes
    // -------------------------------------------------------------------------

    /**
     * Repopulates all labels and recalculates button visibility from currentRequest.
     */
    static void refreshDetail() {
        StaffRequest r = currentRequest;

        label_Status.setText("Status: " + r.getStatus());
        label_SubmittedBy.setText("Submitted by: " + r.getCreatedByUsername());
        label_Created.setText("Created: " + r.getCreatedAt().format(DATE_FMT));
        label_Desc.setText(r.getDescription());

        boolean closed = r.isClosed();

        // Closed-section visibility
        line_Sep2.setVisible(closed);
        label_ActionHdr.setVisible(closed);
        label_Action.setVisible(closed);
        label_ClosedBy.setVisible(closed);
        label_ClosedAt.setVisible(closed);

        if (closed) {
            label_Action.setText(r.getAdminActionDescription() != null
                    ? r.getAdminActionDescription() : "");
            label_ClosedBy.setText("Closed by: " + r.getClosedByUsername());
            label_ClosedAt.setText("Closed: " + r.getClosedAt().format(DATE_FMT));
        }

        // Button visibility logic
        boolean isAdmin   = theUser.getAdminRole();
        boolean isRole2   = theUser.getNewRole2();
        boolean isOwner   = r.getCreatedByUsername().equals(theUser.getUserName());
        boolean hasLinked = r.getLinkedRequestId() != null;

        button_Close.setVisible(isAdmin && r.isOpen());
        button_Reopen.setVisible(!isAdmin && isRole2 && isOwner && closed);
        button_Original.setVisible(hasLinked);
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
