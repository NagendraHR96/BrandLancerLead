package com.example.brandlancerlead.model;

import java.io.Serializable;

public class DPPendingListJsonObjects implements Serializable {
    private String BookingID;
    private String CustContactNo;
    private String CustName;
    private Double DPAmount;
    private Double DPBalance;
    private String OfferName;
    private String OfferPrice;
    private String ProjectName;
    private String FollowupDate;
    private String FollowupTime;
    private String AgingDays;

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getCustContactNo() {
        return CustContactNo;
    }

    public void setCustContactNo(String custContactNo) {
        CustContactNo = custContactNo;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }

    public Double getDPAmount() {
        return DPAmount;
    }

    public void setDPAmount(Double DPAmount) {
        this.DPAmount = DPAmount;
    }

    public Double getDPBalance() {
        return DPBalance;
    }

    public void setDPBalance(Double DPBalance) {
        this.DPBalance = DPBalance;
    }

    public String getOfferName() {
        return OfferName;
    }

    public void setOfferName(String offerName) {
        OfferName = offerName;
    }

    public String getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(String offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public String getFollowupDate() {
        return FollowupDate;
    }

    public void setFollowupDate(String followupDate) {
        FollowupDate = followupDate;
    }

    public String getFollowupTime() {
        return FollowupTime;
    }

    public void setFollowupTime(String followupTime) {
        FollowupTime = followupTime;
    }

    public String getAgingDays() {
        return AgingDays;
    }

    public void setAgingDays(String agingDays) {
        AgingDays = agingDays;
    }
}
