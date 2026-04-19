package guiAdminHome;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import database.Database;

/**
 * junit tests for controlleradminhome. these tests use mockito to mock the database and
 * replace the javafx alerts with lightweight test alerts that do not block. depending on
 * your environment you may need to run these tests with javafx availability (or adapt them
 * to run with testfx / a headless javafx toolkit).
 */
public class TestControllerAdminHome {

    // lightweight alert used in tests. note: we do not override showandwait (it's final);
    // these alerts are used primarily for their content text and title fields in assertions.
    static class TestAlert extends javafx.scene.control.Alert {
        public TestAlert(javafx.scene.control.Alert.AlertType t) {
            super(t);
        }
    }

    @BeforeEach
    public void setup() throws Exception {
        // create a simple fakedatabase and inject it into controlleradminhome.thedatabase
        FakeDatabase fakeDb = new FakeDatabase();
        Field dbField = ControllerAdminHome.class.getDeclaredField("theDatabase");
        dbField.setAccessible(true);
        dbField.set(null, fakeDb);

        // enable test mode so controller does not call blocking showandwait()
        Field testModeField = ControllerAdminHome.class.getDeclaredField("testMode");
        testModeField.setAccessible(true);
        testModeField.setBoolean(null, true);

        // reset any test-captured fields
        ControllerAdminHome.testLastAlertContent = null;
        ControllerAdminHome.testLastAlertTitle = null;
        ControllerAdminHome.testLastAlertHeader = null;
        ControllerAdminHome.testText_InvitationEmailAddress = null;
        ControllerAdminHome.testLabel_NumberOfInvitations = null;
        ControllerAdminHome.testLabel_NumberOfUsers = null;

        // replace alerts in viewadminhome with test alerts created on the javafx application thread
        CountDownLatch latch = new CountDownLatch(1);
        javafx.application.Platform.runLater(() -> {
            ViewAdminHome.alertEmailError = new TestAlert(javafx.scene.control.Alert.AlertType.INFORMATION);
            ViewAdminHome.alertEmailSent = new TestAlert(javafx.scene.control.Alert.AlertType.INFORMATION);
            ViewAdminHome.alertNotImplemented = new TestAlert(javafx.scene.control.Alert.AlertType.INFORMATION);
            latch.countDown();
        });
        // wait for the fx thread to construct alerts (timeout to avoid hanging tests)
        if (!latch.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout waiting for JavaFX to create test alerts");
        }

        // initialize ui widgets used by controller methods so they are non-null
        CountDownLatch latch2 = new CountDownLatch(1);
        javafx.application.Platform.runLater(() -> {
            // ensure combobox has at least the default items and a selection
            ViewAdminHome.combobox_SelectRole.getItems().clear();
            ViewAdminHome.combobox_SelectRole.getItems().addAll("Admin", "Student", "Instructor");
            ViewAdminHome.combobox_SelectRole.getSelectionModel().select(0);
            // ensure labels/text fields exist
            ViewAdminHome.text_InvitationEmailAddress.setText("");
            ViewAdminHome.label_NumberOfInvitations.setText("");
            ViewAdminHome.label_NumberOfUsers.setText("");
            latch2.countDown();
        });
        if (!latch2.await(5, TimeUnit.SECONDS)) {
            throw new RuntimeException("Timeout waiting for JavaFX to initialize UI widgets");
        }
    }

    @BeforeAll
    public static void initJavaFX() {
        // initialize javafx toolkit for tests that interact with javafx controls/alerts
        try {
            javafx.application.Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // toolkit already initialized; ignore
        }
    }

    // helper to obtain the injected mock database
    private Database getInjectedDatabase() throws Exception {
        Field dbField = ControllerAdminHome.class.getDeclaredField("theDatabase");
        dbField.setAccessible(true);
        return (Database) dbField.get(null);
    }

    /**
     * simple test double for database to avoid mockito.
     * only methods used by these tests are overridden.
     */
    static class FakeDatabase extends Database {
        public boolean emailUsed = false;
        public String lastGeneratedCode = null;
        public int invitations = 0;
        public java.util.List<String> users = Collections.emptyList();
        public java.util.List<Database.UserDetails> userDetails = Collections.emptyList();
        public boolean generateInvitationCodeCalled = false;

        @Override
        public boolean emailaddressHasBeenUsed(String emailAddress) {
            return emailUsed;
        }

        @Override
        public String generateInvitationCode(String emailAddress, String role) {
            generateInvitationCodeCalled = true;
            lastGeneratedCode = lastGeneratedCode != null ? lastGeneratedCode : "FAKE01";
            return lastGeneratedCode;
        }

        @Override
        public int getNumberOfInvitations() {
            return invitations;
        }

        @Override
        public java.util.List<String> getUserList() {
            // return a mutable copy so callers can remove the placeholder
            return new java.util.ArrayList<>(users);
        }

