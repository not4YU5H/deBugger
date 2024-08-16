package com.codefury.service;

public interface BugTrackingService {

    String login(String username, String password);

    int fetchUsers(String token);
}
