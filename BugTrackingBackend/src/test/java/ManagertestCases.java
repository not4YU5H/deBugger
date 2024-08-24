import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.Team;
import com.codefury.exception.*;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ManagertestCases {

    private BugTrackingService bugTrackingService;
    private Connection conn;

    @BeforeEach
    public void setUp() throws SQLException {
        String url = "jdbc:mysql://localhost:3306/codefury";
        String user = "root";
        String password = "samgeorge";
        conn = DriverManager.getConnection(url, user, password);
        bugTrackingService = new BugTrackingServiceImpl();

        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);

    }
//The test passed the first time but after which it wont as the team mebrs are already alloted
    @Test
    public void testCreateProject_Success() {
        // Arrange
        String name = "FOX";
        String desc = "Worlds Best AI";
        String stakeholders = "TATA";
        String clientName = "TATA";
        Double budget = 1231231123.0;
        String poc = "Shekhar";
        LocalDate startDate = LocalDate.now().plusDays(3); // A valid future date
        int teamId = 1;
        List<Integer> team = Arrays.asList(1, 2, 3, 4, 5);

        Project proj = new Project(1, name, desc, stakeholders, clientName, budget, poc, startDate, teamId);
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);

        // Act
        boolean result = false;
        try {
            result = bugTrackingService.createProject(token, proj, team);
        } catch (Exception e) {
            fail("Exception should not be thrown: " + e.getMessage());
        }

        // Assert
        assertTrue(result, "Project should be created successfully.");
    }

    @Test
    //This test will show error because manager does not have 4 or more projects assigned in db and the
    // devloper has 1 assigned so developer exception will be raised
    public void testCreateProject_ManagerMaxProjectException() {
        // Arrange
        String name = "FOX";
        String desc = "Worlds Best AI";
        String stakeholders = "TATA";
        String clientName = "TATA";
        Double budget = 1231231123.0;
        String poc = "Shekhar";
        LocalDate startDate = LocalDate.now().plusDays(3); // A valid future date
        int teamId = 1;
        List<Integer> team = Arrays.asList(1, 2, 3, 4, 5);

        Project proj = new Project(1, name, desc, stakeholders, clientName, budget, poc, startDate, teamId);
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1); // Token for a manager with already 4 projects

        // Act & Assert
        assertThrows(ManagerMaxProjectException.class, () -> {
            bugTrackingService.createProject(token, proj, team);
        }, "Should throw ManagerMaxProjectException for manager with 4 projects.");
    }

    @Test
    public void testCreateProject_ProjectStartDateException() {
        // Arrange
        String name = "FOX";
        String desc = "Worlds Best AI";
        String stakeholders = "TATA";
        String clientName = "TATA";
        Double budget = 1231231123.0;
        String poc = "Shekhar";
        LocalDate startDate = LocalDate.now().minusDays(2); // Invalid past date
        int teamId = 1;
        List<Integer> team = Arrays.asList(1, 2, 3, 4, 5);

        Project proj = new Project(1, name, desc, stakeholders, clientName, budget, poc, startDate, teamId);
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);

        // Act & Assert
        assertThrows(ProjectStartDateException.class, () -> {
            bugTrackingService.createProject(token, proj, team);
        }, "Should throw ProjectStartDateException for a start date that is not at least 2 days in the future.");
    }







    @Test
    public void testFetchProjectsManagedByManagerId_ValidToken() throws InvalidTokenException, NoAccessException, NoDataFoundException {
        // Assume bugTrackingService has a way to preload data for testing
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);
        List<Project> projects = bugTrackingService.fetchProjectsManagedByManagerId(token);

        assertNotNull(projects);
        assertEquals(2, projects.size());
        assertEquals("Project Phoenix", projects.get(0).getName());
        assertEquals("FOX", projects.get(1).getName());
    }

    @Test
    public void testFetchProjectsManagedByManagerId_InvalidToken() {
        String username = "davidbrown";
        String password1 = "devPass456";
        String token = bugTrackingService.login(username, password1);
        token ="asdasddasdadhakdhkasdaksjdhkadhkashdkasdhak1231313hdlasda11213";
        String finalToken = token;
        Exception exception = assertThrows(
                InvalidTokenException.class,
                () -> bugTrackingService.fetchProjectsManagedByManagerId(finalToken)
        );

        assertTrue(exception.getMessage().contains("Invalid token"));
    }

    @Test
    public void testFetchProjectDetails_ValidToken() throws InvalidTokenException, NoAccessException, NoDataFoundException {
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);
        Project projectDetails = bugTrackingService.fetchProjectDetails(token);

        assertNotNull(projectDetails);
        assertEquals("Project Phoenix", projectDetails.getName());
    }

    @Test
    public void testFetchBugsPerProjectId_ValidToken() throws InvalidTokenException, NoAccessException, NoDataFoundException {
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);
        List<Bug> bugs = bugTrackingService.fetchBugsPerProjectId(token);

        assertNotNull(bugs);
        assertFalse(bugs.isEmpty());
        assertEquals("Login Page Error", bugs.get(0).getBugName());
    }

    @Test
    public void testFetchRolesByTeamMemberId_ValidToken() throws InvalidTokenException, NoAccessException, NoDataFoundException {
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);
        Team teamRole = bugTrackingService.fetchRolesByTeamMemberId(token);

        assertNotNull(teamRole);
        assertEquals("Development Team Alpha", teamRole.getName());
    }

    @Test
    public void testAssignBugToDeveloper_Success() throws InvalidTokenException, NoAccessException {
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);
        int bugId = 1;
        int developerId = 2;
        boolean assignStatus = bugTrackingService.assignBugToDeveloper(token, bugId, developerId);

        assertTrue(assignStatus);
    }

    @Test
    public void testCloseBug_Success() throws InvalidTokenException, NoAccessException {
        String username = "george@1234";
        String password1 = "password123";
        String token = bugTrackingService.login(username, password1);
        boolean closeStatus = bugTrackingService.closeBug(token);

        assertTrue(closeStatus);
    }


}
