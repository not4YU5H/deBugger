package com.codefury.beans;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Project {
    private int projectId;
    private String name;
    private String description;
    private String stakeHolders;
    private String clientName;
    private BigDecimal budget;
    private String poc;
    private LocalDate startDate;
    private int teamId; //Only pass Team ID for easy transfer of data to DB
    private String status;

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

    public BigDecimal getBudget() {
        return budget;
    }

    public void setBudget(BigDecimal budget) {
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
}
