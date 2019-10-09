package com.p5m.me.data;

public class RemoteConfigDataModel {

private String en;
private String ar;

    public RemoteConfigDataModel(String en, String ar) {
        this.en = en;
        this.ar = ar;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }

    public String getAr() {
        return ar;
    }

    public void setAr(String ar) {
        this.ar = ar;
    }

}
