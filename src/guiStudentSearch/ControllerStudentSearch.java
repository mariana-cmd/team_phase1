package guiStudentSearch;

/*******
 * <p> Title: ControllerStudentSearch Class. </p>
 *
 * <p> Description: Controller for the Search page. Delegates keyword search
 * to the shared PostList and triggers a View refresh. </p>
 *
 * @author Gunbir Singh
 *
 * @version 1.00  2026-03-25 Initial version
 */
public class ControllerStudentSearch {

    /** Default constructor — not used directly. */
    public ControllerStudentSearch() {}

    /**********
     * <p> Method: goBack() </p>
     *
     * <p> Description: Returns the user to the Student Home Page. </p>
     */
    protected static void goBack() {
        guiRole1.ViewRole1Home.displayRole1Home(
            ViewStudentSearch.theStage, ViewStudentSearch.theUser);
    }

    /**********
     * <p> Method: performSearch(String keyword) </p>
     *
     * <p> Description: Calls postList.searchByKeyword and refreshes the result
     * ListView. If keyword is blank, shows an error message. </p>
     *
     * Student US-5: Search posts by keyword.
     *
     * @param keyword the search term typed by the student
     */
    protected static void performSearch(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            ViewStudentSearch.showError("Please enter a keyword to search.");
            return;
        }
        try {
            applicationMain.FoundationsMain.postList.searchByKeyword(keyword);
            ViewStudentSearch.refreshResults();
            ViewStudentSearch.showError(""); // clear any previous error
        } catch (IllegalArgumentException e) {
            ViewStudentSearch.showError(e.getMessage());
        }
    }

    /**********
     * <p> Method: openPost(entityClasses.Post post) </p>
     *
     * <p> Description: Navigates to the View Post page for the selected result. </p>
     *
     * @param post the post to open
     */
    protected static void openPost(entityClasses.Post post) {
        guiStudentViewPost.ViewStudentViewPost.currentPost = post;
        guiStudentViewPost.ViewStudentViewPost.displayStudentViewPost(
            ViewStudentSearch.theStage, ViewStudentSearch.theUser);
    }
}
