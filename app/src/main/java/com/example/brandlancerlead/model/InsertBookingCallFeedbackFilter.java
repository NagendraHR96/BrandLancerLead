package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class InsertBookingCallFeedbackFilter implements Serializable {
    private int BookingID;
    private String Call_Date;
    private String Time;
    private int Duration;
    private String CallFeedback;
    private int Status_ID;
    private ArrayList<String> AudioFile;
    private boolean AppointmentGiven;
    private String AppointmentDate;
    private String AppointmentTime;
    private boolean IsFollowup;
    private boolean RequestForBookingCancel;
    private String FollowupDate;
    private String FollowupTime;
    private String ExecutiveID;
    private int SourceID;

    public boolean isRequestForBookingCancel() {
        return RequestForBookingCancel;
    }

    public void setRequestForBookingCancel(boolean requestForBookingCancel) {
        RequestForBookingCancel = requestForBookingCancel;
    }

    public int getBookingID() {
        return BookingID;
    }

    public void setBookingID(int bookingID) {
        BookingID = bookingID;
    }

    public String getCall_Date() {
        return Call_Date;
    }

    public void setCall_Date(String call_Date) {
        Call_Date = call_Date;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getDuration() {
        return Duration;
    }

    public void setDuration(int duration) {
        Duration = duration;
    }

    public String getCallFeedback() {
        return CallFeedback;
    }

    public void setCallFeedback(String callFeedback) {
        CallFeedback = callFeedback;
    }

    public int getStatus_ID() {
        return Status_ID;
    }

    public void setStatus_ID(int status_ID) {
        Status_ID = status_ID;
    }

    public ArrayList<String> getAudioFile() {
        return AudioFile;
    }

    public void setAudioFile(ArrayList<String> audioFile) {
        AudioFile = audioFile;
    }

    public boolean isAppointmentGiven() {
        return AppointmentGiven;
    }

    public void setAppointmentGiven(boolean appointmentGiven) {
        AppointmentGiven = appointmentGiven;
    }

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

    public boolean isFollowup() {
        return IsFollowup;
    }

    public void setFollowup(boolean followup) {
        IsFollowup = followup;
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

    public String getExecutiveID() {
        return ExecutiveID;
    }

    public void setExecutiveID(String executiveID) {
        ExecutiveID = executiveID;
    }

    public int getSourceID() {
        return SourceID;
    }

    public void setSourceID(int sourceID) {
        SourceID = sourceID;
    }
}
