import static org.junit.jupiter.api.Assertions.*;

import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

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
    public void testInsertUser() {
        // Arrange
        User user = new User();
        user.setPassword("password123");
        user.setEmail("user@example.com");
        user.setUsername("testuser");
        user.setName("Test User");
        user.setAddress("123 Test Street");
        user.setJoinDate(LocalDate.of(2024, 8, 24));
        user.setContactNumber("1234567890");
        user.setDob(LocalDate.of(1990, 1, 1));
        user.setGender("Male");
        user.setUserCreationTime(new java.util.Date());
        user.setProfilePictureUrl("http://example.com/profile.jpg");
        user.setRole("Developer");
        user.setAssignedProjects(5);
        user.setLastLoggedInDatetime(null);

        // Act
        boolean result = bugTrackingService.register(user);

        // Assert
        assertTrue(result, "The register method should return true for a successful insert.");
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
