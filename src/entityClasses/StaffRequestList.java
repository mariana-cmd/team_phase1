package entityClasses;

import java.util.ArrayList;
import java.util.List;

/*******
 * <p> Title: StaffRequestList Class </p>
 *
 * <p> Description: In-memory collection of all StaffRequest objects. Provides
 * filtered views (open / closed) and lookup by ID. Returns defensive copies so
 * callers cannot mutate the internal list directly. </p>
 */
public class StaffRequestList {

    // -------------------------------------------------------------------------
    // Fields
    // -------------------------------------------------------------------------

    /** Master list of every staff request known to the application. */
    private final List<StaffRequest> allRequests = new ArrayList<>();

    // -------------------------------------------------------------------------
    // Constructor
    // -------------------------------------------------------------------------

    /** Default constructor. */
    public StaffRequestList() {}

    // -------------------------------------------------------------------------
    // Mutation
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: addRequest(StaffRequest request) </p>
     *
     * <p> Description: Appends a request to the master list. </p>
     *
     * @param request the StaffRequest to add; must not be null
     * @throws IllegalArgumentException if request is null
     */
    public void addRequest(StaffRequest request) {
        if (request == null) {
            throw new IllegalArgumentException("Request must not be null.");
        }
        allRequests.add(request);
    }

    // -------------------------------------------------------------------------
    // Lookup
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: getRequestById(String requestId) </p>
     *
     * <p> Description: Returns the request with the given ID. </p>
     *
     * @param requestId the UUID string to look up
     * @return the matching StaffRequest
     * @throws IllegalArgumentException if no request with that ID exists
     */
    public StaffRequest getRequestById(String requestId) {
        for (StaffRequest r : allRequests) {
            if (r.getRequestId().equals(requestId)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No staff request found with ID: " + requestId);
    }

    // -------------------------------------------------------------------------
    // Filtered views (defensive copies)
    // -------------------------------------------------------------------------

    /*****
     * <p> Method: getOpenRequests() </p>
     *
     * @return a new list containing only OPEN requests, in insertion order
     */
    public List<StaffRequest> getOpenRequests() {
        List<StaffRequest> result = new ArrayList<>();
        for (StaffRequest r : allRequests) {
            if (r.isOpen()) result.add(r);
        }
        return result;
    }

    /*****
     * <p> Method: getClosedRequests() </p>
     *
     * @return a new list containing only CLOSED requests, in insertion order
     */
    public List<StaffRequest> getClosedRequests() {
        List<StaffRequest> result = new ArrayList<>();
        for (StaffRequest r : allRequests) {
            if (r.isClosed()) result.add(r);
        }
        return result;
    }

    /*****
     * <p> Method: getAllRequests() </p>
     *
     * @return a defensive copy of the full request list
     */
    public List<StaffRequest> getAllRequests() {
        return new ArrayList<>(allRequests);
    }
}
