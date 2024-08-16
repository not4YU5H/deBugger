package com.codefury.service;

import com.codefury.dao.BugTrackingDao;
import com.codefury.dao.StorageFactory;

public class BugTrackingServiceImpl implements BugTrackingService{
    private BugTrackingDao bugTrackingDao;
    public BugTrackingServiceImpl() {
        this.bugTrackingDao = StorageFactory.getConnection();

    }


    @Override
    public String login(String username, String password) {
        return bugTrackingDao.login(username,password);
    }

    @Override
    public int fetchUsers(String token) {
        return bugTrackingDao.fetchUsers(token);
    }


}
