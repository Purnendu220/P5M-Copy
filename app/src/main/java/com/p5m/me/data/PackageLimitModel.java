package com.p5m.me.data;

public class PackageLimitModel implements java.io.Serializable {
    private static final long serialVersionUID = -2568309405145748271L;
    private int numberOfVisit;
    private int totalClassPerPackage;
    private String gymNames;
    private String packageName;
    private String packageType;

    public int getNumberOfVisit() {
        return this.numberOfVisit;
    }

    public void setNumberOfVisit(int numberOfVisit) {
        this.numberOfVisit = numberOfVisit;
    }

    public int getTotalClassPerPackage() {
        return this.totalClassPerPackage;
    }

    public void setTotalClassPerPackage(int totalClassPerPackage) {
        this.totalClassPerPackage = totalClassPerPackage;
    }

    public String getGymNames() {
        return this.gymNames == null ? "" : gymNames;
    }

    public void setGymNames(String gymNames) {
        this.gymNames = gymNames;
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
}
