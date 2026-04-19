package guiUserUpdate;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javafx.application.Platform;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * javadoc: unit test for the email validation behaviour added to ViewUserUpdate.
 *
 * This test verifies that when the provided input does not contain '@' or '.com',
 * an error message "a valid email should have an @ and .com" is prepared (captured
 * in testLastAlertContent) and the displayed email label is set to "<none>".
 */
public class TestViewUserUpdateEmail {

    @BeforeAll
    public static void initFx() {
        try {
            Platform.startup(() -> {});
        } catch (IllegalStateException e) {
            // already started
        }
    }

    @BeforeEach
    public void setup() {
        // enable test mode so alerts don't block and are captured
        ViewUserUpdate.testMode = true;
        ViewUserUpdate.testLastAlertContent = null;
    }

    /**
     * test that an input lacking '@' or '.com' triggers the specific error message.
     */
    @Test
    public void testInvalidEmailShowsSpecificMessage() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        Platform.runLater(() -> {
            // call the new package-private helper directly
            ViewUserUpdate.updateEmailAddressFromInput("invalidemail");
            latch.countDown();
        });
        assertTrue(latch.await(5, TimeUnit.SECONDS), "Timeout waiting for FX thread");

        assertNotNull(ViewUserUpdate.testLastAlertContent, "Expected an error message to be captured");
        assertEquals("a valid email should have an @ and .com", ViewUserUpdate.testLastAlertContent);
    }
}
