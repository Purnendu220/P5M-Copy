package com.p5m.me.data;

public class RatingParamModel implements java.io.Serializable {
    private int id;
    private float rating;
    private String parameter;
    private boolean parameterForPhysical;
    private boolean parameterForVirtual;

    private  boolean isSelected;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isParameterForPhysical() {
        return parameterForPhysical;
    }

    public void setParameterForPhysical(boolean parameterForPhysical) {
        this.parameterForPhysical = parameterForPhysical;
    }

    public boolean isParameterForVirtual() {
        return parameterForVirtual;
    }

    public void setParameterForVirtual(boolean parameterForVirtual) {
        this.parameterForVirtual = parameterForVirtual;
    }
}
