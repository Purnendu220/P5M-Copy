package www.gymhop.p5m.data;

public class ClassActivity implements java.io.Serializable {
    private static final long serialVersionUID = 1044920637082958253L;

    private int id;
    private Integer classCategoryId;
    private String classCategoryName = "";
    private String name;
    private Boolean status;

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
}
