package www.gymhop.p5m.data.main;

public class GymBranchDetail implements java.io.Serializable {
    private static final long serialVersionUID = 7807737247569814553L;
    private String studioInstruction;
    private int branchId;
    private String address;
    private String mediaUrl;
    private double latitude;
    private String gymName;
    private String branchName;
    private int mediaId;
    private int gymId;
    private String localityName;
    private String phoneNumber;
    private String mediaThumbNailUrl;
    private int localityId;
    private double longitude;
    private String gender;

    public String getStudioInstruction() {
        return studioInstruction == null ? "" : studioInstruction;
    }

    public void setStudioInstruction(String studioInstruction) {
        this.studioInstruction = studioInstruction;
    }

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getAddress() {
        return address == null ? "" : address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMediaUrl() {
        return mediaUrl == null ? "" : mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getGymName() {
        return gymName == null ? "" : gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getBranchName() {
        return branchName == null ? "" : branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getMediaId() {
        return mediaId;
    }

    public void setMediaId(int mediaId) {
        this.mediaId = mediaId;
    }

    public int getGymId() {
        return gymId;
    }

    public void setGymId(int gymId) {
        this.gymId = gymId;
    }

    public String getLocalityName() {
        return localityName == null ? "" : localityName;
    }

    public void setLocalityName(String localityName) {
        this.localityName = localityName;
    }

    public String getPhoneNumber() {
        return phoneNumber == null ? "" : phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMediaThumbNailUrl() {
        return mediaThumbNailUrl == null ? "" : mediaThumbNailUrl;
    }

    public void setMediaThumbNailUrl(String mediaThumbNailUrl) {
        this.mediaThumbNailUrl = mediaThumbNailUrl;
    }

    public int getLocalityId() {
        return localityId;
    }

    public void setLocalityId(int localityId) {
        this.localityId = localityId;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
