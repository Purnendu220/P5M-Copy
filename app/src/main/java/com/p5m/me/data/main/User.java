package com.p5m.me.data.main;

import java.util.List;

public class User implements java.io.Serializable {
    private static final long serialVersionUID = -7020619477594468968L;
    private long lastPasswordResetDate;
    private String profileImageThumbnail;
    private String gender;
    private int numberOfTrainer;
    private boolean favTrainerNotification;
    private long lastActiveDate;
    private long facebookId;
    private String profileImage;
    private String firstName;
    private long dateOfJoining;
    private String nationality;
    private String userCategory;
    private String location;
    private Long dob;
    private int userProfileImageId;
    private int id;
    private int followerCount;
    private int numberOfTransactions;
    private String email;
    private String mobile;
    private boolean isfollow;
    private boolean status;
    private List<UserPackage> userPackageDetailDtoList;
    private List<ClassActivity> classCategoryList;

    public int getNumberOfTransactions() {
        return numberOfTransactions;
    }

    public void setNumberOfTransactions(int numberOfTransactions) {
        this.numberOfTransactions = numberOfTransactions;
    }

    public long getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(long facebookId) {
        this.facebookId = facebookId;
    }

    public long getLastPasswordResetDate() {
        return this.lastPasswordResetDate;
    }

    public void setLastPasswordResetDate(long lastPasswordResetDate) {
        this.lastPasswordResetDate = lastPasswordResetDate;
    }

    public String getProfileImageThumbnail() {
        return this.profileImageThumbnail == null ? "" : profileImageThumbnail;
    }

    public void setProfileImageThumbnail(String profileImageThumbnail) {
        this.profileImageThumbnail = profileImageThumbnail;
    }

    public String getGender() {
        return this.gender == null ? "" : gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getNumberOfTrainer() {
        return this.numberOfTrainer;
    }

    public void setNumberOfTrainer(int numberOfTrainer) {
        this.numberOfTrainer = numberOfTrainer;
    }

    public boolean getFavTrainerNotification() {
        return this.favTrainerNotification;
    }

    public void setFavTrainerNotification(boolean favTrainerNotification) {
        this.favTrainerNotification = favTrainerNotification;
    }

    public long getLastActiveDate() {
        return this.lastActiveDate;
    }

    public void setLastActiveDate(long lastActiveDate) {
        this.lastActiveDate = lastActiveDate;
    }

    public List<UserPackage> getUserPackageDetailDtoList() {
        return userPackageDetailDtoList;
    }

    public void setUserPackageDetailDtoList(List<UserPackage> userPackageDetailDtoList) {
        this.userPackageDetailDtoList = userPackageDetailDtoList;
    }

    public String getProfileImage() {
        return this.profileImage == null ? "" : profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getFirstName() {
        return this.firstName == null ? "" : firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public long getDateOfJoining() {
        return this.dateOfJoining;
    }

    public void setDateOfJoining(long dateOfJoining) {
        this.dateOfJoining = dateOfJoining;
    }

    public String getNationality() {
        return this.nationality == null ? "" : nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getLocation() {
        return location == null ? "" : location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserCategory() {
        return this.userCategory == null ? "" : userCategory;
    }

    public void setUserCategory(String userCategory) {
        this.userCategory = userCategory;
    }

    public Long getDob() {
        return this.dob;
    }

    public void setDob(Long dob) {
        this.dob = dob;
    }

    public int getUserProfileImageId() {
        return this.userProfileImageId;
    }

    public void setUserProfileImageId(int userProfileImageId) {
        this.userProfileImageId = userProfileImageId;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFollowerCount() {
        return this.followerCount;
    }

    public void setFollowerCount(int followerCount) {
        this.followerCount = followerCount;
    }

    public String getEmail() {
        return this.email == null ? "" : email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile == null ? "" : mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public boolean getIsfollow() {
        return this.isfollow;
    }

    public void setIsfollow(boolean isfollow) {
        this.isfollow = isfollow;
    }

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ClassActivity> getClassCategoryList() {
        return classCategoryList;
    }

    public void setClassCategoryList(List<ClassActivity> classCategoryList) {
        this.classCategoryList = classCategoryList;
    }
}
