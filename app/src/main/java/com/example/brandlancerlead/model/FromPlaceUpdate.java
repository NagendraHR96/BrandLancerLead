package com.example.brandlancerlead.model;

import java.io.Serializable;

public class FromPlaceUpdate implements Serializable {

        private String Lead_Id;
        private String FromPlace;
        private String EmployeeID;

    public FromPlaceUpdate(String lead_Id, String fromPlace,String EmployeeID) {
        Lead_Id = lead_Id;
        FromPlace = fromPlace;
        this.EmployeeID = EmployeeID;
    }

    public String getLead_Id() {
        return Lead_Id;
    }

    public String getFromPlace() {
        return FromPlace;
    }
}
