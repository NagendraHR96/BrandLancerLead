package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class StatusResultJson  implements Serializable {
    private boolean result;
    private String resultmessage;
    private ArrayList<LeadStatus> resultset;

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

    public ArrayList<LeadStatus> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<LeadStatus> resultset) {
        this.resultset = resultset;
    }
}
