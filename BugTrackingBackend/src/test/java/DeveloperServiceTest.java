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
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


public class DeveloperServiceTest {

    BugTrackingDao bugTrackingDao = new BugTrackingDaoImpl();
    private DeveloperService developerService= new DeveloperServiceImpl(bugTrackingDao);
    private BugTrackingService bugTrackingService = new BugTrackingServiceImpl();


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
        assertNotNull((Object) "Projects list should not be null", (Supplier<String>) projects);
        assertFalse(projects.isEmpty(), "Projects list should not be empty");

        // Print projects for verification
        for (Project project : projects) {
            assertNotNull("Project ID should not be null", String.valueOf(project.getProjectId()));
            assertNotNull("Project Name should not be null", project.getName());
        }
    }

    @Test
    public void testMarkBugForClose() {
        int bugId = 11; // Use an actual bug ID
        try {
            boolean result = developerService.markBugForClose(bugId);
            assertTrue(result, "Bug should be marked as closed");
        } catch (BugNotFoundException e) {
            fail("Bug not found: " + e.getMessage());
        }
    }

    @Test
    public void testInvalidTokenThrowsException() throws InvalidTokenException {
        try{
            bugTrackingService.fetchUserInfo("invalidToken");

        }
        catch (InvalidTokenException e){
            assertEquals("Invalid Token", e.getMessage());

        }
    }
}
