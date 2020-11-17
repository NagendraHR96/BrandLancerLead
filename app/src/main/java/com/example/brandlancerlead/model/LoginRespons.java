package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LoginRespons implements Serializable {
    private boolean result;
    private String resultmessage;
    private ExecutiveLoginDetails resultset;

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

    public ExecutiveLoginDetails getResultset() {
        return resultset;
    }

    public void setResultset(ExecutiveLoginDetails resultset) {
        this.resultset = resultset;
    }
}
