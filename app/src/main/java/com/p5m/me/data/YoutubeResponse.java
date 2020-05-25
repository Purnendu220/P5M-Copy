
package com.p5m.me.data;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class YoutubeResponse {

    @SerializedName("etag")
    private String mEtag;
    @SerializedName("items")
    private List<Item> mItems;
    @SerializedName("kind")
    private String mKind;
    @SerializedName("pageInfo")
    private PageInfo mPageInfo;

    @SerializedName("nextPageToken")
    private String mNextPageToken;

    @SerializedName("prevPageToken")
    private String mPrevPageToken;

    private boolean showMore;

    public String getEtag() {
        return mEtag;
    }

    public void setEtag(String etag) {
        mEtag = etag;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

    public String getKind() {
        return mKind;
    }

    public void setKind(String kind) {
        mKind = kind;
    }

    public PageInfo getPageInfo() {
        return mPageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        mPageInfo = pageInfo;
    }

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public void setmNextPageToken(String mNextPageToken) {
        this.mNextPageToken = mNextPageToken;
    }

    public String getmPrevPageToken() {
        return mPrevPageToken;
    }

    public void setmPrevPageToken(String mPrevPageToken) {
        this.mPrevPageToken = mPrevPageToken;
    }

    public boolean isShowMore() {
        return showMore;
    }

    public void setShowMore(boolean showMore) {
        this.showMore = showMore;
    }
}
