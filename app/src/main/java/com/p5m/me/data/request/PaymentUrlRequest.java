package com.p5m.me.data.request;

/**
 * Created by Varun John on 4/19/2018.
 */

public class PaymentUrlRequest {

    private Integer userId;
    private Integer packageId;
    private Integer sessionId;
    private Integer gymId;
    private Integer promoId;

    public PaymentUrlRequest(Integer userId, Integer packageId) {
        this.userId = userId;
        this.packageId = packageId;
    }

    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
    }

    public PaymentUrlRequest(Integer userId, Integer sessionId, Integer gymId) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.gymId = gymId;
    }

    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId, Integer promoId) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
        this.promoId = promoId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getPackageId() {
        return packageId;
    }

    public void setPackageId(Integer packageId) {
        this.packageId = packageId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Integer getGymId() {
        return gymId;
    }

    public void setGymId(Integer gymId) {
        this.gymId = gymId;
    }

    public Integer getPromoId() {
        return promoId;
    }

    public void setPromoId(Integer promoId) {
        this.promoId = promoId;
    }
}
