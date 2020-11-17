package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ProjectsJsonResult implements Serializable {
    private boolean result;
    private String resultmessage;
    private ArrayList<ProjectsData> resultset;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public void setResultmessage(String resultmessage) {
        this.resultmessage = resultmessage;
    }

    public ArrayList<ProjectsData> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<ProjectsData> resultset) {
        this.resultset = resultset;
    }
}
