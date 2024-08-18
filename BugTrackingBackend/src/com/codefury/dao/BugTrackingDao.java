package com.codefury.dao;

import com.codefury.beans.User;

import java.util.List;

public interface BugTrackingDao {

    String login(String username, String password);

    int fetchUsers(String token);

    boolean addUsersFromJson(List<User>users);
}
