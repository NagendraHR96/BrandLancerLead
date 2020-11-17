package com.example.brandlancerlead.model;

import java.io.Serializable;
import java.util.ArrayList;

public class JsonModelObject implements Serializable {

    private boolean result;
    private String resultmessage;


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

}
