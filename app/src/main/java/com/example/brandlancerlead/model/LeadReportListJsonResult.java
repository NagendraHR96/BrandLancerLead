package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class LeadReportListJsonResult implements Serializable {
    private boolean result;
    private String resultmessage;
    private ArrayList<LeadReportListJsonObjects>resultset;

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

    public ArrayList<LeadReportListJsonObjects> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<LeadReportListJsonObjects> resultset) {
        this.resultset = resultset;
    }
}
