package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GetLeadCallFeedbackJsonResult implements Serializable {
    private boolean result;
    private String resultmessage;
    private ArrayList<GetLeadCallFeedbackJsonObjects>ExecutiveCallFeedback;
    private ArrayList<GetLeadCallFeedbackJsonObjects>resultset;
    private ArrayList<GetLeadCallFeedbackJsonObjects>TelecallerCallFeedback;

    public ArrayList<GetLeadCallFeedbackJsonObjects> getExecutiveCallFeedback() {
        return ExecutiveCallFeedback;
    }

    public void setExecutiveCallFeedback(ArrayList<GetLeadCallFeedbackJsonObjects> executiveCallFeedback) {
        ExecutiveCallFeedback = executiveCallFeedback;
    }

    public ArrayList<GetLeadCallFeedbackJsonObjects> getTelecallerCallFeedback() {
        return TelecallerCallFeedback;
    }

    public void setTelecallerCallFeedback(ArrayList<GetLeadCallFeedbackJsonObjects> telecallerCallFeedback) {
        TelecallerCallFeedback = telecallerCallFeedback;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public void setResultmessage(String resultmessage) {
        this.resultmessage = resultmessage;
    }

    public ArrayList<GetLeadCallFeedbackJsonObjects> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<GetLeadCallFeedbackJsonObjects> resultset) {
        this.resultset = resultset;
    }
}
