package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LeadStatus implements Serializable {
   private String StatusId;
   private String StatusName;

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getStatusName() {
        return StatusName;
    }

    public void setStatusName(String statusName) {
        StatusName = statusName;
    }
}
