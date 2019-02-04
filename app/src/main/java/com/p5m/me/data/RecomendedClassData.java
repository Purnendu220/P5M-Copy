package com.p5m.me.data;

import com.p5m.me.data.main.ClassModel;

import java.util.List;

public class RecomendedClassData {


    public RecomendedClassData(List<ClassModel> recomendedClassesList) {
        this.recomendedClassesList = recomendedClassesList;
    }

    private List<ClassModel> recomendedClassesList;

    public List<ClassModel> getRecomendedClassesList() {
        return recomendedClassesList;
    }

    public void setRecomendedClassesList(List<ClassModel> recomendedClassesList) {
        this.recomendedClassesList = recomendedClassesList;
    }
}
