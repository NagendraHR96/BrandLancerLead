package com.example.brandlancerlead.model;

import java.io.Serializable;

public class ProjectsData implements Serializable {
    private String ProjectId;
    private String ProjectName;
    private boolean isSelect;

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

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
