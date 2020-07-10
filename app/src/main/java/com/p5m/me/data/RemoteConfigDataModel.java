package com.p5m.me.data;

public class RemoteConfigDataModel {

    private String en;
    private String ar;
    private String ar_ksa;
    private String en_ksa;
    private String title_en;
    private String title_ar;


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

    public String getAr_ksa() {
        return ar_ksa;
    }

    public void setAr_ksa(String ar_ksa) {
        this.ar_ksa = ar_ksa;
    }

    public String getEn_ksa() {
        return en_ksa;
    }

    public void setEn_ksa(String en_ksa) {
        this.en_ksa = en_ksa;
    }

    public String getTitle_en() {
        return title_en;
    }

    public void setTitle_en(String title_en) {
        this.title_en = title_en;
    }

    public String getTitle_ar() {
        return title_ar;
    }

    public void setTitle_ar(String title_ar) {
        this.title_ar = title_ar;
    }
}
