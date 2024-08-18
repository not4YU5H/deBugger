package com.codefury.service;

import com.codefury.beans.User;
import com.codefury.dao.BugTrackingDao;
import com.codefury.dao.StorageFactory;
//jar files have tp be manually downloaded from the internet
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BugTrackingServiceImpl implements BugTrackingService{
    private BugTrackingDao bugTrackingDao;
    public BugTrackingServiceImpl() {
        this.bugTrackingDao = StorageFactory.getConnection();

    }


    @Override
    public String login(String username, String password) {
        return bugTrackingDao.login(username,password);
    }

    @Override
    public int fetchUsers(String token) {
        return bugTrackingDao.fetchUsers(token);
    }

    public static List<User> parseJson(String content) {
        List<User> users = new ArrayList<>();
        // Parse the string into a JSON array
        JSONArray jsonArray = new JSONArray(content);

        // Iterate over the JSON array and convert each object to a User
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);


            User user = new User();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Define the date-time format
            DateTimeFormatter formatter1 = DateTimeFormatter.ISO_LOCAL_DATE;
            user.setPassword(jsonObject.getString("password"));
            user.setEmail(jsonObject.getString("email"));
            user.setUsername(jsonObject.getString("username"));
            user.setName(jsonObject.getString("name"));
            user.setAddress(jsonObject.getString("address"));
            user.setJoinDate(LocalDate.parse(jsonObject.getString("joinDate"),formatter1));
            user.setContactNumber(jsonObject.getString("contactNumber"));
            user.setDob(LocalDate.parse(jsonObject.getString("dob")));
            user.setGender(jsonObject.getString("gender"));
            user.setProfilePictureUrl(jsonObject.getString("profilePictureUrl"));
            user.setRole(jsonObject.getString("role"));
            user.setAssignedProjects(jsonObject.getInt("assignedProjects"));
            user.setLastLoggedInDatetime(LocalDateTime.parse(jsonObject.getString("lastLoggedInDatetime"),formatter));

            // Add the user to the list
            users.add(user);
        }

        return users;
    }

    public static String readFile(String filePath) {
        StringBuilder jsonData = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                jsonData.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonData.toString();
    }
    @Override
    public boolean addUsersFromJson(String filepath) {
        String jsonData = readFile(filepath);

        List<User> users = null;
        if (jsonData != null) {
            users = parseJson(jsonData);

        } else {
            System.out.println("Failed to read JSON data from file.");
        }
        return bugTrackingDao.addUsersFromJson(users);
    }


}
