
package com.p5m.me.data;

import java.io.Serializable;

public class ExploreTrainerModel implements Serializable {

    /**
     * trainerId : 24
     * trainerName : Shadwa Mohamed
     * isfollow : false
     * profileImage : http://d1zfy71n00v47t.cloudfront.net/userprofile/f29efb2e-f48b-48fa-997e-e168e870a306.png
     * profileImageThumbnail : http://d1zfy71n00v47t.cloudfront.net/userprofile/f29efb2e-f48b-48fa-997e-e168e870a306_thumbnail_.png
     */

    private int trainerId;
    private String trainerName;
    private boolean isfollow;
    private String profileImage;
    private String profileImageThumbnail;

    public int getTrainerId() {
        return trainerId;
    }

    public void setTrainerId(int trainerId) {
        this.trainerId = trainerId;
    }

    public String getTrainerName() {
        return trainerName;
    }

    public void setTrainerName(String trainerName) {
        this.trainerName = trainerName;
    }

    public boolean isIsfollow() {
        return isfollow;
    }

    public void setIsfollow(boolean isfollow) {
        this.isfollow = isfollow;
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


