
package com.p5m.me.data;

import java.io.Serializable;

public class ExploreRatedClassModel implements Serializable {

    /**
     * title : Aikido | Drop-in Class
     * classSessionId : 138530
     * classDate : 2019-12-14
     * fromTime : 20:00:00
     * toTime : 22:00:00
     * gymBranchDetail : {"gymName":"Shudokan | Aikido Dojo"}
     * classMedia : {"mediaId":2783,"mediaUrl":"http://d1zfy71n00v47t.cloudfront.net/class/a3920cb7-fb07-49a5-a17d-f9329b390201.png","mediaThumbNailUrl":"http://d1zfy71n00v47t.cloudfront.net/class/a3920cb7-fb07-49a5-a17d-f9329b390201_thumbnail_.png"}
     * rating : 5.0
     */

    private String title;
    private int classSessionId;
    private String classDate;
    private String fromTime;
    private String toTime;
    private GymBranchDetailBean gymBranchDetail;
    private ClassMediaBean classMedia;
    private double rating;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public GymBranchDetailBean getGymBranchDetail() {
        return gymBranchDetail;
    }

    public void setGymBranchDetail(GymBranchDetailBean gymBranchDetail) {
        this.gymBranchDetail = gymBranchDetail;
    }

    public ClassMediaBean getClassMedia() {
        return classMedia;
    }

    public void setClassMedia(ClassMediaBean classMedia) {
        this.classMedia = classMedia;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public static class GymBranchDetailBean {
        /**
         * gymName : Shudokan | Aikido Dojo
         */

        private String gymName;

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
        }
    }

    public static class ClassMediaBean {
        /**
         * mediaId : 2783
         * mediaUrl : http://d1zfy71n00v47t.cloudfront.net/class/a3920cb7-fb07-49a5-a17d-f9329b390201.png
         * mediaThumbNailUrl : http://d1zfy71n00v47t.cloudfront.net/class/a3920cb7-fb07-49a5-a17d-f9329b390201_thumbnail_.png
         */

        private int mediaId;
        private String mediaUrl;
        private String mediaThumbNailUrl;

        public int getMediaId() {
            return mediaId;
        }

        public void setMediaId(int mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public String getMediaThumbNailUrl() {
            return mediaThumbNailUrl;
        }

        public void setMediaThumbNailUrl(String mediaThumbNailUrl) {
            this.mediaThumbNailUrl = mediaThumbNailUrl;
        }
    }
}


