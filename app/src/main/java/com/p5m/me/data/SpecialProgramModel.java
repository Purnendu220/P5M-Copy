
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SpecialProgramModel {

    @SerializedName("classId")
    private int[] mSpecialClassId;
    @SerializedName("gymId")
    private int[] mSpecialGym;
    @SerializedName("trainerId")
    private int[] mSpecialTrainer;

    public int[] getSpecialClassId() {
        return mSpecialClassId;
    }

    public void setSpecialClassId(int[] specialClassId) {
        mSpecialClassId = specialClassId;
    }

    public int[] getSpecialGym() {
        return mSpecialGym;
    }

    public void setSpecialGym(int[] specialGym) {
        mSpecialGym = specialGym;
    }

    public int[] getSpecialTrainer() {
        return mSpecialTrainer;
    }

    public void setSpecialTrainer(int[] specialTrainer) {
        mSpecialTrainer = specialTrainer;
    }

}
