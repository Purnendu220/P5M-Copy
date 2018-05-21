package com.p5m.me.data.main;

public class UserWhoFiredEvent implements java.io.Serializable {
    private static final long serialVersionUID = -2478971504316613322L;

    private String firstName;
    private String profileImageThumbnail;
    private int id;
    private String profileImage;

    public String getFirstName() {
        return this.firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getProfileImageThumbnail() {
        return this.profileImageThumbnail == null ? "" : profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileImage() {
        return this.profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
