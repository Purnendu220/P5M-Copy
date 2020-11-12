
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;


public class UpdateSubscriptionRequest {

    public UpdateSubscriptionRequest(String mAction, long mPackageId) {
        this.mAction = mAction;
        this.mPackageId = mPackageId;
    }

    @SerializedName("action")
    private String mAction;
    @SerializedName("packageId")
    private long mPackageId;

    public String getAction() {
        return mAction;
    }

    public void setAction(String action) {
        mAction = action;
    }

    public long getPackageId() {
        return mPackageId;
    }

    public void setPackageId(long packageId) {
        mPackageId = packageId;
    }

}
