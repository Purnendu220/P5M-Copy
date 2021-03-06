
package com.p5m.me.data;

import com.p5m.me.data.main.MediaModel;

import java.io.Serializable;
import java.util.List;

public class ClassRatingListData implements Serializable {

    private Long createDate;
    private Long id;
    private Double rating;
    private List<RatingFeedbackAreaResList> ratingFeedbackAreaResList;
    private UserDetail userDetail;
    private List<MediaModel> mediaList;
    private String remark;
    private String saftyRemark;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Long createDate) {
        this.createDate = createDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getRating() {
        return Math.round(rating*10)/10.f;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public List<RatingFeedbackAreaResList> getRatingFeedbackAreaResList() {
        return ratingFeedbackAreaResList;
    }

    public void setRatingFeedbackAreaResList(List<RatingFeedbackAreaResList> ratingFeedbackAreaResList) {
        this.ratingFeedbackAreaResList = ratingFeedbackAreaResList;
    }

    public UserDetail getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    public List<MediaModel> getMediaList() {
        return mediaList;
    }

    public void setMediaList(List<MediaModel> mediaList) {
        this.mediaList = mediaList;
    }

    public String getSaftyRemark() {
        return saftyRemark;
    }

    public void setSaftyRemark(String saftyRemark) {
        this.saftyRemark = saftyRemark;
    }
}


