package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class FeedbackFilter implements Serializable{

    private String  LeadID;
    private String  Call_Date;
    private String  Time;
    private int  Duration;
    private String  CallFeedback;
    private String  FollowupDate;
    private String  FollowupTime;
    private int  LeadStatus;
    private int Status_ID;
    private int RejectionReason;

    private ArrayList<String> AudioFile;

    public FeedbackFilter(String leadID, String call_Date, int duration, String callFeedback) {
        LeadID = leadID;
        Call_Date = call_Date;
        Duration = duration;
        CallFeedback = callFeedback;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
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



    public int getStatus_ID() {
        return Status_ID;
    }

    public void setStatus_ID(int status_ID) {
        Status_ID = status_ID;
    }

    public int getRejectionReason() {
        return RejectionReason;
    }

    public void setRejectionReason(int rejectionReason) {
        RejectionReason = rejectionReason;
    }

    public ArrayList<String> getAudioString() {
        return AudioFile;
    }

    public void setAudioString(ArrayList<String> audioString) {
        AudioFile = audioString;
    }

    public int getLeadStatus() {
        return LeadStatus;
    }

    public void setLeadStatus(int leadStatus) {
        LeadStatus = leadStatus;
    }
}
