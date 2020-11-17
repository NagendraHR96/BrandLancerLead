package com.example.brandlancerlead.model;

import java.io.Serializable;

public class GetLeadCallFeedbackJsonObjects implements Serializable {
    private String AudioFile;
    private String CallFeedback;
    private String CallStatus;
    private String Duration;
    private String LeadID;
    private String Time;
    private String Call_Date;
    private String BookingID;

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getCall_Date() {
        return Call_Date;
    }

    public void setCall_Date(String call_Date) {
        Call_Date = call_Date;
    }

    public String getAudioFile() {
        return AudioFile;
    }

    public void setAudioFile(String audioFile) {
        AudioFile = audioFile;
    }

    public String getCallFeedback() {
        return CallFeedback;
    }

    public void setCallFeedback(String callFeedback) {
        CallFeedback = callFeedback;
    }

    public String getCallStatus() {
        return CallStatus;
    }

    public void setCallStatus(String callStatus) {
        CallStatus = callStatus;
    }

    public String getDuration() {
        return Duration;
    }

    public void setDuration(String duration) {
        Duration = duration;
    }

    public String getLeadID() {
        return LeadID;
    }

    public void setLeadID(String leadID) {
        LeadID = leadID;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
