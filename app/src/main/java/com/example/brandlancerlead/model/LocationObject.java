package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LocationObject implements Serializable {
    private String EmployeeID;
    private String Latitude;
    private String Longitude;


    public LocationObject(String employeeID) {
        EmployeeID = employeeID;

    }

    public String getEmployeeID() {
        return EmployeeID;
    }

    public String getLatitude() {
        return Latitude;
    }

    public String getLongitude() {
        return Longitude;
    }


    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }
}
