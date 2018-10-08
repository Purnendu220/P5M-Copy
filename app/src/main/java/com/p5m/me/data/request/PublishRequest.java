package com.p5m.me.data.request;

import java.io.Serializable;

public class PublishRequest implements Serializable
{
    private int isPublish;

    public PublishRequest(int isPublish) {
        this.isPublish = isPublish;
    }

    public int getIsPublish() {
        return isPublish;
    }

    public void setIsPublish(int isPublish) {
        this.isPublish = isPublish;
    }
}
