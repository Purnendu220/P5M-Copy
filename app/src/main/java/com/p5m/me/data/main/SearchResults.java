package com.p5m.me.data.main;

import java.util.List;

public class SearchResults implements java.io.Serializable {
    private static final long serialVersionUID = -7782935247385982835L;

    private int classCount;
    private int trainerCount;
    private int gymCount;
    private List<ClassModel> classDetailList;
    private List<GymDetailModel> gymDetailList;
    private List<TrainerModel> trainerDetailList;

    //Own Use
    public String searchText;

    public int getClassCount() {
        return classCount;
    }

    public void setClassCount(int classCount) {
        this.classCount = classCount;
    }

    public int getTrainerCount() {
        return trainerCount;
    }

    public void setTrainerCount(int trainerCount) {
        this.trainerCount = trainerCount;
    }

    public int getGymCount() {
        return gymCount;
    }

    public void setGymCount(int gymCount) {
        this.gymCount = gymCount;
    }

    public List<ClassModel> getClassDetailList() {
        return classDetailList;
    }

    public void setClassDetailList(List<ClassModel> classDetailList) {
        this.classDetailList = classDetailList;
    }

    public List<GymDetailModel> getGymDetailList() {
        return gymDetailList;
    }

    public void setGymDetailList(List<GymDetailModel> gymDetailList) {
        this.gymDetailList = gymDetailList;
    }

    public List<TrainerModel> getTrainerDetailList() {
        return trainerDetailList;
    }

    public void setTrainerDetailList(List<TrainerModel> trainerDetailList) {
        this.trainerDetailList = trainerDetailList;
    }
}
