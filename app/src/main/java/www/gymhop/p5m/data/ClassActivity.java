package www.gymhop.p5m.data;

import com.google.gson.annotations.SerializedName;

public class ClassActivity implements java.io.Serializable {
    private static final long serialVersionUID = 1044920637082958253L;

    @SerializedName(value="name", alternate={"classCategoryName"})
    private String name;

    private int id;

    private boolean status;

    public String getName() {
        return this.name;
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

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return name;
    }
}
