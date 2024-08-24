package com.codefury.beans;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class Bug {
    private int bugId;
    private int projectId;
    private String bugName;
    private String bugDescription;
    private int createdBy;//UserId
    private LocalDateTime createdOn;
    private int assigned_to;

    public int getAssigned_to() {
        return assigned_to;
    }

    public void setAssigned_to(int assigned_to) {
        this.assigned_to = assigned_to;
    }

    private String imageUrls;
    private String status;
    private String securityLevel;

    public int getBugId() {
        return bugId;
    }

    public void setBugId(int bugId) {
        this.bugId = bugId;
    }

    public int getProjectId() {
        return projectId;
    }

    public void setProjectId(int projectId) {
        this.projectId = projectId;
    }

    public String getBugName() {
        return bugName;
    }

    public void setBugName(String bugName) {
        this.bugName = bugName;
    }

    public String getBugDescription() {
        return bugDescription;
    }

    public void setBugDescription(String bugDescription) {
        this.bugDescription = bugDescription;
    }

    public int getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(String imageUrls) {
        this.imageUrls = imageUrls;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public void setSecurityLevel(String securityLevel) {
        this.securityLevel = securityLevel;
    }


    @Override
    public String toString() {
        return "Bug{" +
                "bugId=" + bugId +
                ", projectId=" + projectId +
                ", bugName='" + bugName + '\'' +
                ", bugDescription='" + bugDescription + '\'' +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                ", assigned_to=" + assigned_to +
                ", imageUrls='" + imageUrls + '\'' +
                ", status='" + status + '\'' +
                ", securityLevel='" + securityLevel + '\'' +
                '}';
    }
}
