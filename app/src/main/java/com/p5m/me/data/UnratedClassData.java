package com.p5m.me.data;

import com.p5m.me.data.main.ClassModel;

import java.util.List;


public class UnratedClassData {

    private List<ClassModel> classDetailList;
    private Long count;

    public List<ClassModel> getClassDetailList() {
        return classDetailList;
    }

    public void setClassDetailList(List<ClassModel> classDetailList) {
        this.classDetailList = classDetailList;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

}
