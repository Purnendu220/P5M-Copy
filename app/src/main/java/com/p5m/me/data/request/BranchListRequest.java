package com.p5m.me.data.request;

import com.p5m.me.data.CityLocality;

import java.io.Serializable;
import java.util.List;

public class BranchListRequest implements Serializable {
    /**
     * locationList : []
     * classDate : 2019-08-05
     * activityList : []
     * userId : 8126
     * genderList : ["MALE","MIXED"]
     * gymList : []
     * branchList : []
     * timingList : []
     */

    private String classDate;
    private int userId;
    private List<CityLocality> locationList;
    private List<String> activityList;
    private List<String> genderList;
    private List<String> gymList;
    private List<String> branchList;
    private List<String> timingList;

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<CityLocality> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<CityLocality> locationList) {
        this.locationList = locationList;
    }

    public List<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<String> activityList) {
        this.activityList = activityList;
    }

    public List<String> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<String> genderList) {
        this.genderList = genderList;
    }

    public List<String> getGymList() {
        return gymList;
    }

    public void setGymList(List<String> gymList) {
        this.gymList = gymList;
    }

    public List<String> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<String> branchList) {
        this.branchList = branchList;
    }

    public List<String> getTimingList() {
        return timingList;
    }

    public void setTimingList(List<String> timingList) {
        this.timingList = timingList;
    }


}
