package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class GetAllCallStatusJsonResult implements Serializable {
    private  boolean result;
    private  String resultmessage;
    private ArrayList<GetAllCallStatusJsonObjects> resultset;

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

    public ArrayList<GetAllCallStatusJsonObjects> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<GetAllCallStatusJsonObjects> resultset) {
        this.resultset = resultset;
    }
}
