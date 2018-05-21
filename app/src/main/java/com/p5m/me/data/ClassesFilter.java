package com.p5m.me.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Varun John on 4/5/2018.
 */

public class ClassesFilter<T> implements Serializable {

    public static int TYPE_ITEM = 1;
    public static int TYPE_HEADER = 2;
    public static int TYPE_SUB_HEADER = 3;

    private String name;
    private String id;
    private int type;
    private int iconResource;
    private boolean isExpanded;
    private boolean isSelected;

    private boolean isLoading;

    private List<ClassesFilter> list;
    private T object;
    private String objectClassName;

    public ClassesFilter(String id, boolean createUniqueId, String objectClassName, String name, int iconResource, int type) {
        if (createUniqueId) {
            this.id = id + objectClassName + name;
        } else {
            this.id = id;
        }
        this.objectClassName = objectClassName;
        this.name = name;
        this.iconResource = iconResource;
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof ClassesFilter) {
            ClassesFilter filter = (ClassesFilter) obj;

            if (filter.getObject() != null && filter.getId().equals(this.getId())) {
                return true;
            }
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return 1029;
    }

    public String getId() {
        return id == null ? "" : id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ClassesFilter> getList() {
        return list;
    }

    public void setList(List<ClassesFilter> list) {
        this.list = list;
    }

    public T getObject() {
        return object;
    }

    public void setObject(T object) {
        this.object = object;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        isLoading = loading;
    }

    public String getName() {
        return name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getIconResource() {
        return iconResource;
    }

    public void setIconResource(int iconResource) {
        this.iconResource = iconResource;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getObjectClassName() {
        return objectClassName;
    }

    public void setObjectClassName(String objectClassName) {
        this.objectClassName = objectClassName;
    }
}