package com.p5m.me.data;

import java.util.List;

public class ClassRatingUserData {
    private List<ClassRatingListData> ratingResList;
    private Long count;


    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<ClassRatingListData> getRatingResList() {
        return ratingResList;
    }

    public void setRatingResList(List<ClassRatingListData> ratingResList) {
        this.ratingResList = ratingResList;
    }
}
