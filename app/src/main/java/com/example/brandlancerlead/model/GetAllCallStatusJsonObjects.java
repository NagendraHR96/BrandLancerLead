package com.example.brandlancerlead.model;

import java.io.Serializable;

public class GetAllCallStatusJsonObjects implements Serializable {
    private String CallStatusID;
    private String CallStatusName;

    public String getCallStatusID() {
        return CallStatusID;
    }

    public void setCallStatusID(String callStatusID) {
        CallStatusID = callStatusID;
    }

    public String getCallStatusName() {
        return CallStatusName;
    }

    public void setCallStatusName(String callStatusName) {
        CallStatusName = callStatusName;
    }
}
