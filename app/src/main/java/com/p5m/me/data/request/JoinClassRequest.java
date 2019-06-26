package com.p5m.me.data.request;

import com.p5m.me.data.BookWithFriendData;

import java.util.List;

/**
 * Created by Varun John on 4/19/2018.
 */

public class JoinClassRequest {

    private Integer userId;
    private Integer classSessionId;
    private List<BookWithFriendData> userList;

    public JoinClassRequest(Integer userId, Integer classSessionId) {
        this.userId = userId;
        this.classSessionId = classSessionId;
    }

    public JoinClassRequest(Integer userId, Integer classSessionId, List<BookWithFriendData> userList) {
        this.userId = userId;
        this.classSessionId = classSessionId;
        this.userList = userList;
    }
}