        @Override
        public java.util.List<Database.UserDetails> getAllUserDetails() {
            return userDetails;
        }
    }

    @Test
    public void testPerformInvitation_invalidEmail_showsEmailError() throws Exception {
        // arrange: blank email triggers emailrecognizer error
        ViewAdminHome.text_InvitationEmailAddress.setText("");

        // act
        ControllerAdminHome.performInvitation();

        // debug output to help diagnose ci/ide failures
        System.out.println("testLastAlertContent: '" + ControllerAdminHome.testLastAlertContent + "'");

        // assert: alert content captured and database was not asked to generate a code
        assertNotNull(ControllerAdminHome.testLastAlertContent);
        assertTrue(ControllerAdminHome.testLastAlertContent.startsWith("Invalid email address"),
            "Unexpected alert content: " + ControllerAdminHome.testLastAlertContent);
        // ensure the fake database did not get asked to generate a code
        FakeDatabase fd = (FakeDatabase) getInjectedDatabase();
        assertFalse(fd.generateInvitationCodeCalled);
    }

    @Test
    public void testPerformInvitation_emailAlreadyUsed_showsError() throws Exception {
        // arrange
        FakeDatabase db = (FakeDatabase) getInjectedDatabase();
        db.emailUsed = true;
        ViewAdminHome.text_InvitationEmailAddress.setText("a@bc.com");
        ViewAdminHome.combobox_SelectRole.setValue("Student");

        // act
        ControllerAdminHome.performInvitation();

        // debug
        System.out.println("testLastAlertContent: '" + ControllerAdminHome.testLastAlertContent + "'");

        // assert (use contains to be tolerant of formatting differences)
        assertNotNull(ControllerAdminHome.testLastAlertContent);
        assertTrue(ControllerAdminHome.testLastAlertContent.contains("An invitation has already been sent to this email address."),
            "Unexpected alert content: " + ControllerAdminHome.testLastAlertContent);
    }

    @Test
    public void testPerformInvitation_success_updatesLabelAndClearsText() throws Exception {
        // arrange
        FakeDatabase db = (FakeDatabase) getInjectedDatabase();
        db.emailUsed = false;
        db.lastGeneratedCode = "ABC123";
        db.invitations = 5;

        ViewAdminHome.text_InvitationEmailAddress.setText("a@bc.com");
        ViewAdminHome.combobox_SelectRole.setValue("Student");

        // act
        ControllerAdminHome.performInvitation();

        // debug
        System.out.println("testText_InvitationEmailAddress: '" + ControllerAdminHome.testText_InvitationEmailAddress + "'");
        System.out.println("testLabel_NumberOfInvitations: '" + ControllerAdminHome.testLabel_NumberOfInvitations + "'");
        System.out.println("testLastAlertContent: '" + ControllerAdminHome.testLastAlertContent + "'");

        // assert
        assertEquals("", ControllerAdminHome.testText_InvitationEmailAddress);
        assertTrue(ControllerAdminHome.testLabel_NumberOfInvitations.contains("5"),
            "Unexpected invitations label: " + ControllerAdminHome.testLabel_NumberOfInvitations);
        assertNotNull(ControllerAdminHome.testLastAlertContent);
        assertTrue(ControllerAdminHome.testLastAlertContent.contains("ABC123"),
            "Unexpected alert content: " + ControllerAdminHome.testLastAlertContent);
    }

    @Test
    public void testSetOnetimePassword_noUsers_showsNoUsersAlert() throws Exception {
        // arrange: database returns only the placeholder
        FakeDatabase db = (FakeDatabase) getInjectedDatabase();
        db.users = Collections.singletonList("<Select a User>");

        // act
        ControllerAdminHome.setOnetimePassword();

        // debug
        System.out.println("testLastAlertContent: '" + ControllerAdminHome.testLastAlertContent + "'");

        // assert (tolerant)
        assertNotNull(ControllerAdminHome.testLastAlertContent);
        assertTrue(ControllerAdminHome.testLastAlertContent.contains("No users available."),
            "Unexpected alert content: " + ControllerAdminHome.testLastAlertContent);
    }

    @Test
    public void testListUsers_formatsCorrectly() throws Exception {
        // arrange: create one userdetails with admin and role2
        Database.UserDetails ud = new Database.UserDetails();
        ud.username = "u";
        ud.firstName = "F";
        ud.middleName = "";
        ud.lastName = "L";
        ud.email = "e@e.com";
        ud.adminRole = true;
        ud.role1 = false;
        ud.role2 = true;

        FakeDatabase db = (FakeDatabase) getInjectedDatabase();
        db.userDetails = Arrays.asList(ud);

        // act
        ControllerAdminHome.listUsers();

        // assert
        assertNotNull(ControllerAdminHome.testLastAlertContent);
        String content = ControllerAdminHome.testLastAlertContent;
        assertTrue(content.contains("Username: u"));
        assertTrue(content.contains("Roles: Admin, Staff"));
    }
}
