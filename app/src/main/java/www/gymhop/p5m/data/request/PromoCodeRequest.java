package www.gymhop.p5m.data.request;

public class PromoCodeRequest implements java.io.Serializable {
    private static final long serialVersionUID = -2674905708586677607L;

    private Integer gymId;
    private String name;
    private int packageId;
    private int userId;

    public PromoCodeRequest(Integer gymId, String name, int packageId, int userId) {
        this.gymId = gymId;
        this.name = name;
        this.packageId = packageId;
        this.userId = userId;
    }

    public PromoCodeRequest(String name, int packageId, int userId) {
        this.name = name;
        this.packageId = packageId;
        this.userId = userId;
    }

    public Integer getGymId() {
        return gymId;
    }

    public void setGymId(Integer gymId) {
        this.gymId = gymId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPackageId() {
        return this.packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public int getUserId() {
        return this.userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
