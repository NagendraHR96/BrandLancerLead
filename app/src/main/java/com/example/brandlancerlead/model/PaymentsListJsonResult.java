package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PaymentsListJsonResult implements Serializable {
    private boolean result;
    private boolean resultmessage;
    private ArrayList<PaymentsListJsonObjects> resultset;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public boolean isResultmessage() {
        return resultmessage;
    }

    public void setResultmessage(boolean resultmessage) {
        this.resultmessage = resultmessage;
    }

    public ArrayList<PaymentsListJsonObjects> getResultset() {
        return resultset;
    }

    public void setResultset(ArrayList<PaymentsListJsonObjects> resultset) {
        this.resultset = resultset;
    }
}
