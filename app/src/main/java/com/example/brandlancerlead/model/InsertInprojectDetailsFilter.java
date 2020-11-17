package com.example.brandlancerlead.model;

import java.io.Serializable;

public class InsertInprojectDetailsFilter implements Serializable {
    private int LeadID;
    private int IndexID;
    private String Place;
    private String Remarks;
    private String FilePath;
    private String Distance;

    public String getDistance() {
        return Distance;
    }
    public void setDistance(String distance) {
        Distance = distance;
    }

    public int getIndexID() {
        return IndexID;
    }

    public void setIndexID(int indexID) {
        IndexID = indexID;
    }

    public int getLeadID() {
        return LeadID;
    }

    public void setLeadID(int leadID) {
        LeadID = leadID;
    }
    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getFilePath() {
        return FilePath;
    }

    public void setFilePath(String filePath) {
        FilePath = filePath;
    }
}
