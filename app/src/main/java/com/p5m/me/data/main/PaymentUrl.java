
package com.p5m.me.data.main;

public class PaymentUrl implements java.io.Serializable {
    private static final long serialVersionUID = 6865989894929977201L;
    private String tapPayURL;
    private String paymentURL;
    private String responseMessage;
    private String referenceID;
    private String responseCode;

    public String getTapPayURL() {
        return this.tapPayURL;
    }

    public void setTapPayURL(String tapPayURL) {
        this.tapPayURL = tapPayURL;
    }

    public String getPaymentURL() {
        return this.paymentURL;
    }

    public void setPaymentURL(String paymentURL) {
        this.paymentURL = paymentURL;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getReferenceID() {
        return this.referenceID;
    }

    public void setReferenceID(String referenceID) {
        this.referenceID = referenceID;
    }

    public String getResponseCode() {
        return this.responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }
}
