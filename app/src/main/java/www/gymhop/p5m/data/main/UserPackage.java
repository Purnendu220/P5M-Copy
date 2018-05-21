package www.gymhop.p5m.data.main;

public class UserPackage implements java.io.Serializable {
    private static final long serialVersionUID = -3233518942033233177L;

    private String expiryDate;
    private int gymId;
    private int balanceClass;
    private String gymName;
    private int packageId;
    private String packageName;
    private String packageType;
    private int totalNumberOfClass;

    public String getExpiryDate() {
        return this.expiryDate == null ? "" : expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public int getGymId() {
        return this.gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public int getBalanceClass() {
        return this.balanceClass;
    }

    public void setBalanceClass(int balanceClass) {
        this.balanceClass = balanceClass;
    }

    public String getGymName() {
        return this.gymName == null ? "" : gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public int getPackageId() {
        return this.packageId;
    }

    public void setPackageId(int packageId) {
        this.packageId = packageId;
    }

    public String getPackageName() {
        return this.packageName == null ? "" : packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getPackageType() {
        return this.packageType == null ? "" : packageType;
    }

    public void setPackageType(String packageType) {
        this.packageType = packageType;
    }

    public int getTotalNumberOfClass() {
        return this.totalNumberOfClass;
    }

    public void setTotalNumberOfClass(int totalNumberOfClass) {
        this.totalNumberOfClass = totalNumberOfClass;
    }
}
