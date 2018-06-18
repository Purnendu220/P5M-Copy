package com.p5m.me.data;

public class Nationality implements java.io.Serializable {
    private static final long serialVersionUID = -7444969994303069143L;

    private String nationality;
    private String num_code;
    private String alpha_3_code;
    private String alpha_2_code;
    private String en_short_name;

    public String getNationality() {
        return this.nationality == null ? "" : nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getNum_code() {
        return this.num_code;
    }

    public void setNum_code(String num_code) {
        this.num_code = num_code;
    }

    public String getAlpha_3_code() {
        return this.alpha_3_code;
    }

    public void setAlpha_3_code(String alpha_3_code) {
        this.alpha_3_code = alpha_3_code;
    }

    public String getAlpha_2_code() {
        return this.alpha_2_code;
    }

    public void setAlpha_2_code(String alpha_2_code) {
        this.alpha_2_code = alpha_2_code;
    }

    public String getEn_short_name() {
        return this.en_short_name;
    }

    public void setEn_short_name(String en_short_name) {
        this.en_short_name = en_short_name;
    }
}
