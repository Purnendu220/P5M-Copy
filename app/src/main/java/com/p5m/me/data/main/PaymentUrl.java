
package com.p5m.me.data.main;

public class PaymentUrl implements java.io.Serializable {
    private static final long serialVersionUID = 6865989894929977201L;
    private String tapPayURL;
    private String paymentUrl;
    private String responseMessage;
    private String referenceId;
    private String responseCode;
    private boolean isCompleted;

    public String getTapPayURL() {
        return this.tapPayURL;
    }

    public void setTapPayURL(String tapPayURL) {
        this.tapPayURL = tapPayURL;
    }

    public String getPaymentURL() {
        return this.paymentUrl;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentUrl = paymentURL;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getReferenceID() {
        return this.referenceId;
    }

    public void setReferenceID(String referenceID) {
        this.referenceId = referenceID;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public boolean getCompleted() {
        return isCompleted;
    }

    public void setCompleted(boolean completed) {
        isCompleted = completed;
    }
}

