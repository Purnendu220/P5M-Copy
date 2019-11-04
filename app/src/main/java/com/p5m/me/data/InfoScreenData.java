package com.p5m.me.data;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class InfoScreenData {

    public String text;
    public String image;
    public int resource;


    public InfoScreenData(String text, int resource) {
        this.text = text;
        this.resource = resource;
    }
    public InfoScreenData(String text, String image) {
        this.text = text;
        this.image = image;
    }
}
