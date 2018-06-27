package com.p5m.me.data;

public class PromoCode implements java.io.Serializable {
    private static final long serialVersionUID = -2039591090530829458L;
    private float price;
    private float discount;
    private String discountType;
    public String code;
    private int id;
    private float priceAfterDiscount;

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getDiscount() {
        return this.discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public String getDiscountType() {
        return this.discountType;
    }

    public void setDiscountType(String discountType) {
        this.discountType = discountType;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getPriceAfterDiscount() {
        return this.priceAfterDiscount;
    }

    public void setPriceAfterDiscount(float priceAfterDiscount) {
        this.priceAfterDiscount = priceAfterDiscount;
    }
}
