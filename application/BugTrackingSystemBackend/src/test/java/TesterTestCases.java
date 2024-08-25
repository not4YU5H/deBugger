import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.NoAccessException;
import com.codefury.exception.ProjectIdNotFoundException;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;
import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TesterTestCases {
    private Connection conn;
    private BugTrackingService bugTrackingService = new BugTrackingServiceImpl();;
    private String username = "frankmiller";
    private String password = "testUser789";

    @BeforeEach
    public void setUp() throws SQLException {
        // Initialize your database connection here
        String url = "jdbc:mysql://localhost:3306/codefury";
        String user = "root";
        String password = "samgeorge";
        conn = DriverManager.getConnection(url, user, password);
        bugTrackingService  = new BugTrackingServiceImpl();
        String username = "root";
        String password1 = "root1234";
    }

    @After
    public void tearDown() throws SQLException {
        // Close the database connection
        if (conn != null && !conn.isClosed()) {
            conn.close();
        }
    }

    @Test
    public void testFetchProjectDetails() throws InvalidTokenException {
        String validToken = bugTrackingService.login(username, password);

        List<Project> projects = bugTrackingService.fetchAssignedProjectList(validToken);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());

        for (Project project : projects) {
            assertNotNull(project.getProjectId());
            assertNotNull(project.getName());
            assertNotNull(project.getStatus());
        }
    }

    @Test
    public void testFetchAssignedProjectList() throws InvalidTokenException {
        String validToken = bugTrackingService.login(username, password);
        List<Project> projects = bugTrackingService.fetchAssignedProjectList(validToken);
        assertNotNull(projects);
        assertFalse(projects.isEmpty());
    }

    @Test
    public void testReportNewBug() throws InvalidTokenException, SQLException {
        String validToken = bugTrackingService.login(username, password);
        String bugName = "Sample Bug";
        String bugDesc = "This is a test bug description";
        String securityLevel = "High";
        int projectId = 1;

        Bug newBug = bugTrackingService.reportNewBug(validToken, bugName, bugDesc, securityLevel, projectId);
        assertNotNull(newBug);
        assertEquals(bugName, newBug.getBugName());
        assertEquals(bugDesc, newBug.getBugDescription());
        assertEquals(securityLevel, newBug.getSecurityLevel());
    }

    @Test
    public void testFetchBugsByProjectID() throws SQLException, ProjectIdNotFoundException {
        int projectId = 1; // Set a valid project ID
        String validToken = bugTrackingService.login(username, password);

        List<Bug> bugs = new ArrayList<>();
        try{
            bugs = bugTrackingService.fetchBugsByProjectID(validToken,projectId);
        }catch (InvalidTokenException | NoAccessException e){

        }
        assertNotNull(bugs);
        assertFalse(bugs.isEmpty());

        for (Bug bug : bugs) {
            assertNotNull(bug.getBugId());
            assertNotNull(bug.getBugName());
            assertNotNull(bug.getBugDescription());
            assertNotNull(bug.getSecurityLevel());
        }
    }

}
