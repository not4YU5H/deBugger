package com.codefury.dao;

import java.sql.Connection;

public class StorageFactory {
    public static BugTrackingDao getConnection(){
        return new BugTrackingDaoImpl();
    }
}
