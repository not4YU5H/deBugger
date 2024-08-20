package com.codefury.service;

import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;

public interface BugTrackingService {

    String login(String username, String password);

    boolean addUsersFromJson(String filepath);

    User fetchUserInfo(String token) throws InvalidTokenException;
}
