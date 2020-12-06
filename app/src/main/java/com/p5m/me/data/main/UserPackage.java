package com.p5m.me.data.main;

import com.p5m.me.data.SubscriptionConfigModal;

public class UserPackage implements java.io.Serializable {
    private static final long serialVersionUID = -3233518942033233177L;

    private String expiryDate;
    private int gymId;
    private String gymName;
    private int packageId;
    private String packageName;
    private String packageType;
    private long id;
    private Integer totalRemainingWeeks;
    private int totalCredit;
    private int balance;
    private boolean subscriped;
    private String subscriptionStatus;

    public String getExpiryDate() {
        return this.expiryDate == null ? "" : expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getGymId() {
        return this.gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }



    public String getGymName() {
        return this.gymName == null ? "" : gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public int getPackageId() {
        return this.packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return this.packageName == null ? "" : packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return this.packageType == null ? "" : packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getTotalRemainingWeeks() {
        return totalRemainingWeeks;
    }

    public void setTotalRemainingWeeks(Integer totalRemainingWeeks) {
        this.totalRemainingWeeks = totalRemainingWeeks;
    }

    public int getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(int totalCredit) {
        this.totalCredit = totalCredit;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public boolean isSubscriped() {
        return subscriped;
    }

    public void setSubscriped(boolean subscriped) {
        this.subscriped = subscriped;
    }

    public String getSubscriptionStatus() {
        return subscriptionStatus;
    }

    public void setSubscriptionStatus(String subscriptionStatus) {
        this.subscriptionStatus = subscriptionStatus;
    }
}
