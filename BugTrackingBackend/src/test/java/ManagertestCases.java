import com.codefury.beans.Project;
import com.codefury.exception.ManagerMaxProjectException;
import com.codefury.exception.ProjectStartDateException;
import com.codefury.exception.TeamMemberException;
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
        String url = "jdbc:mysql://54.197.170.44:3306/codefury";
        String user = "root";
        String password = "codefury";
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
    public void testCreateProject_TeamMemberException() {
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

        // Assume that user ID 3 is a Developer with 1 assigned project and user ID 4 is a Tester with 2 assigned projects
        // Act & Assert
        assertThrows(TeamMemberException.class, () -> {
            bugTrackingService.createProject(token, proj, team);
        }, "Should throw TeamMemberException if a Developer or Tester is already assigned to the maximum allowed projects.");
    }
}
