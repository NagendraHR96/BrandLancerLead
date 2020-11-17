package com.example.brandlancerlead.model;

import java.io.Serializable;

public class PaymentsListJsonObjects implements Serializable {
    private String Amount;
    private String ChequeDate;
    private String ChequeNo;
    private String ModeName;
    private String PaymentCategory;
    private String ReceiptDate;
    private String ReceiptNo;
    private String Status;

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getChequeDate() {
        return ChequeDate;
    }

    public void setChequeDate(String chequeDate) {
        ChequeDate = chequeDate;
    }

    public String getChequeNo() {
        return ChequeNo;
    }

    public void setChequeNo(String chequeNo) {
        ChequeNo = chequeNo;
    }

    public String getModeName() {
        return ModeName;
    }

    public void setModeName(String modeName) {
        ModeName = modeName;
    }

    public String getPaymentCategory() {
        return PaymentCategory;
    }

    public void setPaymentCategory(String paymentCategory) {
        PaymentCategory = paymentCategory;
    }

    public String getReceiptDate() {
        return ReceiptDate;
    }

    public void setReceiptDate(String receiptDate) {
        ReceiptDate = receiptDate;
    }

    public String getReceiptNo() {
        return ReceiptNo;
    }

    public void setReceiptNo(String receiptNo) {
        ReceiptNo = receiptNo;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
