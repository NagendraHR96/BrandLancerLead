package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LeadReportListJsonObjects implements Serializable {
    private String AppointmentDate;
    private String AppointmentTime;
    private String ContactNumber;
    private String CustName;
    private String Feedback;
    private String LeadStatus;
    private String MetStatus;
    private String Telecaller;
    private int  Count;
    private int  LeadID;

    public String getAppointmentDate() {
        return AppointmentDate;
    }

    public void setAppointmentDate(String appointmentDate) {
        AppointmentDate = appointmentDate;
    }

    public String getAppointmentTime() {
        return AppointmentTime;
    }

    public void setAppointmentTime(String appointmentTime) {
        AppointmentTime = appointmentTime;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public String getFeedback() {
        return Feedback;
    }

    public void setFeedback(String feedback) {
        Feedback = feedback;
    }

    public String getLeadStatus() {
        return LeadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        LeadStatus = leadStatus;
    }

    public String getMetStatus() {
        return MetStatus;
    }

    public void setMetStatus(String metStatus) {
        MetStatus = metStatus;
    }

    public String getTelecaller() {
        return Telecaller;
    }

    public void setTelecaller(String telecaller) {
        Telecaller = telecaller;
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public int getLeadID() {
        return LeadID;
    }

    public void setLeadID(int leadID) {
        LeadID = leadID;
    }
}
