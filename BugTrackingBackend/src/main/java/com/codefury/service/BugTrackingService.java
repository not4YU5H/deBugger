package com.codefury.service;

import com.codefury.beans.Bug;
import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.InvalidTokenException;

import java.util.List;

public interface BugTrackingService {

    String login(String username, String password);

    boolean addUsersFromJson(String filepath);

    User fetchUserInfo(String token) throws InvalidTokenException;



    List<Project> fetchProjectsManagedByManagerId(String token) throws InvalidTokenException;


    Project fetchProjectDetails(String token) throws InvalidTokenException;

    List<String> fetchRolesByTeamMemberId(String token) throws InvalidTokenException;


    List<Bug> fetchBugsPerProjectId(String token) throws InvalidTokenException;

    boolean assignBugToDeveloper(String token) throws InvalidTokenException;

    boolean closeBug(String token) throws InvalidTokenException;
}
