package com.p5m.me.restapi;

public class ResponseModel<T> {

    public String statusCode;
    public String errorMessage;
    public String updateInfoText;
    public int totalCount;
    public T data;

}
