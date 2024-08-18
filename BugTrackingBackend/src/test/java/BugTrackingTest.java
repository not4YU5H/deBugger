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
        String filepath = "users.json";
        if (filepath.contains("src/main/java/users.json")) {
            //It will give false because the users are already added once.
            //from the same file.
            boolean status = bugTrackingService.addUsersFromJson(filepath);
            System.out.println(status);
        }


        //Authentication and Authorization points
        //Sample login attempt
        String token = bugTrackingService.login("johndoe", "password123");


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
            }
        } catch (InvalidTokenException e) {
            System.out.println("Error fetching projects: " + e.getMessage());
        }

    }
}
