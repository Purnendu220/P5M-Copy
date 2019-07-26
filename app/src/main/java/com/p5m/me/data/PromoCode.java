package com.p5m.me.data;

public class PromoCode implements java.io.Serializable {
    private static final long serialVersionUID = -2039591090530829458L;
    private float price;
    private float discount;
    private String discountType;
    public String code;
    private int id;
    private float priceAfterDiscount;
    private String promoCode;
    private String promoDesc;;
    private int extraNumberOfDays;
    private int extraNumberOfClass;

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

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public int getExtraNumberOfDays() {
        return extraNumberOfDays;
    }

    public void setExtraNumberOfDays(int extraNumberOfDays) {
        this.extraNumberOfDays = extraNumberOfDays;
    }

    public int getExtraNumberOfClass() {
        return extraNumberOfClass;
    }

    public void setExtraNumberOfClass(int extraNumberOfClass) {
        this.extraNumberOfClass = extraNumberOfClass;
    }

    public String getPromoDesc() {
        return promoDesc;
    }

    public void setPromoDesc(String promoDesc) {
        this.promoDesc = promoDesc;
    }
}
