package com.p5m.me.data;

public class CityLocality implements java.io.Serializable {
    private static final long serialVersionUID = -2589290259287934828L;
    private double latitude;
    private String name;
    private int id;
    private double longitude;

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return this.name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
