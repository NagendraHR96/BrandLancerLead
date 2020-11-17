package com.example.brandlancerlead.model;

import java.io.Serializable;

public class RejectionStatus implements Serializable {
    private String RejectionId;
    private String RejectionName;

    public String getRejectionId() {
        return RejectionId;
    }

    public void setRejectionId(String rejectionId) {
        RejectionId = rejectionId;
    }

    public String getRejectionName() {
        return RejectionName;
    }

    public void setRejectionName(String rejectionName) {
        RejectionName = rejectionName;
    }
}
