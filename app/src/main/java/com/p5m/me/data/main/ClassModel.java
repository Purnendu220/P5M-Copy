package com.p5m.me.data.main;

import com.p5m.me.data.ClassRatingListData;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class ClassModel implements java.io.Serializable {
    private static final long serialVersionUID = -7121053493736355823L;

    private int numberOfParticipants;
    private String classDate;
    private MediaModel classMedia;
    private String reminder;
    private String description;
    private int availableSeat;
    private String classCategory;
    private String title;
    private boolean userJoinStatus;
    private int classId;
    private TrainerModel trainerDetail;
    private int verifyStatus;
    private String specialNote;
    private int classSessionId;
    private String fromTime;
    private String shortDesc;
    private String classType;
    private String toTime;
    private int totalSeat;
    private GymBranchDetail gymBranchDetail;
    private Integer refBookingId;

    /*************Special Class Addition****************/
    private String priceModel;
    private String specialClassRemark;
    private boolean hideClass;
    private float price;

    /********From Joined class**********/
    private int joinClassId;

    /********From wish list**********/
    private String classDay;
    private int wishListId;
    private String classImage;

    /***********************For Class Rating********************/
    private long numberOfRating;
    private float rating;
    private ClassRatingListData ratingResDto;

    private String fitnessLevel;
    private boolean isExpired;

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public ClassModel(String title, String classDate, String fromTime, String toTime, long dataID) {
        this.title=title;
        this.classDate=classDate;
        this.fromTime=fromTime;
        this.toTime=toTime;
        this.classSessionId=(int)dataID;
    }
    //////////////////////////////////

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ClassModel && ((ClassModel) obj).getClassSessionId() == getClassSessionId()) {
            return true;
        }
        return super.equals(obj);
    }

    public ClassRatingListData getRatingResDto() {
        return ratingResDto;
    }

    public void setRatingResDto(ClassRatingListData ratingResDto) {
        this.ratingResDto = ratingResDto;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getClassDate() {
        return classDate == null ? "" : classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public MediaModel getClassMedia() {
        return classMedia;
    }

    public void setClassMedia(MediaModel classMedia) {
        this.classMedia = classMedia;
    }

    public String getReminder() {
        return reminder == null ? "" : reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean isHideClass() {
        return hideClass;
    }

    public void setHideClass(boolean hideClass) {
        this.hideClass = hideClass;
    }

    public String getDescription() {
        return description == null ? "" : description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public String getClassCategory() {
        return classCategory == null ? "" : classCategory;
    }

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    public String getTitle() {
        return title == null ? "" : title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isUserJoinStatus() {
        return userJoinStatus;
    }

    public void setUserJoinStatus(boolean userJoinStatus) {
        this.userJoinStatus = userJoinStatus;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public TrainerModel getTrainerDetail() {
        return trainerDetail;
    }

    public void setTrainerDetail(TrainerModel trainerDetail) {
        this.trainerDetail = trainerDetail;
    }

    public int getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getSpecialNote() {
        return specialNote == null ? "" : specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getFromTime() {
        return fromTime == null ? "" : fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getShortDesc() {
        return shortDesc == null ? "" : shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getClassType() {
        return classType == null ? "" : classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getToTime() {
        return toTime == null ? "" : toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public GymBranchDetail getGymBranchDetail() {
        return gymBranchDetail;
    }

    public void setGymBranchDetail(GymBranchDetail gymBranchDetail) {
        this.gymBranchDetail = gymBranchDetail;
    }

    public String getClassDay() {
        return classDay == null ? "" : classDay;
    }

    public void setClassDay(String classDay) {
        this.classDay = classDay;
    }

    public int getWishListId() {
        return wishListId;
    }

    public void setWishListId(int wishListId) {
        this.wishListId = wishListId;
    }

    public String getClassImage() {
        return classImage == null ? "" : classImage;
    }

    public void setClassImage(String classImage) {
        this.classImage = classImage;
    }

    public String getSpecialClassRemark() {
        return specialClassRemark == null ? "" : specialClassRemark;
    }

    public void setSpecialClassRemark(String specialClassRemark) {
        this.specialClassRemark = specialClassRemark;
    }

    public String getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(String priceModel) {
        this.priceModel = priceModel;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getJoinClassId() {
        return joinClassId;
    }

    public void setJoinClassId(int joinClassId) {
        this.joinClassId = joinClassId;
    }

    public long getNumberOfRating() {
        return numberOfRating;
    }

    public void setNumberOfRating(long numberOfRating) {
        this.numberOfRating = numberOfRating;
    }

    public float getRating() {
            return  rating;



    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public Integer getRefBookingId() {
        return refBookingId;
    }

    public void setRefBookingId(Integer refBookingId) {
        this.refBookingId = refBookingId;
    }
}
