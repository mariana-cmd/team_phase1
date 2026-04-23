
	package guiGradePosts;

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
	 * <p> Title: ViewGradePosts Class. </p>
	 * 
	 * <p> Description: GUI for the staff's grading. </p>
	 * 

	 *  
	 */

	public class ViewGradePosts {
		
		/*-*******************************************************************************************

		Attributes
		
		 */
		
		// These are the application values required by the user interface
		
		private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
		private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


		// These are the widget attributes for the GUI. There are 3 areas for this GUI.
		
		// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
		// and a button to allow this user to update the account settings
		protected static Label label_PageTitle = new Label();
		protected static Label label_UserDetails = new Label();
		protected static Button button_UpdateThisUser = new Button("Account Update");
		
		// This is a separator and it is used to partition the GUI for various tasks
		protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

		// GUI ARea 2: This is a stub, so there are no widgets here.  For an actual role page, this are
		// would contain the widgets needed for the user to play the assigned role.
		
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
		
		// This is a separator and it is used to partition the GUI for various tasks
		protected static Line line_Separator4 = new Line(20, 525, width-20,525);
		
		// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
		// logging out.
	    private static Button   button_Back     = new Button("Back");

		// This is the end of the GUI objects for the page.
		
		// These attributes are used to configure the page and populate it with this user's information
		private static ViewGradePosts theView;		// Used to determine if instantiation of the class
													// is needed

		// Reference for the in-memory database so this package has access
		private static Database theDatabase = applicationMain.FoundationsMain.database;

		protected static Stage theStage;			// The Stage that JavaFX has established for us	
		protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
		protected static User theUser;				// The current logged in User
		

		private static Scene theViewGradePostsScene;	// The shared Scene each invocation populates
		protected static final int theRole = 3;		// Admin: 1; Student: 2; Staff: 3

		/*-*******************************************************************************************

		Constructors
		
		 */


		/**********
		 * <p> Method: displayRole1Home(Stage ps, User user) </p>
		 * 
		 * <p> Description: This method is the single entry point from outside this package to cause
		 * the Role1 Home page to be displayed.
		 * 
		 * It first sets up every shared attributes so we don't have to pass parameters.
		 * 
		 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
		 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
		 * size, and any methods to be performed).
		 * 
		 * After the instantiation, the code then populates the elements that change based on the user
		 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
		 * to the user.
		 * 
		 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
		 * 
		 * @param user specifies the User for this GUI and it's methods
		 * 
		 */
		public static void displayGradePosts(Stage ps, User user) {
			
			// Establish the references to the GUI and the current user
			theStage = ps;
			theUser = user;
			
			// If not yet established, populate the static aspects of the GUI
			if (theView == null) theView = new ViewGradePosts();		// Instantiate singleton if needed
			
			// Populate the dynamic aspects of the GUI with the data from the user and the current
			// state of the system.
			theDatabase.getUserAccountDetails(user.getUserName());
			applicationMain.FoundationsMain.activeHomePage = theRole;
			
			label_UserDetails.setText("User: " + theUser.getUserName());
					
			// Set the title for the window, display the page, and wait for the Admin to do something
			theStage.setTitle("CSE 360 Foundations: Staff Home Page");
			theStage.setScene(theViewGradePostsScene);
			theStage.show();
		}
		
		/**********
		 * <p> Method: ViewRole1Home() </p>
		 * 
		 * <p> Description: This method initializes all the elements of the graphical user interface.
		 * This method determines the location, size, font, color, and change and event handlers for
		 * each GUI object.</p>
		 * 
		 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
		 * fields using the displayRole2Home method.</p>
		 * 
		 */
		private ViewGradePosts() {

			// Create the Pane for the list of widgets and the Scene for the window
			theRootPane = new Pane();
			theViewGradePostsScene = new Scene(theRootPane, width, height);	// Create the scene
			
			// Set the title for the window
			
			// Populate the window with the title and other common widgets and set their static state
			
			// GUI Area 1
			label_PageTitle.setText("Grade Posts");
			setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

			label_UserDetails.setText("User: " + theUser.getUserName());
			setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
			
			setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
			button_UpdateThisUser.setOnAction((_) -> {ControllerGradePosts.performUpdate(); });

			
			 setupLabelUI(label_GradeSection, "Arial", 22, width - 40, Pos.BASELINE_LEFT, 20, 110);

			 setupButtonUI(button_GradePosts, "Dialog", 16, 140, Pos.CENTER, 640, 108);
		        button_GradePosts.setOnAction(evt -> {
		            evt.consume();
		            ControllerGradePosts.performGradePosts();
		        });

		        listView_Posts.setLayoutX(20);
		        listView_Posts.setLayoutY(150);
		        listView_Posts.setPrefWidth(460);
		        listView_Posts.setPrefHeight(250);
		        listView_Posts.setOnMouseClicked(evt -> {
		            evt.consume();
		            ControllerGradePosts.performSelectPost();
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
		            ControllerGradePosts.performSaveGrade();
		        });



			// GUI Area 3
	        setupButtonUI(button_Back, "Dialog", 18, 250, Pos.CENTER, 20, 540);
	        button_Back.setOnAction((_) -> {ControllerGradePosts.goBack(); });
	        
			// This is the end of the GUI initialization code
			
			// Place all of the widget items into the Root Pane's list of children
	        theRootPane.getChildren().addAll(
	        		label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1,
	                label_GradeSection, button_GradePosts, listView_Posts, label_SelectedPost,
	                label_Grade, textField_Grade, label_Feedback, textArea_Feedback,
	                button_SaveGrade, label_Status,
	                line_Separator4, button_Back
	        	);
	}
		
		
		/*-********************************************************************************************

		Helper methods to reduce code length

		 */
		
		/**********
		 * Private local method to initialize the standard fields for a label
		 * 
		 * @param l		The Label object to be initialized
		 * @param ff	The font to be used
		 * @param f		The size of the font to be used
		 * @param w		The width of the Button
		 * @param p		The alignment (e.g. left, centered, or right)
		 * @param x		The location from the left edge (x axis)
		 * @param y		The location from the top (y axis)
		 */
		private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, 
				double y){
			l.setFont(Font.font(ff, f));
			l.setMinWidth(w);
			l.setAlignment(p);
			l.setLayoutX(x);
			l.setLayoutY(y);		
		}
		
		
		/**********
		 * Private local method to initialize the standard fields for a button
		 * 
		 * @param b		The Button object to be initialized
		 * @param ff	The font to be used
		 * @param f		The size of the font to be used
		 * @param w		The width of the Button
		 * @param p		The alignment (e.g. left, centered, or right)
		 * @param x		The location from the left edge (x axis)
		 * @param y		The location from the top (y axis)
		 */
		private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, 
				double y){
			b.setFont(Font.font(ff, f));
			b.setMinWidth(w);
			b.setAlignment(p);
			b.setLayoutX(x);
			b.setLayoutY(y);		
		}
	}

