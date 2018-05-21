package www.gymhop.p5m.data;

public class WishListResponse implements java.io.Serializable {
    private static final long serialVersionUID = 1268908943180718644L;

    private int classSessionId;
    private int id;
    private int userId;
    private long createDate;
    private boolean status;

    public int getClassSessionId() {
        return this.classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public long getCreateDate() {
        return this.createDate;
    }

    public void setCreateDate(long createDate) {
        this.createDate = createDate;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
