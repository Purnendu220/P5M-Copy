package com.p5m.me.data.main;

public class StoreApiModel {

    /**
     * statusCode : 2XX
     * data : [{"id":1,"name":"Kuwait","language":"en","timezone":null,"countryId":1,"cityId":5,"status":true},{"id":4,"name":"Riyadh","language":"en","timezone":null,"countryId":2,"cityId":7,"status":true}]
     */

    /**
     * id : 1
     * name : Kuwait
     * language : en
     * timezone : null
     * countryId : 1
     * cityId : 5
     * status : true
     */

    private int id;
    private String name;
    private String language;
    private Object timezone;
    private int countryId;
    private int cityId;
    private boolean status;

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Object getTimezone() {
        return timezone;
    }

    public void setTimezone(Object timezone) {
        this.timezone = timezone;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
