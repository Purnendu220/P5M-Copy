
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;

public class Thumbnails {

    @SerializedName("default")
    private Default mDefault;
    @SerializedName("high")
    private High mHigh;
    @SerializedName("medium")
    private Medium mMedium;
    @SerializedName("standard")
    private Standard mStandard;

    public Default getDefault() {
        return mDefault;
    }

    public void setDefault(Default mDefault) {
        this.mDefault = mDefault;
    }

    public High getHigh() {
        return mHigh;
    }

    public void setHigh(High high) {
        mHigh = high;
    }

    public Medium getMedium() {
        return mMedium;
    }

    public void setMedium(Medium medium) {
        mMedium = medium;
    }

    public Standard getStandard() {
        return mStandard;
    }

    public void setStandard(Standard standard) {
        mStandard = standard;
    }

}
