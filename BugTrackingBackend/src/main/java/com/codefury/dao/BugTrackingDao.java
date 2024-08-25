package com.codefury.dao;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.Team;
import com.codefury.beans.User;
import com.codefury.exception.*;

import java.sql.SQLException;
import java.util.List;

public interface BugTrackingDao {

    String login(String username, String password);
    
    boolean addUsersFromJson(List<User>users);

    User fetchUserInfo(String token) throws InvalidTokenException;


    boolean createProject(String token, Project proj, List<Integer> team) throws InvalidTokenException, ManagerMaxProjectException, ProjectStartDateException, NoAccessException, TeamMemberException, UserNotFoundException;

    //Sakshi Code
    List<Project> fetchProjectsManagedByManagerId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException;

    Project fetchProjectDetails(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException;

    Team fetchRolesByTeamMemberId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException;

    List<Bug> fetchBugsPerProjectId(String token) throws InvalidTokenException, NoAccessException, NoDataFoundException;

    boolean assignBugToDeveloper(String token,int bugId,int developerId) throws InvalidTokenException, NoAccessException;

    boolean closeBug(String token) throws InvalidTokenException, NoAccessException;

    List<Project> fetchProjectInfoByUserId(String token) throws InvalidTokenException;

    boolean markGivenBugForClose(int bugId) throws BugNotFoundException;

    List<Project> fetchAssignedProjectList(String token)throws InvalidTokenException;

    Bug reportNewBug(String token, String bugName, String bugDesc, String securityLevel, int projectId) throws InvalidTokenException, SQLException;

    //VIEW BUG
    List<Bug> fetchBugsByProjectID(String token, int projectId) throws SQLException, ProjectIdNotFoundException, InvalidTokenException, NoAccessException;
}
