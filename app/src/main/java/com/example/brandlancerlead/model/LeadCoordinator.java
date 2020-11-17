package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LeadCoordinator implements Serializable {
    private String Appointment_Date;
    private String Appointment_Time;
    private String Cust_Address;
    private String Cust_ContactNo;
    private String Cust_Name;
    private String IsRefix;
    private String LeadId;
    private String TelecallerId;
    private String TelecallerName;
    private String FeedBack;
    private String Booking_ID;
    private String IsSiteVisitRunning;
    private String SiteVisitStatus;
    private int SiteVisitCount;
    private int FollowupCount;


    public String getIsSiteVisitRunning() {
        return IsSiteVisitRunning;
    }

    public void setIsSiteVisitRunning(String isSiteVisitRunning) {
        IsSiteVisitRunning = isSiteVisitRunning;
    }

    public String getBooking_ID() {
        return Booking_ID;
    }

    public void setBooking_ID(String booking_ID) {
        Booking_ID = booking_ID;
    }

    public boolean isHoldFlag() {
        return HoldFlag;
    }

    public void setHoldFlag(boolean holdFlag) {
        HoldFlag = holdFlag;
    }

    private boolean HoldFlag;

    public String getAppointment_Time() {
        return Appointment_Time;
    }

    public void setAppointment_Time(String appointment_Time) {
        Appointment_Time = appointment_Time;
    }

    public String getCust_Address() {
        return Cust_Address;
    }

    public void setCust_Address(String cust_Address) {
        Cust_Address = cust_Address;
    }

    public String getCust_ContactNo() {
        return Cust_ContactNo;
    }

    public void setCust_ContactNo(String cust_ContactNo) {
        Cust_ContactNo = cust_ContactNo;
    }

    public String getCust_Name() {
        return Cust_Name;
    }

    public void setCust_Name(String cust_Name) {
        Cust_Name = cust_Name;
    }

    public String getIsRefix() {
        return IsRefix;
    }

    public void setIsRefix(String isRefix) {
        IsRefix = isRefix;
    }

    public String getLeadId() {
        return LeadId;
    }

    public void setLeadId(String leadId) {
        LeadId = leadId;
    }

    public String getTelecallerId() {
        return TelecallerId;
    }

    public void setTelecallerId(String telecallerId) {
        TelecallerId = telecallerId;
    }

    public String getTelecallerName() {
        return TelecallerName;
    }

    public void setTelecallerName(String telecallerName) {
        TelecallerName = telecallerName;
    }

    public String getFeedBack() {
        return FeedBack;
    }

    public void setFeedBack(String feedBack) {
        FeedBack = feedBack;
    }

    public String getAppointment_Date() {
        return Appointment_Date;
    }

    public void setAppointment_Date(String appointment_Date) {
        Appointment_Date = appointment_Date;
    }

    public String getSiteVisitStatus() {
        return SiteVisitStatus;
    }

    public void setSiteVisitStatus(String siteVisitStatus) {
        SiteVisitStatus = siteVisitStatus;
    }

    public int getSiteVisitCount() {
        return SiteVisitCount;
    }

    public void setSiteVisitCount(int siteVisitCount) {
        SiteVisitCount = siteVisitCount;
    }

    public int getFollowupCount() {
        return FollowupCount;
    }

    public void setFollowupCount(int followupCount) {
        FollowupCount = followupCount;
    }
}
