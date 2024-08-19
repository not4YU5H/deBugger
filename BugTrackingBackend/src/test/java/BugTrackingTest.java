import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;

import java.util.List;

public class BugTrackingTest {
    public static void main(String[] args) {

        BugTrackingService bugTrackingService = new BugTrackingServiceImpl();


        //Upload Json formatt for user details.
        //The function recieves the file path into it. It then does the manipulation and passes it on
        String filepath="users.json";
        if(filepath.contains("src/main/java/users.json")){
            //It will give false because the users are already added once.
            //from the same file.
            boolean status = bugTrackingService.addUsersFromJson(filepath);
            System.out.println(status);
        }



        //Authentication and Authorization points
        //Sample login attempt
        String token = bugTrackingService.login("johndoe","password123");



        //fetching user information
        User user = null;
        try {
            user = bugTrackingService.fetchUserInfo(token);
        } catch (InvalidTokenException e) {
            e.getMessage();
        }
        System.out.println(user);

        // Fetch projects managed by the manager
        try {
            List<Project> projects = bugTrackingService.fetchProjectsManagedByManagerId(token);
            if (projects != null) {
                for (Project project : projects) {
                    System.out.println("Project ID: " + project.getProjectId());
                    System.out.println("Name: " + project.getName());
                    System.out.println("Description: " + project.getDescription());
                    System.out.println("Start Date: " + project.getStartDate());
                    System.out.println("Status: " + project.getStatus());
                    System.out.println("-----------");
                }
            } else {
                System.out.println("No projects found or invalid token.");
            }
        } catch (InvalidTokenException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }



        // Fetch project details
        try {
            Project projectDetails = bugTrackingService.fetchProjectDetails(token);
            if (projectDetails != null) {
                System.out.println("Project Details:");
                System.out.println("Project ID: " + projectDetails.getProjectId());
                System.out.println("Name: " + projectDetails.getName());
                System.out.println("Description: " + projectDetails.getDescription());
                System.out.println("Start Date: " + projectDetails.getStartDate());
                System.out.println("Status: " + projectDetails.getStatus());
            } else {
                System.out.println("No project details found or invalid token.");
            }
        } catch (InvalidTokenException e) {
            System.out.println("Error fetching project details: " + e.getMessage());
        }

        // Fetch bugs per project ID
        try {
            List<Bug> bugs = bugTrackingService.fetchBugsPerProjectId(token);
            if (bugs != null && !bugs.isEmpty()) {
                System.out.println("Bugs for the project:");
                for (Bug bug : bugs) {
                    System.out.println("Bug ID: " + bug.getBugId());
                    System.out.println("Title: " + bug.getBugName());
                    System.out.println("Description: " + bug.getBugDescription());
                    System.out.println("Created On: " + bug.getCreatedOn());
                    System.out.println("Status: " + bug.getStatus());
                    System.out.println("-----------");
                }
            } else {
                System.out.println("No bugs found for the project or invalid token.");
            }
        } catch (InvalidTokenException e) {
            System.out.println("Error fetching bugs: " + e.getMessage());
        }

        // Fetch roles by team member ID
        try {
            List<String> roles = bugTrackingService.fetchRolesByTeamMemberId(token);
            if (roles != null) {
                for (String role : roles) {
                    System.out.println(role);
                }
            } else {
                System.out.println("No roles found or invalid token.");
            }
        } catch (InvalidTokenException e) {
            System.out.println("Error fetching roles: " + e.getMessage());
        }


        // Assign bug to developer
        try {
            boolean assignStatus = bugTrackingService.assignBugToDeveloper(token);
            System.out.println("Bug assigned to developer: " + assignStatus);
        } catch (InvalidTokenException e) {
            System.out.println("Error assigning bug: " + e.getMessage());
        }

        // Close a bug
        try {
            boolean closeStatus = bugTrackingService.closeBug(token);
            System.out.println("Bug closed: " + closeStatus);
        } catch (InvalidTokenException e) {
            System.out.println("Error closing bug: " + e.getMessage());
        }
    }
}

