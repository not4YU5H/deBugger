package com.codefury.dao;

import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.BugNotFoundException;
import com.codefury.exception.InvalidTokenException;

import java.util.List;

public interface BugTrackingDao {

    String login(String username, String password);
    
    boolean addUsersFromJson(List<User>users);

    User fetchUserInfo(String token) throws InvalidTokenException;

    List<Project> fetchProjectInfoByUserId(int userId);

    boolean markGivenBugForClose(int bugId) throws BugNotFoundException,RuntimeException;
}
