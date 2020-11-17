package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LeadSubmitPostObject implements Serializable {
    private String Lead_Id;
    private String ToPlace;
    private String Distance;
    private String RejectionComments;
    private String ExecutiveFeedback;
    private String FollowUpDate;
    private String Status_ID;
    private String MetStatus;
    private String FileName;
    private String FollowUpTime;

    private String ReceiptNo;
    private String BookingID;
    private String Amount;
    private String ReceiptPhoto;
    private int IndexID;


    public String getReceiptPhoto() {
        return ReceiptPhoto;
    }

    public void setReceiptPhoto(String receiptPhoto) {
        ReceiptPhoto = receiptPhoto;
    }

    private int LeadPurposeID;

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public int getLeadPurposeID() {
        return LeadPurposeID;
    }

    public void setLeadPurposeID(int leadPurposeID) {
        LeadPurposeID = leadPurposeID;
    }

    public LeadSubmitPostObject(String lead_Id, String toPlace, String distance, String rejectionComments, String executiveFeedback, String followUpDate, String status_ID, String metStatus, String fileName, String followupTime) {
        Lead_Id = lead_Id;
        ToPlace = toPlace;
        Distance = distance;
        RejectionComments = rejectionComments;
        ExecutiveFeedback = executiveFeedback;
        FollowUpDate = followUpDate;
        Status_ID = status_ID;
        MetStatus = metStatus;
        FileName = fileName;
        FollowUpTime = followupTime;
    }

    public String getLead_Id() {
        return Lead_Id;
    }

    public String getToPlace() {
        return ToPlace;
    }

    public String getDistance() {
        return Distance;
    }

    public String getRejectionComments() {
        return RejectionComments;
    }

    public String getExecutiveFeedback() {
        return ExecutiveFeedback;
    }

    public String getFollowUpDate() {
        return FollowUpDate;
    }

    public String getStatus_ID() {
        return Status_ID;
    }

    public String getMetStatus() {
        return MetStatus;
    }

    public String getImageString() {
        return FileName;
    }

    public int getIndexID() {
        return IndexID;
    }

    public void setIndexID(int indexID) {
        IndexID = indexID;
    }

    public LeadSubmitPostObject(String lead_Id, String toPlace, String distance, String rejectionComments, String executiveFeedback, String followUpDate, String status_ID, String metStatus, String fileName, String followUpTime, String receiptNo, String bookingID, String amount, String receiptPhoto, int indexID, int leadPurposeID) {
        Lead_Id = lead_Id;
        ToPlace = toPlace;
        Distance = distance;
        RejectionComments = rejectionComments;
        ExecutiveFeedback = executiveFeedback;
        FollowUpDate = followUpDate;
        Status_ID = status_ID;
        MetStatus = metStatus;
        FileName = fileName;
        FollowUpTime = followUpTime;
        ReceiptNo = receiptNo;
        BookingID = bookingID;
        Amount = amount;
        ReceiptPhoto = receiptPhoto;
        IndexID = indexID;
        LeadPurposeID = leadPurposeID;
    }
}
