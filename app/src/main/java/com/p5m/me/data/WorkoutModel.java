
package com.p5m.me.data;

import java.io.Serializable;

public class WorkoutModel implements Serializable {


    /**
     * id : 1
     * name : Crossfit
     * arName : كروسفت
     * imageUrl : http://d1zfy71n00v47t.cloudfront.net/explore/activities/Crossfit.jpg
     */

    private int id;
    private String name;
    private String arName;
    private String imageUrl;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


