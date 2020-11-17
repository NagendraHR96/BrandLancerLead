package com.example.brandlancerlead.model;

import java.io.Serializable;

public class ApprovedProjectsObjects implements Serializable {
    private String ProjectId;
    private String ProjectName;

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }
}
