package com.codefury.beans;

import java.util.List;

public class Team {
    private int teamId;
    private String name;
    private List<User> teamMembers; //List of teamMembers has to be converted to comma seperated userid values and append to db
    private int projectsCompleted;
    private int projectsAssigned;
    private int managerId;

    public Team(int teamId, String name, List<User> teamMembers, int projectsCompleted, int projectsAssigned, int managerId) {
        this.teamId = teamId;
        this.name = name;
        this.teamMembers = teamMembers;
        this.projectsCompleted = projectsCompleted;
        this.projectsAssigned = projectsAssigned;
        this.managerId = managerId;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<User> getTeamMembers() {
        return teamMembers;
    }

    public void setTeamMembers(List<User> teamMembers) {
        this.teamMembers = teamMembers;
    }

    public int getProjectsCompleted() {
        return projectsCompleted;
    }

    public void setProjectsCompleted(int projectsCompleted) {
        this.projectsCompleted = projectsCompleted;
    }

    public int getProjectsAssigned() {
        return projectsAssigned;
    }

    public void setProjectsAssigned(int projectsAssigned) {
        this.projectsAssigned = projectsAssigned;
    }

    public int getManagerId() {
        return managerId;
    }

    public void setManagerId(int managerId) {
        this.managerId = managerId;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", name='" + name + '\'' +
                ", teamMembers=" + teamMembers +
                ", projectsCompleted=" + projectsCompleted +
                ", projectsAssigned=" + projectsAssigned +
                ", managerId=" + managerId +
                '}';
    }
}
