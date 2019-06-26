package com.p5m.me.data;

public class ContactResponse {

    /**
     * statusCode : 2XX
     * data : SUCCESS
     */

    private String statusCode;
    private String data;

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
