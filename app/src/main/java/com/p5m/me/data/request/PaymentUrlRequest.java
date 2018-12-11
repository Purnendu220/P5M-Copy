package com.p5m.me.data.request;

import com.p5m.me.data.BookWithFriendData;

import java.util.List;

/**
 * Created by Varun John on 4/19/2018.
 */

public class PaymentUrlRequest {

    private Integer userId;
    private Integer packageId;
    private Integer sessionId;
    private Integer gymId;
    private Integer promoId;
    private Long id;
    private List<BookWithFriendData> userList;
    private Integer numOfClass;

    public PaymentUrlRequest(Integer userId, Integer packageId) {
        this.userId = userId;
        this.packageId = packageId;
    }
    public PaymentUrlRequest(Integer userId, Integer sessionId, Integer gymId) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.gymId = gymId;
    }
    public PaymentUrlRequest(Integer userId, Integer sessionId, Integer gymId,List<BookWithFriendData> userList,Integer numOfClass) {
        this.userId = userId;
        this.sessionId = sessionId;
        this.gymId = gymId;
        this.userList=userList;
        this.numOfClass=numOfClass;
    }



    public PaymentUrlRequest(Integer userId,Integer packageId,Long userPacakageId,String type) {
        this.userId = userId;
        this.packageId = packageId;
        this.id = userPacakageId;
    }

    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
    }
    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId,List<BookWithFriendData> userList,Integer numOfClass) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
        this.userList=userList;
        this.numOfClass=numOfClass;
    }
    public PaymentUrlRequest(Integer userId, Integer packageId, Integer sessionId, Integer gymId,List<BookWithFriendData> userList) {
        this.userId = userId;
        this.packageId = packageId;
        this.sessionId = sessionId;
        this.gymId = gymId;
        this.userList=userList;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<BookWithFriendData> getUserList() {
        return userList;
    }

    public void setUserList(List<BookWithFriendData> userList) {
        this.userList = userList;
    }

    public Integer getNumOfClass() {
        return numOfClass;
    }

    public void setNumOfClass(Integer numOfClass) {
        this.numOfClass = numOfClass;
    }
}
