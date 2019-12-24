
package com.p5m.me.data;

import java.io.Serializable;

public class ExploreGymModel implements Serializable {


    /**
     * gymId : 6
     * studioName : P5M Classes
     * profileImage : http://d1zfy71n00v47t.cloudfront.net/gymprofile/9ddf11a5-5d48-4b3d-b735-6adda453e3e1.png
     * profileImageThumbnail : http://d1zfy71n00v47t.cloudfront.net/gymprofile/9ddf11a5-5d48-4b3d-b735-6adda453e3e1_thumbnail_.png
     */

    private String gymId;
    private String studioName;
    private String profileImage;
    private String profileImageThumbnail;

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getProfileImageThumbnail() {
        return profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }
}


