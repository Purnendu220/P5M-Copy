package www.gymhop.p5m.data;

public class Package implements java.io.Serializable {
    private static final long serialVersionUID = -9014729511132434076L;

    private int duration;
    private String validityPeriod;
    private String cost;
    private long modifiedAt;
    private int noOfClass;
    private String name;
    private String description;
    private int id;
    private String packageType;
    private boolean status;

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getValidityPeriod() {
        return this.validityPeriod == null ? "" : validityPeriod;
    }

    public void setValidityPeriod(String validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getCost() {
        return cost == null ? "" : cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public boolean isStatus() {
        return status;
    }

    public long getModifiedAt() {
        return this.modifiedAt;
    }

    public void setModifiedAt(long modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public int getNoOfClass() {
        return this.noOfClass;
    }

    public void setNoOfClass(int noOfClass) {
        this.noOfClass = noOfClass;
    }

    public String getName() {
        return this.name == null ? "" : name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPackageType() {
        return this.packageType == null ? "" : packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
