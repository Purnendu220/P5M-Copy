
package com.p5m.me.data;

import com.google.gson.annotations.SerializedName;


public class ResourceId {

    @SerializedName("kind")
    private String mKind;
    @SerializedName("videoId")
    private String mVideoId;

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public String getVideoId() {
        return mVideoId;
    }

    public void setVideoId(String videoId) {
        mVideoId = videoId;
    }

}
