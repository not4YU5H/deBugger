package com.codefury.service;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.Team;
import com.codefury.beans.User;
import com.codefury.dao.BugTrackingDao;
import com.codefury.dao.StorageFactory;
//jar files have tp be manually downloaded from the internet
import com.codefury.exception.*;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class BugTrackingServiceImpl implements BugTrackingService{
    private BugTrackingDao bugTrackingDao;
    public BugTrackingServiceImpl() {
        this.bugTrackingDao = StorageFactory.getConnection();

    }


    @Override
    public String login(String username, String password) {
        return bugTrackingDao.login(username,password);
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

    @Override
    public User fetchUserInfo(String token) throws InvalidTokenException {

        return bugTrackingDao.fetchUserInfo(token);
    }

    @Override
    public boolean createProject(String token, Project proj, List<Integer> team) throws InvalidTokenException, ManagerMaxProjectException, ProjectStartDateException, TeamMemberException, NoAccessException, UserNotFoundException {
        return bugTrackingDao.createProject(token,proj,team);
    }


    //Sakshi code

    public List<Project> fetchProjectsManagedByManagerId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {

        return bugTrackingDao.fetchProjectsManagedByManagerId(token);
    }

    @Override
    public Project fetchProjectDetails(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        return bugTrackingDao.fetchProjectDetails(token);
    }

    @Override
    public Team fetchRolesByTeamMemberId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        return bugTrackingDao.fetchRolesByTeamMemberId(token);
    }

    @Override
    public List<Bug> fetchBugsPerProjectId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        return bugTrackingDao.fetchBugsPerProjectId(token);
    }

    @Override
    public boolean assignBugToDeveloper(String token,int bugId,int developerId) throws InvalidTokenException, NoAccessException {
        return bugTrackingDao.assignBugToDeveloper(token,bugId,developerId);
    }

    @Override
    public boolean closeBug(String token) throws InvalidTokenException, NoAccessException {
        return bugTrackingDao.closeBug(token);
    }


    //Tester


    @Override
    public List<Project> fetchAssignedProjectList(String token) throws InvalidTokenException {
        return bugTrackingDao.fetchAssignedProjectList(token);
    }

    @Override
    public Bug reportNewBug(String token, String bugName, String bugDesc, String securityLevel, int projectId) throws InvalidTokenException, SQLException {
        return bugTrackingDao.reportNewBug(token,bugName,bugDesc,securityLevel,projectId);
    }

    @Override
    public List<Bug> fetchBugsByProjectID(String token,int projectId) throws SQLException, ProjectIdNotFoundException, InvalidTokenException, NoAccessException {
        return bugTrackingDao.fetchBugsByProjectID(token,projectId);
    }



}
