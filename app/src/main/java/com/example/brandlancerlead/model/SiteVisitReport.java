package com.example.brandlancerlead.model;

import java.io.Serializable;

public class SiteVisitReport implements Serializable {
    private int Site_Visit_Done;
    private int Site_Visit_Pending;
    private int Site_Vist_Confirmed;

    public int getSite_Visit_Done() {
        return Site_Visit_Done;
    }

    public void setSite_Visit_Done(int site_Visit_Done) {
        Site_Visit_Done = site_Visit_Done;
    }

    public int getSite_Visit_Pending() {
        return Site_Visit_Pending;
    }

    public void setSite_Visit_Pending(int site_Visit_Pending) {
        Site_Visit_Pending = site_Visit_Pending;
    }

    public int getSite_Vist_Confirmed() {
        return Site_Vist_Confirmed;
    }

    public void setSite_Vist_Confirmed(int site_Vist_Confirmed) {
        Site_Vist_Confirmed = site_Vist_Confirmed;
    }
}
