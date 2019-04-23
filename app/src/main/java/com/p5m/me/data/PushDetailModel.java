package com.p5m.me.data;

import java.io.Serializable;

public class PushDetailModel implements Serializable {

    private String message;
    private String type;
    private String source= "Android";
    private String url;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSource() {
        return source;
    }

    public void setSource() {
        this.source = "Android";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


}