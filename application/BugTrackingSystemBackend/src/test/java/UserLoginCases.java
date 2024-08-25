import static org.junit.jupiter.api.Assertions.*;

import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class UserLoginCases {
    private BugTrackingService bugTrackingService;

    private String username = "george@1234";
    private String password = "password123";

    @BeforeEach
    public void setUp() {
        bugTrackingService = new BugTrackingServiceImpl();
        // Add any necessary setup or initialization
    }

    @Test
    public void testLoginWithValidCredentials() {
        String token = bugTrackingService.login(username, password);

        assertNotNull(token);
        assertEquals(344, token.length()); // 256 bits = 32 bytes = 64 hex characters
        // You may also add other checks if there's a specific pattern the token should follow
    }

    @Test
    public void testFetchUserInfoWithValidToken() throws InvalidTokenException {
        String token = bugTrackingService.login(username, password);
        User user = null;

        try {
            user = bugTrackingService.fetchUserInfo(token);
        } catch (InvalidTokenException e) {
            fail("Exception should not be thrown for a valid token");
        }

        assertNotNull(user);
        assertEquals(username, user.getUsername());
    }

    @Test
    public void testFetchUserInfoWithInvalidToken() {
        String invalidToken = "invalidToken123"; // Example invalid token for testing
        User user = null;

        try {
            user = bugTrackingService.fetchUserInfo(invalidToken);
            fail("InvalidTokenException should have been thrown");
        } catch (InvalidTokenException e) {
            assertEquals("Invalid Token", e.getMessage());
        }

        assertNull(user);
    }

    @Test
    public void testLoginWithInvalidCredentials() {
        String token = bugTrackingService.login(username, "wrongPassword");
        assertNull(token);
    }
}
