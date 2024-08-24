package com.codefury.dao;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.ProjectIdNotFoundException;


import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BugTrackingDaoImpl implements BugTrackingDao{
    private Connection conn;//Connection to DB
    private com.codefury.dao.EncryptionUtil encryption;//Holds the encryption values
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
        if(auth == 0 || auth == 1 || auth ==2){
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

    //TESTER DASHBOARD VIEW PROJECT DETAILS

    //fetchProjectDetails() method is used for fetching all the project details to a particular tester
    @Override
    public List<Project> fetchProjectDetails(String token) throws InvalidTokenException{
        int auth = (int) isAuthorized(token).get(0);
        int id = (int) isAuthorized(token).get(1);
        if(auth == 2) { // Assuming '2' indicates a tester role
            PreparedStatement pst = null;
            List<Project> projects = new ArrayList<>();

            try {
                // Fetch projects assigned to the tester
                pst = conn.prepareStatement("SELECT PROJECT.PROJECTID, PROJECT.NAME, PROJECT.STATUS FROM PROJECT JOIN TEAMS ON PROJECT.TEAM_ID = TEAMS.TEAMID WHERE FIND_IN_SET(?, TEAMS.TEAM_MEMBERS);");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getInt("PROJECTID"));
                    project.setName(rs.getString("NAME"));
                    project.setStatus(rs.getString("STATUS"));
                    projects.add(project);
                }
                return projects;
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }
        return null;
    }

    //fetchAssignedProjectList() method is used for fetching list of the project details to a particular tester
    public List<Project> fetchAssignedProjectList(String token) throws InvalidTokenException {
        List<Project> assignedProjects = fetchProjectDetails(token);
        return assignedProjects;
    }

    //REPORT NEW BUG METHOD
    public Bug reportNewBug(String token, String bugName, String bugDesc, String securityLevel, int projectId) throws InvalidTokenException,SQLException {
        int auth = (int) isAuthorized(token).get(0);
        int userId = (int) isAuthorized(token).get(1);

        if (auth == 2) {
            PreparedStatement pst = null;
            ResultSet rs = null;
            Bug bug = new Bug();

            // Insert the new bug into the BUGS table
            pst = conn.prepareStatement(
                    "INSERT INTO BUGS (PROJECT_ID, BUG_NAME, BUG_DESCRIPTION, CREATEDBY, CREATEDON, STATUS, SECURITY_LEVEL) " +
                            "VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, 'Open', ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setInt(1, projectId);
            pst.setString(2, bugName);
            pst.setString(3, bugDesc);
            pst.setInt(4, userId);
            pst.setString(5, securityLevel);
            pst.executeUpdate();

            // Get the generated bug ID
            rs = pst.getGeneratedKeys();
            int bugId = 0;
            if (rs.next()) {
                bugId = rs.getInt(1);
                bug.setBugId(bugId);
            }

            bug.setBugName(bugName);
            bug.setBugDescription(bugDesc);
            bug.setSecurityLevel(securityLevel);

            //display the new bug created
            displayBugDetails(bug);
            return bug;
        }
        else {
            throw new InvalidTokenException("User is not authorized to report bugs.");
        }
    }

    //DISPLAY BUG
    private void displayBugDetails(Bug bug) {
        System.out.println("Bug Reported Successfully!");
        System.out.println("Project ID: " + bug.getProjectId());
        System.out.println("Bug ID: " + bug.getBugId());
        System.out.println("Bug Name: " + bug.getBugName());
        System.out.println("Bug Description: " + bug.getBugDescription());
        System.out.println("Severity Level: " + bug.getSecurityLevel());
        System.out.println("Created By (User ID): " + User.getUserId());
        System.out.println("Created On: " + LocalDateTime.now());
    }

    //VIEW BUG
    public List<Bug> fetchBugsByProjectID(int projectId) throws SQLException, ProjectIdNotFoundException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Bug> bugsList = new ArrayList<>();

        // Fetch all bugs for the given project ID from the BUGS table
        String bugQuery = "SELECT b.BUG_ID, b.PROJECT_ID, b.BUG_NAME, b.BUG_DESCRIPTION, b.CREATEDBY, b.CREATEDON, " +
                "b.STATUS, b.SECURITY_LEVEL, p.NAME as PROJECT_NAME " +
                "FROM BUGS b " +
                "JOIN PROJECT p ON b.PROJECT_ID = p.PROJECTID " +
                "WHERE b.PROJECT_ID = ?";
        pst = conn.prepareStatement(bugQuery);
        pst.setInt(1, projectId);
        rs = pst.executeQuery();

        // Check if any bugs exist for the given project ID
        boolean hasBugs = false;
        while (rs.next()) {
            hasBugs = true;

            Bug bug = new Bug();
            bug.setBugId(rs.getInt("BUG_ID"));
            bug.setProjectId(rs.getInt("PROJECT_ID"));
            bug.setBugName(rs.getString("BUG_NAME"));
            bug.setBugDescription(rs.getString("BUG_DESCRIPTION"));
            bug.setSecurityLevel(rs.getString("SECURITY_LEVEL"));
            bug.setCreatedBy(rs.getInt("CREATEDBY"));

            // Convert SQL Timestamp to LocalDateTime and then to Date
            LocalDateTime createdOnLDT = rs.getTimestamp("CREATEDON").toLocalDateTime();
            Date createdOnDate = (Date) Date.from(createdOnLDT.atZone(ZoneId.systemDefault()).toInstant());
            bug.setCreatedOn(createdOnDate);

            // Add the bug to the list
            bugsList.add(bug);
        }

        // If no bugs were found
        if (!hasBugs) {
            throw new ProjectIdNotFoundException("No bugs found for Project ID: " + projectId);
        }

        // Close resources
        if (rs != null) rs.close();
        if (pst != null) pst.close();

        // Return the list of bugs
        return bugsList;
    }
}
