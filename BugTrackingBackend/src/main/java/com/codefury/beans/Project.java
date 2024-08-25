package com.codefury.beans;



import java.time.LocalDate;

public class Project {
    private int projectId;
    private String name;
    private String description;
    private String stakeHolders;
    private String clientName;
    private Double budget;
    private String poc;
    private LocalDate startDate;
    private int teamId; //Only pass Team ID for easy transfer of data to DB
    private String status = "In Progress";

    public Project(int projectId, String name, String description, String stakeHolders, String clientName, Double budget, String poc, LocalDate startDate, int teamId) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.stakeHolders = stakeHolders;
        this.clientName = clientName;
        this.budget = budget;
        this.poc = poc;
        this.startDate = startDate;
        this.teamId = teamId;

    }



    public Project() {

    }

    // Getters and Setters
    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStakeHolders() {
        return stakeHolders;
    }

    public void setStakeHolders(String stakeHolders) {
        this.stakeHolders = stakeHolders;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Double getBudget() {
        return budget;
    }

    public void setBudget(Double budget) {
        this.budget = budget;
    }

    public String getPoc() {
        return poc;
    }

    public void setPoc(String poc) {
        this.poc = poc;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public int getTeamId() {
        return teamId;
    }

    public void setTeamId(int teamId) {
        this.teamId = teamId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Project{" +
                "projectId=" + projectId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", stakeHolders='" + stakeHolders + '\'' +
                ", clientName='" + clientName + '\'' +
                ", budget=" + budget +
                ", poc='" + poc + '\'' +
                ", startDate=" + startDate +
                ", teamId=" + teamId +
                ", status='" + status + '\'' +
                '}';
    }
}
