package com.codefury.dao;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.Team;
import com.codefury.beans.User;
import com.codefury.exception.*;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class BugTrackingDaoImpl implements BugTrackingDao {
    private Connection conn;//Connection to DB
    private EncryptionUtil security;//Holds the encryption values
    private HashMap<String, LocalDateTime> tokenvalidity;//Holds the token and validity statements
    //The hashmap is stored in ram so that if the application shutsdown new tokens are generated.

    public BugTrackingDaoImpl() {
        this.conn = DBUtil.getMyConnection();
        this.security = new EncryptionUtil();
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
        for (String tok : tokenvalidity.keySet()) {
            if (tok.equals(token) && compareTime(tokenvalidity.get(tok), LocalDateTime.now())) {
                tokenvalidity.remove(tok);
            }
        }
        if (!tokenvalidity.containsKey(token)) {
            security.log("Tried Signing in with expired/illegal token "+LocalDateTime.now().toString(), Level.WARNING);
            throw new InvalidTokenException("Invalid Token");
        }

        String auth;
        int id;
        List<Object> res = new ArrayList<>();

        try {
            //Splitting the decrypted string back to different strings
            auth = security.decrypt(token).split(",")[2];
            id = Integer.parseInt(security.decrypt(token).split(",")[0]);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (auth.equals("Manager")) {
            res.add(0);
            res.add(id);

        } else if (auth.equals("Tester")) {
            res.add(1);
            res.add(id);
        } else if (auth.equals("Developer")) {
            res.add(2);
            res.add(id);
        } else {
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
            pst.setString(1, username);
            pst.setString(2, password);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                try {
                    int id = rs.getInt("USERID");
                    //Localdate.now() is used to create more randomness in the string to be encrypted. It will be almost impossible to guess the server time to the millisecond
                    String tokenization = id + "," + rs.getString("NAME") + "," + rs.getString("ROLE") + "," + LocalDateTime.now();
                    String token = security.encrypt(tokenization);
                    tokenvalidity.put(token, LocalDateTime.now());

                    //After login we have to update last logged in.
                    pst = conn.prepareStatement("update USERS set LAST_LOGGED_IN_DATETIME=? where USERID=?");
                    pst.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
                    pst.setInt(2, id);


                    return token;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                security.log("Tried Signing in with wrong username/password username = "+username+" "+LocalDateTime.now().toString(), Level.SEVERE);

                return null;
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);

        }


    }


    @Override
    public boolean addUsersFromJson(List<User> users) {

        for (User user : users) {
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
        if (auth == 0 || auth == 1 || auth == 2) {
            PreparedStatement pst = null;
            try {
                pst = conn.prepareStatement("select * from USERS where USERID=?;");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();
                User user = new User();
                if (rs.next()) {
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


        security.log("Tried Signing into ID: "+id+" Failed with Token "+LocalDateTime.now().toString(), Level.WARNING);
        return null;
    }

    private boolean updateProjectAssigned(List<Integer> team){
        String updateQuery = "UPDATE USERS SET ASSIGNED_PROJECTS = ASSIGNED_PROJECTS + 1 WHERE USERID = ?";
        for(Integer userId:team){
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setInt(1, userId); // Set the USERID in the query
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        }
        return false;
    }

    private List<User>getUsersfromteam(List<Integer> team) throws UserNotFoundException {
        List<User> teammembers = new ArrayList<>();
        for (Integer member : team) {
            try {
                PreparedStatement pst = conn.prepareStatement("select * from USERS where USERID=?;");
                pst.setInt(1, member);
                ResultSet rs = pst.executeQuery();
                User user = new User();
                if (rs.next()) {
                    user.setUsername(rs.getString("USERNAME"));
                    user.setName(rs.getString("NAME"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setLastLoggedInDatetime(rs.getTimestamp("LAST_LOGGED_IN_DATETIME").toLocalDateTime());
                    user.setRole(rs.getString("ROLE"));
                    user.setProfilePictureUrl(rs.getString("PROFILE_PICTURE_URL"));
                    user.setAssignedProjects(rs.getInt("ASSIGNED_PROJECTS"));
                }
                teammembers.add(user);

            } catch (SQLException e) {
                throw new UserNotFoundException("The User does not Exists");

            }
        }
        return teammembers;
    }

    private int saveTeam(Team team1,List<Integer> teammembers){
        int teamid = -1;
        try {
            PreparedStatement ptst = conn.prepareStatement("INSERT INTO TEAMS (NAME, TEAM_MEMBERS, PROJECTS_COMPLETED, PROJECTS_ASSIGNED, MANAGER_ID) VALUES (?, ?, ?, ?, ?)");
            String teamMembersStr = teammembers.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(",")); // Converts list to comma-separated string

            System.out.println(teamMembersStr);
            ptst.setString(1, team1.getName()); // NAME
            ptst.setString(2, teamMembersStr); // TEAM_MEMBERS as comma-separated string of user IDs
            ptst.setInt(3, team1.getProjectsCompleted()); // PROJECTS_COMPLETED
            ptst.setInt(4, team1.getProjectsAssigned()); // PROJECTS_ASSIGNED
            ptst.setInt(5, team1.getManagerId()); // MANAGER_ID (should be a valid USERID from the USERS table)
            ptst.executeUpdate();
            ptst = conn.prepareStatement("SELECT MAX(TEAMID) FROM TEAMS");
            ResultSet rs = ptst.executeQuery();
            //This will help us get the id generated by mysql for the inserted team.
            // Check if there's a result
            if (rs.next()) {
                // Get the value of the MAX(TEAMID) from the result set
                teamid = rs.getInt(1);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return teamid;
    }

    @Override
    public boolean createProject(String token, Project proj, List<Integer> team) throws InvalidTokenException, ManagerMaxProjectException, ProjectStartDateException, NoAccessException, TeamMemberException, UserNotFoundException {
        List creds = isAuthorized(token);
        //TO check if he is a manager or not
        int auth = (int) creds.get(0);
        int id = (int) creds.get(1);

        if (auth != 0) { //To check if He has admin access
            security.log("Tried Accessing create project method with: "+id+" Failed with Token "+LocalDateTime.now().toString(), Level.SEVERE);

            throw new NoAccessException("You dont have access to the Feature");
        }
        try {
            //Check if he has more than 4 projects then raise exception
            PreparedStatement pst = conn.prepareStatement("select * from USERS where USERID=?;");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                int assigned = rs.getInt("ASSIGNED_PROJECTS");
                if (assigned >= 4) {
                    throw new ManagerMaxProjectException("Manager Can only create 4 Projects");
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        //Check if the Startdate is two days from current date
        if (!proj.getStartDate().isAfter(LocalDate.now().plusDays(2))) {
            throw new ProjectStartDateException("Project Date should be atleast 2 days in future");
        }

        //create list of user objects of all the team member ids.
        List<User> teammembers = getUsersfromteam(team);


        //Check if Developer assigned has a project already if then raise exception.
        //Check if tester assigned has 2 projects already if then raise exception.

        //By default in front while fetching and showing available devlopers and testers
        // we can always check this. and only show once that match the criteria
        for (User user : teammembers) {
            if (user.getRole().equals("Developer") && user.getAssignedProjects() >= 1) {
                throw new TeamMemberException(user.getUsername() + " Has Already been allotted a project");
            } else if (user.getRole().equals("Tester") && user.getAssignedProjects() >= 2) {
                throw new TeamMemberException(user.getUsername() + " Has Already been allotted to 2 projects");
            }
        }

        //Team object has to be created:
        String teamname = proj.getName() + "001";
        Team teamobj = new Team(1, teamname, teammembers, 1, 1, id);

        //Insert team into table
        //And assign the team id to proj object
        int teamid = saveTeam(teamobj,team);
        proj.setTeamId(teamid);

        try {
            PreparedStatement pstmt = conn.prepareStatement("INSERT INTO PROJECT (name, description, stake_holders, client_name, budget, poc, start_date, team_id, status) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
            pstmt.setString(1, proj.getName());
            pstmt.setString(2, proj.getDescription());
            pstmt.setString(3, proj.getStakeHolders());
            pstmt.setString(4, proj.getClientName());
            pstmt.setDouble(5, proj.getBudget());
            pstmt.setString(6, proj.getPoc());
            pstmt.setDate(7, java.sql.Date.valueOf(String.valueOf(proj.getStartDate())));
            pstmt.setInt(8, proj.getTeamId());
            pstmt.setString(9, proj.getStatus());

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted >= 1) {
                //Since project is created update projects assigned of all the users.
                return updateProjectAssigned(team); //This is the last step of the process hence if it returns true all is true.

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return false;

    }


    //Sakshi code


    @Override
    public List<Project> fetchProjectsManagedByManagerId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        int auth = (int) isAuthorized(token).get(0);
        int managerId = (int) isAuthorized(token).get(1);

        if(auth!=0){
            throw new NoAccessException("You are not Authorized to Access");
        }

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
            if(projects.size()==0){
                throw new NoDataFoundException("No Data Available");

            }
            return projects;
        }

        return null;
    }


    @Override
    public Project fetchProjectDetails(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);
        if(authLevel!=0){
            throw new NoAccessException("You are not Authorized to Access");
        }
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
                }else{
                        throw new NoDataFoundException("No Data Available");

                }
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        return null;
    }

    @Override
    public Team fetchRolesByTeamMemberId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if(authLevel!=0){
            throw new NoAccessException("You are not Authorized to Access");
        }
        if (authLevel == 0) { // Assuming only Admin can fetch roles by team member ID
            List<User> users = new ArrayList<>();
            Team team = new Team();
            String query = "SELECT t.TEAMID as TEAMID, t.NAME AS TEAM_NAME, u.USERID as USERID, u.NAME AS USER_NAME, u.ROLE " +
                    "FROM TEAMS t " +
                    "JOIN USERS u ON t.TEAMID = u.ASSIGNED_PROJECTS " +
                    "WHERE t.MANAGER_ID = ?;";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, userId);
                ResultSet rs = pst.executeQuery();
                while (rs.next()) {
                    User user = new User();
                    team.setTeamId(rs.getInt("TEAMID"));
                    team.setName(rs.getString("TEAM_NAME"));
                    user.setName(rs.getString("USERID"));
                    user.setUsername(rs.getString("USER_NAME"));
                    user.setRole(rs.getString("ROLE"));
                    users.add(user);

                }
                team.setTeamMembers(users);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            if(team.getTeamMembers().isEmpty()){
                throw new NoDataFoundException("No Data Available");
            }
            return team;
        }
        return null;
    }


    @Override
    public List<Bug> fetchBugsPerProjectId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);
        if(authLevel!=0){
            throw new NoAccessException("You are not Authorized to Access");
        }else { // Assuming Admin has access
            List<Bug> bugs = new ArrayList<>();
            String query = "select * from bugs,(select projectid,manager_id,teamid,team_members  from project,teams where project.team_id=teams.teamid and manager_id=?) as t1 where t1.projectid=bugs.project_id;";
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
                System.out.println(e.getMessage()+"jello");
            }
            if(bugs.size()==0){
                throw new NoDataFoundException("No Data Available");
            }
            return bugs;
        }

    }

    @Override
    public boolean assignBugToDeveloper(String token,int bugId,int developerId) throws InvalidTokenException, NoAccessException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);
        if(authLevel!=0){
            throw new NoAccessException("You are not Authorized to Access");
        }

        if (authLevel == 0) { // Assuming Admin has access
            // Assuming input will come from somewhere, for example, a method parameter

            String query = "UPDATE BUGS SET ASSIGNED_TO = ? WHERE BUG_ID = ?";
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
    public boolean closeBug(String token) throws InvalidTokenException, NoAccessException {
        List<Object> authResult = isAuthorized(token);
        int authLevel = (int) authResult.get(0);
        int userId = (int) authResult.get(1);

        if(authLevel!=0){
            throw new NoAccessException("You are not Authorized to Access");
        }
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


//    Abhiraj code

    @Override
    public List<Project> fetchProjectInfoByUserId(String token) throws InvalidTokenException {

        int auth = (int) isAuthorized(token).get(0); // Check if user's authorization
        int id = (int) isAuthorized(token).get(1);   // Fetch user ID of user
        if(auth==2) {
            List<Project> projects = new ArrayList<>();
            String query = "SELECT p.* " +
                    "FROM PROJECT p " +
                    "WHERE p.TEAM_ID IN (" +
                    "    SELECT t.TEAMID " +
                    "    FROM TEAMS t " +
                    "    WHERE t.MANAGER_ID = ? OR FIND_IN_SET(?, t.TEAM_MEMBERS)" +
                    ");";

            try (PreparedStatement pst = conn.prepareStatement(query)) {
                pst.setInt(1, id); // Manager ID
                pst.setInt(2, id); // User ID in team members
                ResultSet rs = pst.executeQuery();

                while (rs.next()) {
                    Project project = new Project();
                    project.setProjectId(rs.getInt("PROJECTID"));
                    project.setName(rs.getString("NAME"));
                    project.setDescription(rs.getString("DESCRIPTION"));
                    project.setStakeHolders(rs.getString("STAKE_HOLDERS"));
                    project.setClientName(rs.getString("CLIENT_NAME"));
                    project.setBudget(rs.getDouble("BUDGET"));
                    project.setPoc(rs.getString("POC"));
                    project.setStartDate(rs.getDate("START_DATE").toLocalDate());
                    project.setTeamId(rs.getInt("TEAM_ID"));
                    project.setStatus(rs.getString("STATUS"));

                    projects.add(project);
                }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch projects for user ID: " + id, e);
            }
            return projects;
        }else
            throw new InvalidTokenException("Token has No access to the system.");
    }




    @Override
    public boolean markGivenBugForClose(int bugId) throws BugNotFoundException, RuntimeException {
        String query = "UPDATE BUGS SET STATUS='CLOSED' WHERE BUG_ID=?;";

        try (PreparedStatement pst = conn.prepareStatement(query)) {
            pst.setInt(1, bugId);
            int rowsAffected = pst.executeUpdate();
            if (rowsAffected == 0) {
                throw new BugNotFoundException("Bug not found with ID: " + bugId);
            }
            return true;
        } catch (SQLException e) {
            throw new RuntimeException("Error marking bug as closed: " + bugId, e);
        }
    }


    //Venky code
//-----------------------------------------------------------------------------------------
    //fetchProjectDetails() method is used for fetching all the project details to a particular tester

    public List<Project> fetchProjectDetailslist(String token) throws InvalidTokenException{
        int auth = (int) isAuthorized(token).get(0);
        int id = (int) isAuthorized(token).get(1);
        if(auth == 1) { // Assuming '2' indicates a tester role
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
        List<Project> assignedProjects = fetchProjectDetailslist(token);
        return assignedProjects;
    }

    //REPORT NEW BUG METHOD
    public Bug reportNewBug(String token, String bugName, String bugDesc, String securityLevel, int projectId) throws InvalidTokenException,SQLException {
        int auth = (int) isAuthorized(token).get(0);
        int userId = (int) isAuthorized(token).get(1);

        if (auth == 1) {
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
        System.out.println("Created By (User ID): " + bug.getCreatedBy());
        System.out.println("Created On: " + LocalDateTime.now());
    }

    //VIEW BUG
    @Override
    public List<Bug> fetchBugsByProjectID(String token, int projectId) throws SQLException, ProjectIdNotFoundException, InvalidTokenException, NoAccessException {
        PreparedStatement pst = null;
        ResultSet rs = null;
        List<Bug> bugsList = new ArrayList<>();

        int auth = (int) isAuthorized(token).get(0);
        int userId = (int) isAuthorized(token).get(1);
        if(auth>2 || auth<0){ //Because manager developer tester cn access
            throw new NoAccessException("No Access Granted to you");
        }

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
//            Date createdOnDate = (Date) Date.from(createdOnLDT.atZone(ZoneId.systemDefault()).toInstant());
            bug.setCreatedOn(createdOnLDT);

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

