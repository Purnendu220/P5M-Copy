package com.p5m.me.data.main;

import com.p5m.me.data.PromoCode;

public class Package implements java.io.Serializable {
    private static final long serialVersionUID = -9014729511132434076L;

    private int duration;
    private String validityPeriod;
    private float cost;
    private long modifiedAt;
    private int noOfClass;
    private String name;
    private String description;
    private String gymName;
    private int id;
    private String packageType;
    private boolean status;
    private PromoCode promoResponseDto;

    public String getGymName() {
        return gymName == null ? "" : gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getValidityPeriod() {
        return this.validityPeriod == null ? "" : validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public float getCost() {
        return cost;
    }

    public void setCost(float cost) {
        this.cost = cost;
    }

    public boolean isStatus() {
        return status;
    }

    public long getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getNoOfClass() {
        return this.noOfClass;
    }

    public void setNoOfClass(int noOfClass) {
        this.noOfClass = noOfClass;
    }

    public String getName() {
        return this.name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageType() {
        return this.packageType == null ? "" : packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public PromoCode getPromoResponseDto() {
        return promoResponseDto;
    }

    public void setPromoResponseDto(PromoCode promoResponseDto) {
        this.promoResponseDto = promoResponseDto;
    }
}
