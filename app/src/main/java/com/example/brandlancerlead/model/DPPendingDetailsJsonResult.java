package com.example.brandlancerlead.model;

import java.io.Serializable;

public class DPPendingDetailsJsonResult implements Serializable {
    private boolean result;
    private String resultmessage;
    private DPPendingDetailsJsonObjects resultset;

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

    public DPPendingDetailsJsonObjects getResultset() {
        return resultset;
    }

    public void setResultset(DPPendingDetailsJsonObjects resultset) {
        this.resultset = resultset;
    }
}
