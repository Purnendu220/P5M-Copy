
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;


public class SpecialProgramValuesModel {

    @SerializedName("buttonOneClassFull")
    private String mButtonOneClassFull;
    @SerializedName("buttonOneNoPlan")
    private String mButtonOneNoPlan;
    @SerializedName("buttonTwoClassFull")
    private String mButtonTwoClassFull;
    @SerializedName("buttonTwoNoPlan")
    private String mButtonTwoNoPlan;
    @SerializedName("messageClassFull")
    private String mMessageClassFull;
    @SerializedName("messageNoPlan")
    private String mMessageNoPlan;
    @SerializedName("title")
    private String mTitle;

    public String getButtonOneClassFull() {
        return mButtonOneClassFull;
    }

    public void setButtonOneClassFull(String buttonOneClassFull) {
        mButtonOneClassFull = buttonOneClassFull;
    }

    public String getButtonOneNoPlan() {
        return mButtonOneNoPlan;
    }

    public void setButtonOneNoPlan(String buttonOneNoPlan) {
        mButtonOneNoPlan = buttonOneNoPlan;
    }

    public String getButtonTwoClassFull() {
        return mButtonTwoClassFull;
    }

    public void setButtonTwoClassFull(String buttonTwoClassFull) {
        mButtonTwoClassFull = buttonTwoClassFull;
    }

    public String getButtonTwoNoPlan() {
        return mButtonTwoNoPlan;
    }

    public void setButtonTwoNoPlan(String buttonTwoNoPlan) {
        mButtonTwoNoPlan = buttonTwoNoPlan;
    }

    public String getMessageClassFull() {
        return mMessageClassFull;
    }

    public void setMessageClassFull(String messageClassFull) {
        mMessageClassFull = messageClassFull;
    }

    public String getMessageNoPlan() {
        return mMessageNoPlan;
    }

    public void setMessageNoPlan(String messageNoPlan) {
        mMessageNoPlan = messageNoPlan;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
