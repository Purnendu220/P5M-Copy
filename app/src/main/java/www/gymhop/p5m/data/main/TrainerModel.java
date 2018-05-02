package www.gymhop.p5m.data.main;

import java.util.List;

public class TrainerModel implements java.io.Serializable {
    private static final long serialVersionUID = 3425195923894483261L;

    private String firstName;
    private String profileImageThumbnail;
    private String gymNames;
    private int id;
    private String profileImage;
    private boolean isfollow;
    private int profileImageId;
    private boolean trainerDeleted;
    private boolean trainerStatus;

    private List<String> categoryList;
    private List<GymBranchDetail> trainerBranchResponseList;

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof TrainerModel && ((TrainerModel) obj).getId() == getId()) {
            return true;
        }
        return super.equals(obj);
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getProfileImageThumbnail() {
        return profileImageThumbnail == null ? "" : profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getGymNames() {
        return gymNames == null ? "" : gymNames;
    }

    public void setGymNames(String gymNames) {
        this.gymNames = gymNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public boolean isIsfollow() {
        return isfollow;
    }

    public void setIsfollow(boolean isfollow) {
        this.isfollow = isfollow;
    }

    public int getProfileImageId() {
        return profileImageId;
    }

    public void setProfileImageId(int profileImageId) {
        this.profileImageId = profileImageId;
    }

    public boolean isTrainerDeleted() {
        return trainerDeleted;
    }

    public void setTrainerDeleted(boolean trainerDeleted) {
        this.trainerDeleted = trainerDeleted;
    }

    public boolean isTrainerStatus() {
        return trainerStatus;
    }

    public void setTrainerStatus(boolean trainerStatus) {
        this.trainerStatus = trainerStatus;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public List<GymBranchDetail> getTrainerBranchResponseList() {
        return trainerBranchResponseList;
    }

    public void setTrainerBranchResponseList(List<GymBranchDetail> trainerBranchResponseList) {
        this.trainerBranchResponseList = trainerBranchResponseList;
    }
}
