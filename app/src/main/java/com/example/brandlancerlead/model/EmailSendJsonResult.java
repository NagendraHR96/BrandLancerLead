package com.example.brandlancerlead.model;

import java.io.Serializable;

public class EmailSendJsonResult implements Serializable {
    private boolean result;
    private String HtmlText;
    private String resultmessage;

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getHtmlText() {
        return HtmlText;
    }

    public void setHtmlText(String htmlText) {
        HtmlText = htmlText;
    }

    public String getResultmessage() {
        return resultmessage;
    }

    public void setResultmessage(String resultmessage) {
        this.resultmessage = resultmessage;
    }
}
