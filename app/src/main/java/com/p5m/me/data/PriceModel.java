package com.p5m.me.data;

public class PriceModel {


    /**
     * order : 1
     * name : Group Classes
     * arName : كلاسات جماعية
     * value : CHARGABLE
     * imageUrl : http://d1zfy71n00v47t.cloudfront.net/explore/categories/group.jpg
     */
    private String id;

    private String order;
    private String name;
    private String arName;
    private String value;
    private String imageUrl;

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
