package com.p5m.me.data.main;

public class Transaction implements java.io.Serializable {
    private static final long serialVersionUID = -4910303785266356025L;
    private long date;
    private double amount;
    private String packageName;
    private String referenceId;
    private String status;

    public long getDate() {
        return this.date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getPackageName() {
        return this.packageName == null ? "" : packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getReferenceId() {
        return this.referenceId == null ? "" : referenceId;
    }

    public void setReferenceId(String referenceId) {
        this.referenceId = referenceId;
    }

    public String getStatus() {
        return this.status == null ? "" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
