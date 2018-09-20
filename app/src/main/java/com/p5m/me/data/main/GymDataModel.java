package com.p5m.me.data.main;

import java.io.Serializable;
import java.util.List;

public class GymDataModel implements Serializable {

    private int id;
    private String firstName;
    private String lastName;
    private String studioName;
    private boolean isfollow;
    private List<ClassActivity> classCategoryList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStudioName() {
        return studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public boolean isIsfollow() {
        return isfollow;
    }

    public void setIsfollow(boolean isfollow) {
        this.isfollow = isfollow;
    }

    public List<ClassActivity> getClassCategoryList() {
        return classCategoryList;
    }

    public void setClassCategoryList(List<ClassActivity> classCategoryList) {
        this.classCategoryList = classCategoryList;
    }
}
