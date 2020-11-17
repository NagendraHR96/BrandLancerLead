package com.example.brandlancerlead.model;

import java.io.Serializable;

public class DPPendingDetailsJsonObjects implements Serializable {
    private String Address;
    private String AgingInDays;
    private String AmountPaid;
    private String BookingDate;
    private String BookingID;
    private String ContactNumber;
    private String CustomerName;
    private Double DPAmount;
    private Double DPBalance;
    private String DPDate;
    private String Offer;
    private Double OfferPrice;
    private String ProjectName;

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getAgingInDays() {
        return AgingInDays;
    }

    public void setAgingInDays(String agingInDays) {
        AgingInDays = agingInDays;
    }

    public String getAmountPaid() {
        return AmountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        AmountPaid = amountPaid;
    }

    public String getBookingDate() {
        return BookingDate;
    }

    public void setBookingDate(String bookingDate) {
        BookingDate = bookingDate;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
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

    public String getDPDate() {
        return DPDate;
    }

    public void setDPDate(String DPDate) {
        this.DPDate = DPDate;
    }

    public String getOffer() {
        return Offer;
    }

    public void setOffer(String offer) {
        Offer = offer;
    }

    public Double getOfferPrice() {
        return OfferPrice;
    }

    public void setOfferPrice(Double offerPrice) {
        OfferPrice = offerPrice;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }
}
