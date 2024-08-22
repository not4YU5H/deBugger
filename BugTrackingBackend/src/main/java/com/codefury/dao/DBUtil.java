package com.codefury.dao;

import com.mysql.cj.jdbc.Driver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    static Connection conn = null;
    public static Connection getMyConnection(){
        if(conn==null){
            try {
                DriverManager.registerDriver(new Driver());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            String url = "jdbc:mysql://localhost:3306/codefury";
            String user = "root";
            String password = "samgeorge";

            //step 2
            try {
                conn = DriverManager.getConnection(url,user,password);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Connection Established");
        }
        return conn;
    }
    public static void closeConnection(){
        try {
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
