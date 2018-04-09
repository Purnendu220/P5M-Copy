package www.gymhop.p5m.data.gym_class;

public class TrainerDetail implements java.io.Serializable {
    private static final long serialVersionUID = 8390484787005109308L;
    private String firstName;
    private String profileImageThumbnail;
    private int profileImageId;
    private boolean trainerDeleted;
    private int id;
    private String profileImage;
    private boolean trainerStatus;

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getProfileImageThumbnail() {
        return this.profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public int getProfileImageId() {
        return this.profileImageId;
    }

    public void setProfileImageId(int profileImageId) {
        this.profileImageId = profileImageId;
    }

    public boolean getTrainerDeleted() {
        return this.trainerDeleted;
    }

    public void setTrainerDeleted(boolean trainerDeleted) {
        this.trainerDeleted = trainerDeleted;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileImage() {
        return this.profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean getTrainerStatus() {
        return this.trainerStatus;
    }

    public void setTrainerStatus(boolean trainerStatus) {
        this.trainerStatus = trainerStatus;
    }
}
