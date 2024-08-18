package com.codefury.test;

import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;

public class BugTrackingTest {
    public static void main(String[] args) {

        BugTrackingService bugTrackingService = new BugTrackingServiceImpl();
        //Authentication and Authorization points







        //Sample login attempt
        String token = bugTrackingService.login("johndoe","password123");
        System.out.println(token);
        int authorization = bugTrackingService.fetchUsers(token);
        System.out.println(authorization);// this will print 2 for the time being as john doe is set as Developer



        //Upload Json formatt for user details.
        //The function recieves the file path into it. It then does the manipulation and passes it on
        String filepath="src/users.json";
        if(filepath.contains(".json")){

            //It will give false because the users are already added once.
            //from the same file.
            boolean status = bugTrackingService.addUsersFromJson(filepath);
            System.out.println(status);
        }

    }
}
