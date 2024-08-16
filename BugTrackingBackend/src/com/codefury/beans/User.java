package com.codefury.beans;

import java.util.Date;

public class User {
    private int userId;
    private String password;
    private String email;
    private String username;
    private String name;
    private String address;
    private Date joinDate;
    private String contactNumber;
    private Date dob;
    private String gender;
    private Date userCreationTime;
    private String profilePictureUrl;
    private String role;
    private int assignedProjects;
    private Date lastLoggedInDatetime;

    // Getters and Setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getUserCreationTime() {
        return userCreationTime;
    }

    public void setUserCreationTime(Date userCreationTime) {
        this.userCreationTime = userCreationTime;
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

    public Date getLastLoggedInDatetime() {
        return lastLoggedInDatetime;
    }

    public void setLastLoggedInDatetime(Date lastLoggedInDatetime) {
        this.lastLoggedInDatetime = lastLoggedInDatetime;
    }

    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", joinDate=" + joinDate +
                ", contactNumber='" + contactNumber + '\'' +
                ", dob=" + dob +
                ", gender='" + gender + '\'' +
                ", userCreationTime=" + userCreationTime +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", role='" + role + '\'' +
                ", assignedProjects=" + assignedProjects +
                ", lastLoggedInDatetime=" + lastLoggedInDatetime +
                '}';
    }
}
