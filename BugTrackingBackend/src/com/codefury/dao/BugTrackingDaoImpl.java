package com.codefury.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BugTrackingDaoImpl implements BugTrackingDao{
    private Connection conn;//Connection to DB
    private EncryptionUtil encryption;//Holds the encryption values
    private HashMap<String, LocalDateTime> tokenvalidity;//Holds the token and validity statements
    //The hashmap is stored in ram so that if the application shutsdown new tokens are generated.

    public BugTrackingDaoImpl() {
        this.conn = DBUtil.getMyConnection();
        this.encryption = new EncryptionUtil();
        this.tokenvalidity = new HashMap<>();
    }


    private static boolean compareTime(LocalDateTime dateTime1, LocalDateTime dateTime2) {
        // Calculate the duration between the two LocalDateTime instances
        Duration duration = Duration.between(dateTime1, dateTime2);
        // 10 minutes in seconds
        long tenMinutes = 10 * 60;
        // Return true if the difference is greater than 10 minutes
        return Math.abs(duration.getSeconds()) > tenMinutes;
    }
    //This function checks the Authorization levels of the user based on the token passed on from the user
    //Use this function always to check for level
    private int isAuthorized(String token){
        //This compares the time the token was created and the current time now to check if the token is stil valid
        //if the time stamp shows more than 10 min it gets removed from the hashmap
        //If not it moves to the authorization checks
        for(String tok :tokenvalidity.keySet()){
            if(tok.equals(token) && compareTime(tokenvalidity.get(tok),LocalDateTime.now())){
                tokenvalidity.remove(tok);
            }
        }
        if(!tokenvalidity.containsKey(token)){
            return -1;
        }
        try {
            //Splitting the decrypted string back to different strings
            String auth = encryption.decrypt(token).split(",")[1];
            if(auth.equals("Admin")){
                return 0;
            }
            else if(auth.equals("Tester")){
                return 1;
            }
            else if (auth.equals("Developer")) {
                return 2;
            }
            else{
                return -1;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //Login function returns a token string which is an encrypted string of data back to the user.
    //This helps us authorize easily by just decrypting the string and dont have to raise a db call everytime.
    //Expiry time also can be set to the token as well.





    //Main methods for Data begins from here.
    @Override
    public String login(String username, String password) {
        try {
            PreparedStatement pst = conn.prepareStatement("select * from USERS where username=? and password=?;");
            pst.setString(1,username);
            pst.setString(2,password);
            ResultSet rs = pst.executeQuery();
            if(rs.next()){
                try {
                    String tokenization =rs.getString("NAME")+","+rs.getString("ROLE")+","+ LocalDateTime.now();
                    String token = encryption.encrypt(tokenization);
                    tokenvalidity.put(token,LocalDateTime.now());
                    return token;
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);

        }

        return "";
    }



    //Sample function to check token access.
    @Override
    public int fetchUsers(String token) {
        return isAuthorized(token);
    }
}
