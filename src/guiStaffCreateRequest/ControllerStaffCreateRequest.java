package guiStaffCreateRequest;

import entityClasses.StaffRequest;

/*******
 * <p> Title: ControllerStaffCreateRequest Class. </p>
 *
 * <p> Description: Handles submit and cancel actions on the New Staff Request
 * form. All methods are protected static — only invoked by the View. </p>
 */
public class ControllerStaffCreateRequest {

    /** Default constructor not used. */
    public ControllerStaffCreateRequest() {}

    /**
     * Validates input, creates a new StaffRequest, persists it, and navigates
     * back to the request list.
     */
    protected static void submitRequest() {
        String description = ViewStaffCreateRequest.text_Description.getText();

        StaffRequest newRequest;
        try {
            newRequest = new StaffRequest(
                    ViewStaffCreateRequest.theUser.getUserName(),
                    description);
        } catch (IllegalArgumentException e) {
            ViewStaffCreateRequest.showError(e.getMessage());
            return;
        }

        applicationMain.FoundationsMain.staffRequestList.addRequest(newRequest);

        try {
            applicationMain.FoundationsMain.database.saveStaffRequest(newRequest);
        } catch (java.sql.SQLException e) {
            ViewStaffCreateRequest.showError("Database error: " + e.getMessage());
            return;
        }

        // Return to the list, defaulting to OPEN view
        guiStaffRequestList.ViewStaffRequestList.showingOpen = true;
        guiStaffRequestList.ViewStaffRequestList.displayStaffRequestList(
                ViewStaffCreateRequest.theStage, ViewStaffCreateRequest.theUser);
    }

    /**
     * Cancels request creation and returns to the staff request list.
     */
    protected static void cancel() {
        guiStaffRequestList.ViewStaffRequestList.displayStaffRequestList(
                ViewStaffCreateRequest.theStage, ViewStaffCreateRequest.theUser);
    }
}
