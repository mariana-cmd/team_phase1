package guiStaffRequestDetail;

import java.util.Optional;

import entityClasses.StaffRequest;
import javafx.scene.control.TextInputDialog;

/*******
 * <p> Title: ControllerStaffRequestDetail Class. </p>
 *
 * <p> Description: Handles all user interactions on the request detail page.
 * All methods are protected static — only invoked by the View. </p>
 */
public class ControllerStaffRequestDetail {

    /** Default constructor not used. */
    public ControllerStaffRequestDetail() {}

    /**
     * Admin closes the current request. Shows a TextInputDialog for the action
     * description, then persists the change and refreshes the detail view.
     */
    protected static void closeRequest() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Close Request");
        dialog.setHeaderText("Describe the action taken:");
        dialog.setContentText("Action:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        String actionDesc = result.get().trim();
        if (actionDesc.isEmpty()) return;

        StaffRequest r = ViewStaffRequestDetail.currentRequest;
        try {
            r.close(ViewStaffRequestDetail.theUser.getUserName(), actionDesc);
        } catch (IllegalStateException | IllegalArgumentException e) {
            // Already closed or invalid — nothing to do
            return;
        }

        applicationMain.FoundationsMain.database.updateStaffRequest(r);
        ViewStaffRequestDetail.refreshDetail();
    }

    /**
     * Role2 owner reopens a closed request. Shows a TextInputDialog pre-filled
     * with the original description. Creates a NEW StaffRequest linked to the
     * original, persists it, and switches the detail view to the new request.
     */
    protected static void reopenRequest() {
        StaffRequest original = ViewStaffRequestDetail.currentRequest;

        TextInputDialog dialog = new TextInputDialog(original.getDescription());
        dialog.setTitle("Reopen Request");
        dialog.setHeaderText("Edit description for the new request:");
        dialog.setContentText("Description:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) return;

        String newDescription = result.get().trim();
        if (newDescription.isEmpty()) return;

        StaffRequest reopened;
        try {
            reopened = new StaffRequest(
                    ViewStaffRequestDetail.theUser.getUserName(),
                    newDescription);
        } catch (IllegalArgumentException e) {
            return;
        }
        reopened.setLinkedRequestId(original.getRequestId());

        applicationMain.FoundationsMain.staffRequestList.addRequest(reopened);
        try {
            applicationMain.FoundationsMain.database.saveStaffRequest(reopened);
        } catch (java.sql.SQLException e) {
            e.printStackTrace();
        }

        // Switch to showing the new (OPEN) request
        ViewStaffRequestDetail.currentRequest = reopened;
        ViewStaffRequestDetail.refreshDetail();
    }

    /**
     * Navigates to the original request that this request was reopened from.
     */
    protected static void viewOriginal() {
        String linkedId = ViewStaffRequestDetail.currentRequest.getLinkedRequestId();
        try {
            StaffRequest original =
                    applicationMain.FoundationsMain.staffRequestList.getRequestById(linkedId);
            ViewStaffRequestDetail.currentRequest = original;
            ViewStaffRequestDetail.refreshDetail();
        } catch (IllegalArgumentException e) {
            // Original not found — nothing to do
        }
    }

    /**
     * Returns to the staff request list page.
     */
    protected static void goBack() {
        guiStaffRequestList.ViewStaffRequestList.displayStaffRequestList(
                ViewStaffRequestDetail.theStage, ViewStaffRequestDetail.theUser);
    }
}
