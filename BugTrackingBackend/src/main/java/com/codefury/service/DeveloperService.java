package com.codefury.service;
import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.exception.BugNotFoundException;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.NoProjectsAssignedException;
import java.util.List;
public interface DeveloperService {

    User fetchUserInfo(String token) throws InvalidTokenException;
    List<Project> fetchProjectDetailsByUser(String token) throws NoProjectsAssignedException,InvalidTokenException;
    boolean markBugForClose(int bugId) throws BugNotFoundException;
}
