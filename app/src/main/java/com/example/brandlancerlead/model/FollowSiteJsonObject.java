package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FollowSiteJsonObject implements Serializable {


    private boolean result;
    private String resultmessage;

    private ArrayList<LeadCoordinator>  CurrentFollowups;
    private ArrayList<LeadCoordinator>  FutureFollowups;
    private ArrayList<LeadCoordinator>  PreviousFollowups;


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

    public ArrayList<LeadCoordinator> getCurrentFollowups() {
        return CurrentFollowups;
    }

    public void setCurrentFollowups(ArrayList<LeadCoordinator> currentFollowups) {
        CurrentFollowups = currentFollowups;
    }

    public ArrayList<LeadCoordinator> getFutureFollowups() {
        return FutureFollowups;
    }

    public void setFutureFollowups(ArrayList<LeadCoordinator> futureFollowups) {
        FutureFollowups = futureFollowups;
    }

    public ArrayList<LeadCoordinator> getPreviousFollowups() {
        return PreviousFollowups;
    }

    public void setPreviousFollowups(ArrayList<LeadCoordinator> previousFollowups) {
        PreviousFollowups = previousFollowups;
    }
}
