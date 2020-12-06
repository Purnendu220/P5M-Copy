
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;


public class UpdateSubscriptionResponse {

    @SerializedName("isCompleted")
    private boolean mIsCompleted;
    @SerializedName("referenceId")
    private String mReferenceId;

    public boolean getIsCompleted() {
        return mIsCompleted;
    }

    public void setIsCompleted(boolean isCompleted) {
        mIsCompleted = isCompleted;
    }

    public String getReferenceId() {
        return mReferenceId;
    }

    public void setReferenceId(String referenceId) {
        mReferenceId = referenceId;
    }

}
