package com.p5m.me.data;

public class OnBoardingData {
    /**
     * text_en : welcome to p5m
     * text_ar : welcome to p5m
     * image : http://d1zfy71n00v47t.cloudfront.net/class/5ce3c7b0-7a24-44f8-aab1-403c82a71c99.png
     */

    private String text_en;
    private String text_ar;
    private String image;

    public String getText_en() {
        return text_en;
    }

    public void setText_en(String text_en) {
        this.text_en = text_en;
    }

    public String getText_ar() {
        return text_ar;
    }

    public void setText_ar(String text_ar) {
        this.text_ar = text_ar;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
