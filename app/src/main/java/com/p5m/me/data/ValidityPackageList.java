package com.p5m.me.data;


public class ValidityPackageList {


private Integer id;
private String name;
private String description;
private Double cost;
private String currencyCode;
private Integer duration;
private String validityPeriod;
private String packageType;
private Integer noOfClass;
private Boolean status;
private long createdAt;
private long modifiedAt;


public Integer getId() {
return id;
}

public void setId(Integer id) {
this.id = id;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public String getDescription() {
return description;
}

public void setDescription(String description) {
this.description = description;
}

public Double getCost() {
return cost;
}

public void setCost(Double cost) {
this.cost = cost;
}

public Integer getDuration() {
return duration;
}

public void setDuration(Integer duration) {
this.duration = duration;
}

public String getValidityPeriod() {
return validityPeriod;
}

public void setValidityPeriod(String validityPeriod) {
this.validityPeriod = validityPeriod;
}

public String getPackageType() {
return packageType;
}

public void setPackageType(String packageType) {
this.packageType = packageType;
}

public Integer getNoOfClass() {
return noOfClass;
}

public void setNoOfClass(Integer noOfClass) {
this.noOfClass = noOfClass;
}

public Boolean getStatus() {
return status;
}

public void setStatus(Boolean status) {
this.status = status;
}

public long getCreatedAt() {
return createdAt;
}

public void setCreatedAt(long createdAt) {
this.createdAt = createdAt;
}

public long getModifiedAt() {
return modifiedAt;
}

public void setModifiedAt(long modifiedAt) {
this.modifiedAt = modifiedAt;
}

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}