package com.p5m.me.data.request;

import com.p5m.me.data.CityLocality;

import java.util.List;

public class ClassListRequest implements java.io.Serializable {
    private static final long serialVersionUID = -9109485815849361072L;

    private List<CityLocality> locationList;
    private List<String> timingList;
    private List<String> activityList;
    private List<String> genderList;
    private  List<String> gymList;
    private  List<String> priceModelList;
    private  List<String> fitnessLevelList;

    private String classDate;
    private int userId;
    private int size;
    private int page;

    public ClassListRequest() {
    }

    public List<CityLocality> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<CityLocality> locationList) {
        this.locationList = locationList;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public List<String> getTimingList() {
        return timingList;
    }

    public void setTimingList(List<String> timingList) {
        this.timingList = timingList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<String> activityList) {
        this.activityList = activityList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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

    public List<String> getpriceModelList() {
        return priceModelList;
    }

    public void setpriceModelList(List<String> priceModelList) {
        this.priceModelList = priceModelList;
    }

    public List<String> getfitnessLevelList() {
        return fitnessLevelList;
    }

    public void setfitnessLevelList(List<String> fitnessLevelList) {
        this.fitnessLevelList = fitnessLevelList;
    }
}
