package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class RejectionJson implements Serializable {

    private boolean result;
    private String resultmessage;
    private ArrayList<RejectionStatus> resultset;

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

    public ArrayList<RejectionStatus> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<RejectionStatus> resultset) {
        this.resultset = resultset;
    }
}
