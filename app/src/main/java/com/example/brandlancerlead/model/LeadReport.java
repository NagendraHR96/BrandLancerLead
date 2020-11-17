package com.example.brandlancerlead.model;

import java.io.Serializable;

public class LeadReport implements Serializable {
    private int Total_Leads_Assigned;
    private int Met;
    private int Not_Met;

    public int getTotal_Leads_Assigned() {
        return Total_Leads_Assigned;
    }

    public void setTotal_Leads_Assigned(int total_Leads_Assigned) {
        Total_Leads_Assigned = total_Leads_Assigned;
    }

    public int getMet() {
        return Met;
    }

    public void setMet(int met) {
        Met = met;
    }

    public int getNot_Met() {
        return Not_Met;
    }

    public void setNot_Met(int not_Met) {
        Not_Met = not_Met;
    }
}
