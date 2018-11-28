package com.p5m.me.data;

public class LimitExceedErrorResponse {
    public String statusCode;
    public String errorMessage;
    public String updateInfoText;
    public int totalCount;
    public LimitExceedResponseData data;


    public LimitExceedErrorResponse(String statusCode, String errorMessage, String updateInfoText, int totalCount, LimitExceedResponseData data) {
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
        this.updateInfoText = updateInfoText;
        this.totalCount = totalCount;
        this.data = data;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getUpdateInfoText() {
        return updateInfoText;
    }

    public void setUpdateInfoText(String updateInfoText) {
        this.updateInfoText = updateInfoText;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public LimitExceedResponseData getData() {
        return data;
    }

    public void setData(LimitExceedResponseData data) {
        this.data = data;
    }
}
