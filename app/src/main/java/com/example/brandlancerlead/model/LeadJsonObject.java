package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class LeadJsonObject implements Serializable {

    private boolean result;
    private String resultmessage;
    private ArrayList<LeadCoordinator>  resultset;
    private ArrayList<LeadCoordinator>  Futureleads;
    private ArrayList<LeadCoordinator>  PreviousLeads;

    public ArrayList<LeadCoordinator> getPreviousLeads() {
        return PreviousLeads;
    }

    public void setPreviousLeads(ArrayList<LeadCoordinator> previousLeads) {
        PreviousLeads = previousLeads;
    }

    public ArrayList<LeadCoordinator> getFutureleads() {
        return Futureleads;
    }

    public void setFutureleads(ArrayList<LeadCoordinator> futureleads) {
        Futureleads = futureleads;
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

    public ArrayList<LeadCoordinator> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<LeadCoordinator> resultset) {
        this.resultset = resultset;
    }
}
