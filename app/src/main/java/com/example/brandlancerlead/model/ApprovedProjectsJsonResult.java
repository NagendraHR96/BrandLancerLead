package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class ApprovedProjectsJsonResult implements Serializable {
    private boolean result;
    private String resultmessage;

    private ArrayList<ApprovedProjectsObjects>resultset;

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

    public ArrayList<ApprovedProjectsObjects> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<ApprovedProjectsObjects> resultset) {
        this.resultset = resultset;
    }

}
