package com.p5m.me.data.main;

import com.p5m.me.utils.LogUtils;

import java.util.List;

public class TrainerDetailModel implements java.io.Serializable {
    private static final long serialVersionUID = -2605596037140272546L;

    private int profileImageId;
    private boolean trainerDeleted;
    private boolean trainerStatus;

    private String profileImageThumbnail;
    private String gender;
    private int numberOfTrainer;
    private String mobile;
    private boolean favTrainerNotification;
    private long lastActiveDate;
    private String gymNames;
    private String profileImage;
    private String firstName;
    private long dateOfJoining;
    private String nationality;
    private String userCategory;
    private String certificates;
    private String bio;
    private int userProfileImageId;
    private int id;
    private int followerCount = -1;
    private String email;
    private boolean isfollow;
    private List<ClassActivity> classCategoryList;
    private List<GymBranchDetail> trainerBranchResponseList;
    private List<MediaModel> mediaResponseDtoList;
    private List<String> categoryList;
    private boolean status;

    public TrainerDetailModel(TrainerModel trainerModel) {

        try {
            firstName = trainerModel.getFirstName();
            profileImageThumbnail = trainerModel.getProfileImageThumbnail();
            gymNames = trainerModel.getGymNames();
            int id = trainerModel.getId();
            profileImage = trainerModel.getProfileImage();
            isfollow = trainerModel.isIsfollow();
            profileImageId = trainerModel.getProfileImageId();
            trainerDeleted = trainerModel.isTrainerDeleted();
            trainerStatus = trainerModel.isTrainerStatus();

            categoryList = trainerModel.getCategoryList();
            trainerBranchResponseList = trainerModel.getTrainerBranchResponseList();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
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

    public String getProfileImageThumbnail() {
        return profileImageThumbnail == null ? "" : profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getGender() {
        return gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getNumberOfTrainer() {
        return numberOfTrainer;
    }

    public void setNumberOfTrainer(int numberOfTrainer) {
        this.numberOfTrainer = numberOfTrainer;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean isFavTrainerNotification() {
        return favTrainerNotification;
    }

    public void setFavTrainerNotification(boolean favTrainerNotification) {
        this.favTrainerNotification = favTrainerNotification;
    }

    public long getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(long lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getGymNames() {
        return gymNames == null ? "" : gymNames;
    }

    public void setGymNames(String gymNames) {
        this.gymNames = gymNames;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(long dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getNationality() {
        return nationality == null ? "" : nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getUserCategory() {
        return userCategory == null ? "" : userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public String getCertificates() {
        return certificates == null ? "" : certificates;
    }

    public void setCertificates(String certificates) {
        this.certificates = certificates;
    }

    public int getUserProfileImageId() {
        return userProfileImageId;
    }

    public void setUserProfileImageId(int userProfileImageId) {
        this.userProfileImageId = userProfileImageId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowerCount() {
        return followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getEmail() {
        return email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isIsfollow() {
        return isfollow;
    }

    public void setIsfollow(boolean isfollow) {
        this.isfollow = isfollow;
    }

    public List<ClassActivity> getClassCategoryList() {
        return classCategoryList;
    }

    public void setClassCategoryList(List<ClassActivity> classCategoryList) {
        this.classCategoryList = classCategoryList;
    }

    public List<GymBranchDetail> getTrainerBranchResponseList() {
        return trainerBranchResponseList;
    }

    public void setTrainerBranchResponseList(List<GymBranchDetail> trainerBranchResponseList) {
        this.trainerBranchResponseList = trainerBranchResponseList;
    }

    public List<MediaModel> getMediaResponseDtoList() {
        return mediaResponseDtoList;
    }

    public void setMediaResponseDtoList(List<MediaModel> mediaResponseDtoList) {
        this.mediaResponseDtoList = mediaResponseDtoList;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getBio() {
        return bio == null ? "" : bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
