
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;


public class SpecialModel {

    @SerializedName("id")
    private int mId;
    @SerializedName("title")
    private String mTitle;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

}
