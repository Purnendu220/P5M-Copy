package com.p5m.me.data.request;

import java.util.List;

public class ScheduleRequest {

    /**
     * locationList : []
     * classDate : 2019-08-05
     * activityList : []
     * userId : 8126
     * genderList : ["MALE","MIXED"]
     * gymList : []
     * branchList : [9]
     * timingList : []
     */

    private String classDate;
    private int userId;
    private List<?> locationList;
    private List<?> activityList;
    private List<String> genderList;
    private List<?> gymList;
    private List<Integer> branchList;
    private List<?> timingList;
    private int size;

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

    public List<?> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<?> locationList) {
        this.locationList = locationList;
    }

    public List<?> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<?> activityList) {
        this.activityList = activityList;
    }

    public List<String> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<String> genderList) {
        this.genderList = genderList;
    }

    public List<?> getGymList() {
        return gymList;
    }

    public void setGymList(List<?> gymList) {
        this.gymList = gymList;
    }

    public List<Integer> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<Integer> branchList) {
        this.branchList = branchList;
    }

    public List<?> getTimingList() {
        return timingList;
    }

    public void setTimingList(List<?> timingList) {
        this.timingList = timingList;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getSize() {
        return size;
    }
}
