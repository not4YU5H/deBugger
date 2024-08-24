import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.dao.BugTrackingDao;
import com.codefury.dao.BugTrackingDaoImpl;
import com.codefury.exception.BugNotFoundException;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.NoProjectsAssignedException;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;
import com.codefury.service.DeveloperService;
import com.codefury.service.DeveloperServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DeveloperServiceTest {

    private DeveloperService developerService;
    private BugTrackingService bugTrackingService;

    @Before
    public void setUp() {
        BugTrackingDao bugTrackingDao = new BugTrackingDaoImpl();
        developerService = new DeveloperServiceImpl(bugTrackingDao);
        bugTrackingService = new BugTrackingServiceImpl();
    }

    @Test
    public void testFetchProjectDetailsByUser() throws InvalidTokenException, NoProjectsAssignedException {
        // Sample login
        String token = bugTrackingService.login("davidbrown", "devPass456");

        // Fetch user info
        User user = bugTrackingService.fetchUserInfo(token);

        // Fetch project details
        List<Project> projects = developerService.fetchProjectDetailsByUser(token);

        // Assert that projects are fetched correctly
        assertNotNull("Projects list should not be null", projects);
        assertFalse("Projects list should not be empty", projects.isEmpty());

        // Print projects for verification
        for (Project project : projects) {
            assertNotNull("Project ID should not be null", project.getProjectId());
            assertNotNull("Project Name should not be null", project.getName());
        }
    }

    @Test
    public void testMarkBugForClose() {
        int bugId = 11; // Use an actual bug ID
        try {
            boolean result = developerService.markBugForClose(bugId);
            assertTrue("Bug should be marked as closed", result);
        } catch (BugNotFoundException e) {
            fail("Bug not found: " + e.getMessage());
        }
    }

    @Test(expected = InvalidTokenException.class)
    public void testInvalidTokenThrowsException() throws InvalidTokenException {
        bugTrackingService.fetchUserInfo("invalidToken");
    }
}
