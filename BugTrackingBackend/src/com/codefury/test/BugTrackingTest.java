package com.codefury.test;

import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;
import com.codefury.service.BugTrackingService;
import com.codefury.service.BugTrackingServiceImpl;

public class BugTrackingTest {
    public static void main(String[] args) {

        BugTrackingService bugTrackingService = new BugTrackingServiceImpl();
        
        
        //Upload Json formatt for user details.
        //The function recieves the file path into it. It then does the manipulation and passes it on
        String filepath="src/users.json";
        if(filepath.contains(".json")){
            //It will give false because the users are already added once.
            //from the same file.
            boolean status = bugTrackingService.addUsersFromJson(filepath);
            System.out.println(status);
        }
        
        
        

        //Authentication and Authorization points
        //Sample login attempt
        String token = bugTrackingService.login("johndoe","password123");


        User user = null;
        try {
            user = bugTrackingService.fetchUserInfo(token);
        } catch (InvalidTokenException e) {
            e.getMessage();
        }
        System.out.println(user);
        
        


       
        

    }
}
