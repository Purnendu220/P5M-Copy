
package com.p5m.me.data.request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.SerializedName;

public class ClassRatingRequest implements Serializable {

    private int objectDataId;
    private int objectTypeId;
    private int rating;
    private List<RatingFeedbackAreaList> ratingFeedbackAreaList;
    private int userId;
    private int isPublish;
    private String remark;
    private int status;



    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public ClassRatingRequest(int isPublish) {
        this.isPublish = isPublish;
    }

    public ClassRatingRequest() {
    }


    public int getmObjectDataId() {
        return objectDataId;
    }

    public void setmObjectDataId(int mObjectDataId) {
        this.objectDataId = mObjectDataId;
    }

    public int getmObjectTypeId() {
        return objectTypeId;
    }

    public void setmObjectTypeId(int objectTypeId) {
        this.objectTypeId = objectTypeId;
    }

    public int getmRating() {
        return rating;
    }

    public void setmRating(int rating) {
        this.rating = rating;
    }

    public int getmUserId() {
        return userId;
    }

    public void setmUserId(int userId) {
        this.userId = userId;
    }

    public List<RatingFeedbackAreaList> getRatingFeedbackAreaList() {
        return ratingFeedbackAreaList;
    }

    public void setRatingFeedbackAreaList(List<RatingFeedbackAreaList> ratingFeedbackAreaList) {
        this.ratingFeedbackAreaList = ratingFeedbackAreaList;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }
}
