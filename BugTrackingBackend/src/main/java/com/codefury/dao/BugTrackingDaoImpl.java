package com.codefury.dao;

import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.beans.Bug;
import com.codefury.exception.InvalidTokenException;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BugTrackingDaoImpl implements BugTrackingDao{
    private Connection conn;//Connection to DB
    private EncryptionUtil encryption;//Holds the encryption values
    private HashMap<String, LocalDateTime> tokenvalidity;//Holds the token and validity statements
    //The hashmap is stored in ram so that if the application shutsdown new tokens are generated.

    public BugTrackingDaoImpl() {
        this.conn = DBUtil.getMyConnection();
        this.encryption = new EncryptionUtil();
        this.tokenvalidity = new HashMap<>();
    }


    private static boolean compareTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        // Calculate the duration between the two LocalDateTime instances
        Duration duration = Duration.between(dateTime1, dateTime2);
        // 10 minutes in seconds
        long tenMinutes = 10 * 60;
        // Return true if the difference is greater than 10 minutes
        return Math.abs(duration.getSeconds()) > tenMinutes;
    }
    //This function checks the Authorization levels of the user based on the token passed on from the user
    //Use this function always to check for level
    private List<Object> isAuthorized(String token) throws InvalidTokenException {
        //This compares the time the token was created and the current time now to check if the token is stil valid
        //if the time stamp shows more than 10 min it gets removed from the hashmap
        //If not it moves to the authorization checks
        for(String tok :tokenvalidity.keySet()){
            if(tok.equals(token) && compareTime(tokenvalidity.get(tok),LocalDateTime.now())){
                tokenvalidity.remove(tok);
            }
        }
        if(!tokenvalidity.containsKey(token)){
            throw new InvalidTokenException("Invalid Token");
        }

        String auth;
        int id;
        List<Object> res = new ArrayList<>();

        try {
            //Splitting the decrypted string back to different strings
            auth = encryption.decrypt(token).split(",")[2];
            id = Integer.parseInt(encryption.decrypt(token).split(",")[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

            if(auth.equals("Admin")){
                res.add(0);
                res.add(id);

            }
            else if(auth.equals("Tester")){
                res.add(1);
                res.add(id);
            }
            else if (auth.equals("Developer")) {
                res.add(2);
                res.add(id);
            }
            else{
                throw new InvalidTokenException("Token has No access to the system.");

            }
            return res;

    }
    //Login function returns a token string which is an encrypted string of data back to the user.
    //This helps us authorize easily by just decrypting the string and dont have to raise a db call everytime.
    //Expiry time also can be set to the token as well.





    //Main methods for Data begins from here.
    @Override
    public String login(String username, String password) {
        try {
            PreparedStatement pst = conn.prepareStatement("select * from USERS where username=? and password=?;");
            pst.setString(1,username);
            pst.setString(2,password);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                try {
                    int id = rs.getInt("USERID");
                    //Localdate.now() is used to create more randomness in the string to be encrypted. It will be almost impossible to guess the server time to the millisecond
                    String tokenization =id+","+rs.getString("NAME")+","+rs.getString("ROLE")+","+ LocalDateTime.now();
                    String token = encryption.encrypt(tokenization);
                    tokenvalidity.put(token,LocalDateTime.now());

                    //After login we have to update last logged in.
                    pst = conn.prepareStatement("update USERS set LAST_LOGGED_IN_DATETIME=? where USERID=?");
                    pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setInt(2,id);


                    return token;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            else{
                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }



    @Override
    public boolean addUsersFromJson(List<User> users) {

        for(User user:users){
            String insertUsers = "INSERT INTO USERS (PASSWORD, EMAIL, USERNAME, NAME, ADDRESS, JOIN_DATE, CONTACT_NUMBER, DOB, GENDER, PROFILE_PICTURE_URL, ROLE, ASSIGNED_PROJECTS, LAST_LOGGED_IN_DATETIME) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(insertUsers)) {
                ps.setString(1, user.getPassword());
                ps.setString(2, user.getEmail());
                ps.setString(3, user.getUsername());
                ps.setString(4, user.getName());
                ps.setString(5, user.getAddress());
                ps.setDate(6, java.sql.Date.valueOf(user.getJoinDate().toString()));
                ps.setString(7, user.getContactNumber());
                ps.setDate(8, java.sql.Date.valueOf(user.getDob().toString()));
                ps.setString(9, user.getGender());
                ps.setString(10, user.getProfilePictureUrl());
                ps.setString(11, user.getRole());
                ps.setInt(12, user.getAssignedProjects());
                ps.setTimestamp(13, java.sql.Timestamp.valueOf(user.getLastLoggedInDatetime()));
                ps.executeUpdate();
            } catch (SQLException e) {
                return false;
            }

        }
        return true;
    }


    @Override
    public User fetchUserInfo(String token) throws InvalidTokenException {
        int auth = (int) isAuthorized(token).get(0);
        int id = (int) isAuthorized(token).get(1);
        if(auth==0||auth==1||auth==2){
            PreparedStatement pst = null;
            try {
                pst = conn.prepareStatement("select * from USERS where USERID=?;");
                pst.setInt(1,id);
                ResultSet rs = pst.executeQuery();
                User user = new User();
                if(rs.next()){
                    user.setUsername(rs.getString("USERNAME"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setLastLoggedInDatetime(rs.getTimestamp("LAST_LOGGED_IN_DATETIME").toLocalDateTime());
                    user.setRole(rs.getString("ROLE"));
                    user.setProfilePictureUrl(rs.getString("PROFILE_PICTURE_URL"));
                    user.setAssignedProjects(rs.getInt("ASSIGNED_PROJECTS"));
                }
                return user;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return null;
    }


    @Override
    public List<Project> fetchProjectsManagedByManagerId(String token) throws InvalidTokenException {
        int auth = (int) isAuthorized(token).get(0);
        int managerId = (int) isAuthorized(token).get(1);

        if (auth == 0) {  // Admin level access
            List<Project> projects = new ArrayList<>();
            String query = "SELECT * FROM PROJECT WHERE TEAM_ID IN (SELECT TEAMID FROM TEAMS WHERE MANAGER_ID=?)";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, managerId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getInt("PROJECTID"));
                    project.setName(rs.getString("NAME"));
                    project.setDescription(rs.getString("DESCRIPTION"));
                    project.setStartDate(rs.getDate("START_DATE").toLocalDate());
                    project.setStatus(rs.getString("STATUS"));
                    projects.add(project);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return projects;
        }
        return null;
    }


    @Override
    public Project fetchProjectDetails(String token) throws InvalidTokenException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if (authLevel == 0) { // Assuming Admin has access
            String query = "SELECT P.NAME, P.START_DATE, U.NAME AS MANAGER_NAME FROM PROJECT P JOIN TEAMS T ON P.TEAM_ID = T.TEAMID JOIN USERS U ON T.MANAGER_ID = U.USERID WHERE T.MANAGER_ID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();
                if (rs.next()) {
                    Project project = new Project();
                    //project.setProjectId(rs.getInt("PROJECTID"));
                    project.setName(rs.getString("NAME"));
                    project.setStartDate(rs.getDate("START_DATE").toLocalDate());
                    //ALSO DISPLAY MANAGER NAME
                   // project.setStatus(rs.getString("STATUS"));
                    return project;
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public List<String> fetchRolesByTeamMemberId(String token) throws InvalidTokenException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if (authLevel == 0) { // Assuming only Admin can fetch roles by team member ID
            List<String> roles = new ArrayList<>();
            String query = "SELECT t.NAME AS TEAM_NAME, u.NAME AS USER_NAME, u.ROLE " +
                    "FROM TEAMS t " +
                    "JOIN USERS u ON t.TEAMID = u.ASSIGNED_PROJECTS " +
                    "WHERE t.MANAGER_ID = ?;";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    String roleInfo = "Team: " + rs.getString("TEAM_NAME") +
                            ", Member: " + rs.getString("USER_NAME") +
                            ", Role: " + rs.getString("ROLE");
                    roles.add(roleInfo);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return roles;
        }
        return null;
    }


    @Override
    public List<Bug> fetchBugsPerProjectId(String token) throws InvalidTokenException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if (authLevel == 0) { // Assuming Admin has access
            List<Bug> bugs = new ArrayList<>();
            String query = "SELECT * FROM BUGS WHERE PROJECT_ID = (SELECT PROJECTID FROM TEAMS WHERE MANAGER_ID = ?)";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    Bug bug = new Bug();
                    bug.setBugId(rs.getInt("BUG_ID"));
                    bug.setProjectId(rs.getInt("PROJECT_ID"));
                    bug.setBugName(rs.getString("BUG_NAME"));
                    bug.setBugDescription(rs.getString("BUG_DESCRIPTION"));
                    bug.setCreatedBy(rs.getInt("CREATEDBY"));
                    bug.setCreatedOn(rs.getTimestamp("CREATEDON").toLocalDateTime());
                    bug.setImageUrls(rs.getString("IMAGE_URLS"));
                    bug.setStatus(rs.getString("STATUS"));
                    bug.setSecurityLevel(rs.getString("SECURITY_LEVEL"));
                    bugs.add(bug);
                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            return bugs;
        }
        return null;
    }

    @Override
    public boolean assignBugToDeveloper(String token) throws InvalidTokenException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if (authLevel == 0) { // Assuming Admin has access
            // Assuming input will come from somewhere, for example, a method parameter
            int bugId = 1; // Placeholder
            int developerId = 2; // Placeholder

            String query = "UPDATE BUGS SET CREATEDBY = ? WHERE BUG_ID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, developerId);
                pst.setInt(2, bugId);
                int rowsAffected = pst.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean closeBug(String token) throws InvalidTokenException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if (authLevel == 0) { // Assuming Admin has access
            // Assuming input will come from somewhere, for example, a method parameter
            int bugId = 1; // Placeholder

            String query = "UPDATE BUGS SET STATUS = 'Closed' WHERE BUG_ID = ?";
            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, bugId);
                int rowsAffected = pst.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return false;
    }




}
