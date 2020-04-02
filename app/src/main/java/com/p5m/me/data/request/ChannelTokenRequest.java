package com.p5m.me.data.request;

public class ChannelTokenRequest {
  private int userId;
  private int classSessionId;

    public ChannelTokenRequest(int userId, int classSessionId) {
        this.userId = userId;
        this.classSessionId = classSessionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }
}
