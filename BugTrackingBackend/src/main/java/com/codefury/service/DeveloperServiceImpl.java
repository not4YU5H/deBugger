package com.codefury.service;



import com.codefury.beans.Project;
import com.codefury.beans.User;
import com.codefury.dao.BugTrackingDao;
import com.codefury.dao.StorageFactory;
import com.codefury.exception.BugNotFoundException;
import com.codefury.exception.InvalidTokenException;
import com.codefury.exception.NoProjectsAssignedException;

import java.util.List;

public class DeveloperServiceImpl implements DeveloperService{

    private final BugTrackingDao bugTrackingDao;

    public DeveloperServiceImpl(BugTrackingDao bugTrackingDao) {
        this.bugTrackingDao = StorageFactory.getConnection();
    }

    /**
     * Fetches the list of projects assigned to the developer.
     *
     * @param token The authorization token.
     * @return List of projects.
     * @throws InvalidTokenException if the token is invalid or expired.
     * @throws NoProjectsAssignedException if the developer has no assigned projects.
     */
    @Override
    public User fetchUserInfo(String token) throws InvalidTokenException {
        return bugTrackingDao.fetchUserInfo(token);
    }

    @Override
    public List<Project> fetchProjectDetailsByUser(String token) throws NoProjectsAssignedException,InvalidTokenException {
        return bugTrackingDao.fetchProjectInfoByUserId(token);
    }

    @Override
    public boolean markBugForClose(int bugId) throws BugNotFoundException {
        return bugTrackingDao.markGivenBugForClose(bugId);
    }

}
