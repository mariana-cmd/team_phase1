package guiStaffRequestList;

import java.util.List;

import entityClasses.StaffRequest;

/*******
 * <p> Title: ControllerStaffRequestList Class. </p>
 *
 * <p> Description: Handles all user interactions on the staff request list page.
 * All methods are protected static — only invoked by the View. </p>
 */
public class ControllerStaffRequestList {

    /** Default constructor not used. */
    public ControllerStaffRequestList() {}

    /**
     * Switches the list to show OPEN requests.
     */
    protected static void showOpen() {
        ViewStaffRequestList.showingOpen = true;
        ViewStaffRequestList.refreshList();
    }

    /**
     * Switches the list to show CLOSED requests.
     */
    protected static void showClosed() {
        ViewStaffRequestList.showingOpen = false;
        ViewStaffRequestList.refreshList();
    }

    /**
     * Opens the detail page for the selected request.
     * Ignores the click if no row is selected.
     */
    protected static void openSelected() {
        int idx = ViewStaffRequestList.getSelectedIndex();
        if (idx < 0) return;

        List<StaffRequest> source = ViewStaffRequestList.showingOpen
                ? applicationMain.FoundationsMain.staffRequestList.getOpenRequests()
                : applicationMain.FoundationsMain.staffRequestList.getClosedRequests();

        // Filter to match what the list actually shows (admin sees all, Role2 sees own)
        java.util.List<StaffRequest> visible = new java.util.ArrayList<>();
        for (StaffRequest r : source) {
            if (ViewStaffRequestList.theUser.getAdminRole() ||
                    r.getCreatedByUsername().equals(ViewStaffRequestList.theUser.getUserName())) {
                visible.add(r);
            }
        }

        if (idx >= visible.size()) return;

        guiStaffRequestDetail.ViewStaffRequestDetail.currentRequest = visible.get(idx);
        guiStaffRequestDetail.ViewStaffRequestDetail.displayStaffRequestDetail(
                ViewStaffRequestList.theStage, ViewStaffRequestList.theUser);
    }

    /**
     * Navigates to the New Request creation form (Role2 only).
     */
    protected static void goToNewRequest() {
        guiStaffCreateRequest.ViewStaffCreateRequest.displayStaffCreateRequest(
                ViewStaffRequestList.theStage, ViewStaffRequestList.theUser);
    }

    /**
     * Returns to the appropriate home page based on the user's role.
     */
    protected static void goBack() {
        if (ViewStaffRequestList.theUser.getAdminRole()) {
            guiAdminHome.ViewAdminHome.displayAdminHome(
                    ViewStaffRequestList.theStage, ViewStaffRequestList.theUser);
        } else {
            guiRole2.ViewRole2Home.displayRole2Home(
                    ViewStaffRequestList.theStage, ViewStaffRequestList.theUser);
        }
    }
}
