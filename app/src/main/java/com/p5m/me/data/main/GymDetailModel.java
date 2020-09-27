package com.p5m.me.data.main;

import com.p5m.me.data.QuestionAnswerModel;

import java.util.List;

public class GymDetailModel implements java.io.Serializable {
    private static final long serialVersionUID = -3539286405036295126L;
    private String profileImageThumbnail;
    private String gender;
    private int numberOfTrainer;
    private String studioName;
    private long lastActiveDate;
    private String bio;
    private String profileImage;
    private String userCategory;
    private int userProfileImageId;
    private int coverBackGroundImageId;
    private int price;
    private String coverImage;
    private int monthlyPrive;
    private int id;
    private int followerCount = -1;
    private String email;
    private String coverImageThumbnail;
    private int yearlyPrice;
    private String website;
    private String mobile;
    private boolean favTrainerNotification;
    private String firstName;
    private String mainLocation;
    private long dateOfJoining;
    private boolean isfollow;
    private boolean status;

    private List<GymBranchDetail> gymBranchResponseList;
    private List<MediaModel> mediaResponseDtoList;
    private List<ClassActivity> classCategoryList;
    private List<QuestionAnswerModel> covidSafetyList;


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

    public String getStudioName() {
        return studioName == null ? "" : studioName;
    }

    public void setStudioName(String studioName) {
        this.studioName = studioName;
    }

    public long getLastActiveDate() {
        return lastActiveDate;
    }

    public void setLastActiveDate(long lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public String getBio() {
        return bio == null ? "" : bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfileImage() {
        return profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserCategory() {
        return userCategory == null ? "" : userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public int getUserProfileImageId() {
        return userProfileImageId;
    }

    public void setUserProfileImageId(int userProfileImageId) {
        this.userProfileImageId = userProfileImageId;
    }

    public int getCoverBackGroundImageId() {
        return coverBackGroundImageId;
    }

    public void setCoverBackGroundImageId(int coverBackGroundImageId) {
        this.coverBackGroundImageId = coverBackGroundImageId;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getCoverImage() {
        return coverImage == null ? "" : coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public int getMonthlyPrive() {
        return monthlyPrive;
    }

    public void setMonthlyPrive(int monthlyPrive) {
        this.monthlyPrive = monthlyPrive;
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

    public String getCoverImageThumbnail() {
        return coverImageThumbnail == null ? "" : coverImageThumbnail;
    }

    public void setCoverImageThumbnail(String coverImageThumbnail) {
        this.coverImageThumbnail = coverImageThumbnail;
    }

    public int getYearlyPrice() {
        return yearlyPrice;
    }

    public void setYearlyPrice(int yearlyPrice) {
        this.yearlyPrice = yearlyPrice;
    }

    public String getWebsite() {
        return website == null ? "" : website;
    }

    public void setWebsite(String website) {
        this.website = website;
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

    public String getFirstName() {
        return firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMainLocation() {
        return mainLocation == null ? "" : mainLocation;
    }

    public void setMainLocation(String mainLocation) {
        this.mainLocation = mainLocation;
    }

    public long getDateOfJoining() {
        return dateOfJoining;
    }

    public void setDateOfJoining(long dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public boolean isIsfollow() {
        return isfollow;
    }

    public void setIsfollow(boolean isfollow) {
        this.isfollow = isfollow;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<GymBranchDetail> getGymBranchResponseList() {
        return gymBranchResponseList;
    }

    public void setGymBranchResponseList(List<GymBranchDetail> gymBranchResponseList) {
        this.gymBranchResponseList = gymBranchResponseList;
    }

    public List<MediaModel> getMediaResponseDtoList() {
        return mediaResponseDtoList;
    }

    public void setMediaResponseDtoList(List<MediaModel> mediaResponseDtoList) {
        this.mediaResponseDtoList = mediaResponseDtoList;
    }

    public List<ClassActivity> getClassCategoryList() {
        return classCategoryList;
    }

    public void setClassCategoryList(List<ClassActivity> classCategoryList) {
        this.classCategoryList = classCategoryList;
    }

    public List<QuestionAnswerModel> getCovidSafetyList() {
        return covidSafetyList;
    }

    public void setCovidSafetyList(List<QuestionAnswerModel> covidSafetyList) {
        this.covidSafetyList = covidSafetyList;
    }
}
