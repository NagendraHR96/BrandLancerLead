package com.example.brandlancerlead.model;

import java.io.Serializable;

public class SiteObjects implements Serializable {
    private String SiteNo;
    private int StatusCode;

    public String getSiteNo() {
        return SiteNo;
    }

    public void setSiteNo(String siteNo) {
        SiteNo = siteNo;
    }

    public int getStatusCode() {
        return StatusCode;
    }

    public void setStatusCode(int statusCode) {
        StatusCode = statusCode;
    }
}
