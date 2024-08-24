package com.codefury.service;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.*;

import java.sql.SQLException;
import java.util.List;

public interface BugTrackingService {

    String login(String username, String password);

    boolean addUsersFromJson(String filepath);

    User fetchUserInfo(String token) throws InvalidTokenException;

    boolean createProject(String token, Project proj, List<Integer> team) throws InvalidTokenException, ManagerMaxProjectException, ProjectStartDateException, TeamMemberException, NoAccessException, UserNotFoundException;


    //-----------------------------------------------------------------------------------------
    //TESTER FUNCTIONS
    //-----------------------------------------------------------------------------------------
    List<Project> fetchProjectDetails(String token) throws InvalidTokenException;

    List<Project> fetchAssignedProjectList(String token) throws InvalidTokenException;

    Bug reportNewBug(String token, String bugName, String bugDesc, String securityLevel, int projectId) throws InvalidTokenException, SQLException;

    List<Bug> fetchBugsByProjectID(int projectId) throws SQLException, ProjectIdNotFoundException;
}
