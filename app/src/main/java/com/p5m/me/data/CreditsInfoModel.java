
package com.p5m.me.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class CreditsInfoModel {

    @SerializedName("detail")
    private List<Detail> mDetail;
    @SerializedName("sub")
    private String mSub;
    @SerializedName("sub_ar")
    private String mSubAr;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("title_ar")
    private String mTitleAr;

    public List<Detail> getDetail() {
        return mDetail;
    }

    public void setDetail(List<Detail> detail) {
        mDetail = detail;
    }

    public String getSub() {
        return mSub;
    }

    public void setSub(String sub) {
        mSub = sub;
    }

    public String getSubAr() {
        return mSubAr;
    }

    public void setSubAr(String subAr) {
        mSubAr = subAr;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getTitleAr() {
        return mTitleAr;
    }

    public void setTitleAr(String titleAr) {
        mTitleAr = titleAr;
    }

}
