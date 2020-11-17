package com.example.brandlancerlead.model;

import java.io.Serializable;

public class ConversionReport implements Serializable {
    private int Total_Leads_Assigned;
    private int Converted;
    private int Not_Converted;

    private int Closed;
    private int Not_Closed;

    public int getTotal_Leads_Assigned() {
        return Total_Leads_Assigned;
    }

    public void setTotal_Leads_Assigned(int total_Leads_Assigned) {
        Total_Leads_Assigned = total_Leads_Assigned;
    }

    public int getConverted() {
        return Converted;
    }

    public void setConverted(int converted) {
        Converted = converted;
    }

    public int getNot_Converted() {
        return Not_Converted;
    }

    public void setNot_Converted(int not_Converted) {
        Not_Converted = not_Converted;
    }

    public int getClosed() {
        return Closed;
    }

    public void setClosed(int closed) {
        Closed = closed;
    }

    public int getNot_Closed() {
        return Not_Closed;
    }

    public void setNot_Closed(int not_Closed) {
        Not_Closed = not_Closed;
    }
}
