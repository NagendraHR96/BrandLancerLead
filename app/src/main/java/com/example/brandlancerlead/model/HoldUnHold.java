package com.example.brandlancerlead.model;

import java.io.Serializable;

public class HoldUnHold implements Serializable {
    private String Lead_Id;
    private boolean HoldFlag;

    public HoldUnHold(String lead_Id, boolean holdFlag) {
        Lead_Id = lead_Id;
        HoldFlag = holdFlag;
    }
}
