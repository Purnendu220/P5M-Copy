package com.p5m.me.data.main;

public class ClassActivity implements java.io.Serializable {
    private static final long serialVersionUID = 1044920637082958253L;

    private int id;
    private Integer classCategoryId;
    private String classCategoryName = "";
    private String name;
    private String arName;
    private Boolean status;
    private Boolean isSelected=false;

    public ClassActivity(String name, int id) {
        this.name = name;
        this.id = id;
        status = true;
    }

    public ClassActivity(Integer classCategoryId, String classCategoryName) {
        this.classCategoryId = classCategoryId;
        this.classCategoryName = classCategoryName;
    }

    public ClassActivity(int id, Integer classCategoryId, String classCategoryName) {
        this.id = id;
        this.classCategoryId = classCategoryId;
        this.classCategoryName = classCategoryName;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ClassActivity && ((ClassActivity) obj).getClassCategoryId() == getClassCategoryId()) {
            return true;
        }

        return super.equals(obj);
    }

    public String getClassCategoryName() {
        return classCategoryName;
    }

    public void setClassCategoryName(String classCategoryName) {
        this.classCategoryName = classCategoryName;
    }

    public int getClassCategoryId() {
        return classCategoryId == null ? id : classCategoryId;
    }

    public void setClassCategoryId(int classCategoryId) {
        this.classCategoryId = classCategoryId;
    }

    public String getName() {
        return this.name == null ? classCategoryName : name;
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

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public String getArName() {
        return arName;
    }

    public void setArName(String arName) {
        this.arName = arName;
    }
}
