package com.example.brandlancerlead.model;

import java.io.Serializable;

public class RawReportSummary implements Serializable {
    private boolean result;
    private String resultmessage;

    private ConversionReport leadsConverted;
    private SiteVisitReport siteVisits;
    private LeadReport leadsMet;

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

    public ConversionReport getLeadsConverted() {
        return leadsConverted;
    }

    public void setLeadsConverted(ConversionReport leadsConverted) {
        this.leadsConverted = leadsConverted;
    }

    public SiteVisitReport getSiteVisits() {
        return siteVisits;
    }

    public void setSiteVisits(SiteVisitReport siteVisits) {
        this.siteVisits = siteVisits;
    }

    public LeadReport getLeadsMet() {
        return leadsMet;
    }

    public void setLeadsMet(LeadReport leadsMet) {
        this.leadsMet = leadsMet;
    }
}
