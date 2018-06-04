package com.p5m.me.data.request;

/**
 * Created by Varun John on 4/19/2018.
 */

public class WishListRequest {

    private Integer userId;
    private Integer classSessionId;

    public WishListRequest(Integer userId, Integer classSessionId) {
        this.userId = userId;
        this.classSessionId = classSessionId;
    }
}
