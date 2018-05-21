package com.p5m.me.data;

public class FollowResponse implements java.io.Serializable {
    private static final long serialVersionUID = -7968751412570170079L;

    private String profileImageThumbnail;
    private String profileImage;
    private boolean follow;

    public String getProfileImageThumbnail() {
        return this.profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean getFollow() {
        return this.follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }
}
