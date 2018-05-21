package www.gymhop.p5m.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun John on 4/5/2018.
 */

public class PackageLimitListItem implements Serializable {

    public static class Gym {
        public int id;
        public String name;
        public String image;

        public Gym(int id, String name, String image) {
            this.id = id;
            this.name = name;
            this.image = image;
        }
    }

    public static int TYPE_ITEM = 1;
    public static int TYPE_HEADER = 2;
    public static int TYPE_TAB = 3;

    private int type;
    private String name;
    private String id;
    private int selectedTab;
    private boolean isExpanded;

    private PackageLimitModel packageLimitModel;

    //Parent Header item, useful in tabs
    private PackageLimitListItem packageLimitListItem;

    private List<PackageLimitModel> list;

    public PackageLimitListItem(String id, String name, int type) {
        this.id = id;
        this.name = name;
        this.type = type;
        list = new ArrayList<>();
    }

    public PackageLimitListItem(List<PackageLimitModel> list, int type, int selectedTab, PackageLimitListItem packageLimitListItem) {
        this.list = list;
        this.type = type;
        this.selectedTab = selectedTab;
        this.packageLimitListItem = packageLimitListItem;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj instanceof PackageLimitListItem) {
            PackageLimitListItem item = (PackageLimitListItem) obj;

            if (item.getId().equals(this.getId())) {
                return true;
            }
        }

        return super.equals(obj);
    }

    public PackageLimitListItem getPackageLimitListItem() {
        return packageLimitListItem;
    }

    public void setPackageLimitListItem(PackageLimitListItem packageLimitListItem) {
        this.packageLimitListItem = packageLimitListItem;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public PackageLimitModel getPackageLimitModel() {
        return packageLimitModel;
    }

    public void setPackageLimitModel(PackageLimitModel packageLimitModel) {
        this.packageLimitModel = packageLimitModel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(int selectedTab) {
        this.selectedTab = selectedTab;
    }

    public boolean isExpanded() {
        return isExpanded;
    }

    public void setExpanded(boolean expanded) {
        isExpanded = expanded;
    }

    public List<PackageLimitModel> getList() {
        return list;
    }

    public void setList(List<PackageLimitModel> list) {
        this.list = list;
    }
}