package com.p5m.me.data.request;

public class FollowRequest implements java.io.Serializable {
    private static final long serialVersionUID = -3468581577281456488L;

    private int followerId;
    private int followedId;

    public FollowRequest(int followerId, int followedId) {
        this.followerId = followerId;
        this.followedId = followedId;
    }

    public int getFollowerId() {
        return followerId;
    }

    public void setFollowerId(int followerId) {
        this.followerId = followerId;
    }

    public int getFollowedId() {
        return followedId;
    }

    public void setFollowedId(int followedId) {
        this.followedId = followedId;
    }
}
