package com.codefury.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class User {
    private int userid;
    private String password;
    private String email;
    private String username;
    private String name;
    private String address;
    private LocalDate joindate;
    private String contactnumber;
    private LocalDate dob;
    private String gender;
    private Date usercreationtime;
    private String profilePictureUrl;
    private String role;
    private int assignedProjects;
    private LocalDateTime lastLoggedInDatetime;

    // Getters and Setters
    public int getUserId() {
        return userid;
    }

    public void setUserId(int userId) {
        this.userid = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getJoinDate() {
        return joindate;
    }

    public void setJoinDate(LocalDate joinDate) {
        this.joindate = joinDate;
    }

    public String getContactNumber() {
        return contactnumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactnumber = contactNumber;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getUserCreationTime() {
        return usercreationtime;
    }

    public void setUserCreationTime(Date userCreationTime) {
        this.usercreationtime = userCreationTime;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getAssignedProjects() {
        return assignedProjects;
    }

    public void setAssignedProjects(int assignedProjects) {
        this.assignedProjects = assignedProjects;
    }

    public LocalDateTime getLastLoggedInDatetime() {
        return lastLoggedInDatetime;
    }

    public void setLastLoggedInDatetime(LocalDateTime lastLoggedInDatetime) {
        this.lastLoggedInDatetime = lastLoggedInDatetime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userid +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", joinDate=" + joindate +
                ", contactNumber='" + contactnumber + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", userCreationTime=" + usercreationtime +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", role='" + role + '\'' +
                ", assignedProjects=" + assignedProjects +
                ", lastLoggedInDatetime=" + lastLoggedInDatetime +
                '}';
    }
}
