
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SpecialProgramModel {

    @SerializedName("specialClassId")
    private List<SpecialModel> mSpecialClassId;
    @SerializedName("specialGym")
    private List<SpecialModel> mSpecialGym;
    @SerializedName("specialTrainer")
    private List<SpecialModel> mSpecialTrainer;

    public List<SpecialModel> getSpecialClassId() {
        return mSpecialClassId;
    }

    public void setSpecialClassId(List<SpecialModel> specialClassId) {
        mSpecialClassId = specialClassId;
    }

    public List<SpecialModel> getSpecialGym() {
        return mSpecialGym;
    }

    public void setSpecialGym(List<SpecialModel> specialGym) {
        mSpecialGym = specialGym;
    }

    public List<SpecialModel> getSpecialTrainer() {
        return mSpecialTrainer;
    }

    public void setSpecialTrainer(List<SpecialModel> specialTrainer) {
        mSpecialTrainer = specialTrainer;
    }

}
