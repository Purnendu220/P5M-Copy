
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;


public class SpecialProgramPopupModel {

    @SerializedName("url")
    private String url;
    @SerializedName("ar_values")
    private SpecialProgramValuesModel mArValues;
    @SerializedName("en_values")
    private SpecialProgramValuesModel mEnValues;

    public SpecialProgramValuesModel getArValues() {
        return mArValues;
    }

    public void setArValues(SpecialProgramValuesModel arValues) {
        mArValues = arValues;
    }

    public SpecialProgramValuesModel getEnValues() {
        return mEnValues;
    }

    public void setEnValues(SpecialProgramValuesModel enValues) {
        mEnValues = enValues;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
