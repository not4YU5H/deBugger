package com.codefury.service;

import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.*;

import java.util.List;

public interface BugTrackingService {

    String login(String username, String password);

    boolean addUsersFromJson(String filepath);

    User fetchUserInfo(String token) throws InvalidTokenException;

    boolean createProject(String token, Project proj, List<Integer> team) throws InvalidTokenException, ManagerMaxProjectException, ProjectStartDateException, TeamMemberException, NoAccessException, UserNotFoundException;
}
