package com.p5m.me.data;

public class TryP5MData {

    /**
     * message :
     * buttonTitle : Explore plans
     * buttonTitleAr : null
     */



    private String message;
    private String buttonTitle;
    private String buttonTitleAr;

    public TryP5MData(String message, String buttonTitle, String buttonTitleAr) {
        this.message = message;
        this.buttonTitle = buttonTitle;
        this.buttonTitleAr = buttonTitleAr;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getButtonTitle() {
        return buttonTitle;
    }

    public void setButtonTitle(String buttonTitle) {
        this.buttonTitle = buttonTitle;
    }

    public String getButtonTitleAr() {
        return buttonTitleAr;
    }

    public void setButtonTitleAr(String buttonTitleAr) {
        this.buttonTitleAr = buttonTitleAr;
    }
}
