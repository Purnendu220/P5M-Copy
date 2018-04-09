package www.gymhop.p5m.data;

import java.util.List;

/**
 * Created by Varun John on 4/5/2018.
 */

public class ClassesFilter<K,T> {

    public static int TYPE_ITEM = 1;
    public static int TYPE_HEADER = 2;
    public static int TYPE_SUB_HEADER = 3;

    private String name;
    private int type;
    private int iconResource;
    private boolean isExpanded;
    private boolean isSelected;

    private List<ClassesFilter<K,T>> list;
    private K object;

    public List<ClassesFilter<K, T>> getList() {
        return list;
    }

    public void setList(List<ClassesFilter<K, T>> list) {
        this.list = list;
    }

    public K getObject() {
        return object;
    }

    public void setObject(K object) {
        this.object = object;
    }

    public ClassesFilter(String name, int iconResource, int type) {
        this.name = name;
        this.iconResource = iconResource;
        this.type = type;
    }

    public String getName() {
        return name;
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
}