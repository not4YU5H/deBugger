package com.codefury.dao;

public interface BugTrackingDao {

    String login(String username, String password);

    int fetchUsers(String token);
}
