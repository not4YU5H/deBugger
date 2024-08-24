package com.codefury.dao;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.ProjectIdNotFoundException;

import java.sql.SQLException;
import java.util.List;

public interface BugTrackingDao {

    String login(String username, String password);
    
    boolean addUsersFromJson(List<User>users);

    User fetchUserInfo(String token) throws InvalidTokenException;


    List<Project> fetchProjectDetails(String token) throws InvalidTokenException;

    List<Project> fetchAssignedProjectList(String token) throws InvalidTokenException;

    Bug reportNewBug(String token, String bugName, String bugDesc, String securityLevel, int projectId) throws InvalidTokenException, SQLException;

    List<Bug> fetchBugsByProjectID(int projectId) throws SQLException, ProjectIdNotFoundException;
}
