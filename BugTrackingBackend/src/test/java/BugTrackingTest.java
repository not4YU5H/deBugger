import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.Team;
import com.codefury.beans.User;
import com.codefury.exception.*;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BugTrackingTest {
    public static void main(String[] args) {

        BugTrackingService bugTrackingService = new BugTrackingServiceImpl();
        
        
        //Upload Json formatt for user details.
        //The function recieves the file path into it. It then does the manipulation and passes it on
        String filepath="src/main/java/users.json";
        if(filepath.contains(".json")){
            //It will give false because the users are already added once.
            //from the same file.
            boolean status = bugTrackingService.addUsersFromJson(filepath);
            System.out.println(status);
        }
        
        

        //Authentication and Authorization points
        //Sample login attempt
        String token = bugTrackingService.login("george@1234","password123");

        System.out.println(token);


        //fetching user information
        User user = null;
        try {
            user = bugTrackingService.fetchUserInfo(token);
        } catch (InvalidTokenException e) {
            e.getMessage();
        }
        System.out.println(user);

        //
        String name = "FOX";
        String desc = "Worlds Best AI";
        String stakeholders = "TATA";
        String ClientName = "TATA";
        Double Budget = 1231231123.0;
        String POC = "Shekhar";
        LocalDate startDate = LocalDate.now().plusDays(3);
        int teamid = 1;
        //List of user ids.
        List<Integer> team = new ArrayList<>(Arrays.asList(5, 6, 7, 8, 9));

        Project proj = new Project(1,name,desc,stakeholders,ClientName,Budget,POC,startDate,teamid);

        boolean stat = false;
        try {
            stat = bugTrackingService.createProject(token,proj,team);
        } catch (InvalidTokenException | ManagerMaxProjectException | ProjectStartDateException |
                 UserNotFoundException | TeamMemberException | NoAccessException e) {
            System.out.println(e.getMessage());
        }
        System.out.println(stat);


        // Fetch projects managed by the manager
        try {
            List<Project> projects = bugTrackingService.fetchProjectsManagedByManagerId(token);
            if (projects != null) {
                for (Project project : projects) {
                    System.out.println(project);
                }
            }
        } catch (InvalidTokenException e) {
            System.out.println(e.getMessage());
        } catch (NoAccessException | NoDataFoundException e) {
            throw new RuntimeException(e);
        }


        // Fetch project details
        try {
            Project projectDetails = bugTrackingService.fetchProjectDetails(token);
            if (projectDetails != null) {
                System.out.println(projectDetails);
            } else {
                System.out.println("No project details found or invalid token.");
            }
        } catch (InvalidTokenException | NoAccessException | NoDataFoundException e) {
            System.out.println("Error fetching project details: " + e.getMessage());
        }

        // Fetch bugs per project ID
        try {
            List<Bug> bugs = bugTrackingService.fetchBugsPerProjectId(token);
            if (bugs != null && !bugs.isEmpty()) {
                for (Bug bug : bugs) {
                    System.out.println(bug);
                }
            }
        } catch (InvalidTokenException | NoAccessException | NoDataFoundException e) {
            System.out.println(e.getMessage());
        }

        // Fetch roles by team member ID
        try {
            Team teamrole = bugTrackingService.fetchRolesByTeamMemberId(token);
            System.out.println(teamrole);
        } catch (InvalidTokenException | NoAccessException | NoDataFoundException e) {
            System.out.println(e.getMessage());
        }


        // Assign bug to developer
        try {
            int bugId = 1; // Placeholder
            int developerId = 2;
            boolean assignStatus = bugTrackingService.assignBugToDeveloper(token,bugId,developerId);
            System.out.println("Bug assigned to developer: " + assignStatus);
        } catch (InvalidTokenException | NoAccessException e) {
            System.out.println("Error assigning bug: " + e.getMessage());
        }

        // Close a bug
        try {
            boolean closeStatus = bugTrackingService.closeBug(token);
            System.out.println("Bug closed: " + closeStatus);
        } catch (InvalidTokenException | NoAccessException e) {
            System.out.println("Error closing bug: " + e.getMessage());
        }
    }
}

