package com.codefury.dao;

import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.*;

import java.util.List;

public interface BugTrackingDao {

    String login(String username, String password);
    
    boolean addUsersFromJson(List<User>users);

    User fetchUserInfo(String token) throws InvalidTokenException;


    boolean createProject(String token, Project proj, List<Integer> team) throws InvalidTokenException, ManagerMaxProjectException, ProjectStartDateException, NoAccessException, TeamMemberException, UserNotFoundException;
}
