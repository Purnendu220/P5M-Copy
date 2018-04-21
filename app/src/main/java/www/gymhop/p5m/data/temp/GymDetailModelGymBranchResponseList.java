package www.gymhop.p5m.data.temp;

public class GymDetailModelGymBranchResponseList implements java.io.Serializable {
    private static final long serialVersionUID = -6427709754484811743L;
    private String studioInstruction;
    private int branchId;
    private int gymId;
    private String localityName;
    private String address;
    private String phoneNumber;
    private String gender;
    private double latitude;
    private String gymName;
    private String branchName;
    private int localityId;
    private double longitude;

    public String getStudioInstruction() {
        return this.studioInstruction;
    }

    public void setStudioInstruction(String studioInstruction) {
        this.studioInstruction = studioInstruction;
    }

    public int getBranchId() {
        return this.branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public int getGymId() {
        return this.gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getLocalityName() {
        return this.localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getGymName() {
        return this.gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getBranchName() {
        return this.branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getLocalityId() {
        return this.localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
