package com.p5m.me.data.main;


import java.io.Serializable;

public class ScheduleClassModel implements Serializable {
    /**
     * classId : 1140
     * title : Strong Flow
     * shortDesc : Postures are sequenced to flow together connecting body to breath. In this dynamic class you will gain core strength, develop focus and build stamina. Above all, this class invites you to move like yo
     * description : Postures are sequenced to flow together connecting body to breath. In this dynamic class you will gain core strength, develop focus and build stamina. Above all, this class invites you to move like you. To intuitively listen to what your body needs in the present moment.*We are strictly implementing the Late Policy. Students who are late even 1 minute is not allowed to join the class to avoid distraction from the other students. Please come 10 minutes before the class.*We require everyone to book your class thru mindbody app to reserve your slot.*If you choose to not show up due to any circumstance, please cancel your booking to allow other students on the waiting list to join the class.
     * classCategory : POWER YOGA
     * classSessionId : 56280
     * classType : MIXED
     * price : 0.0
     * classDate : 2019-08-05
     * fromTime : 17:30:00
     * toTime : 18:30:00
     * availableSeat : 1
     * totalSeat : 1
     * reminder : If you cancel up to 2 hours before the start time of the class, your class will be credited back to your P5M balance. If you miss a class without notification or cancel within 2 hours before the start time of the class, it will result in the class being deducted from your balance, with no class refund.
     * userJoinStatus : false
     * verifyStatus : 2
     * hideClass : false
     * priceModel : CHARGABLE
     * trainerDetail : {"id":8232,"firstName":"Dalal","trainerStatus":true,"trainerDeleted":false}
     * gymBranchDetail : {"branchId":9,"localityName":"Shuwaikh","localityId":105,"branchName":"Toby's Estate ","address":"Shuwaikh Industrial, Kuwait","latitude":29.3409835,"longitude":47.942023500000005,"gymName":"P5M APP","gymId":6,"mediaId":2,"mediaUrl":"http://d1zfy71n00v47t.cloudfront.net/gymprofile/c65f0652-b417-4f1d-a457-a44f1710a312.png","mediaThumbNailUrl":"http://d1zfy71n00v47t.cloudfront.net/gymprofile/c65f0652-b417-4f1d-a457-a44f1710a312_thumbnail_.png","studioInstruction":""}
     * numberOfParticipants : 0
     * rating : 3.77
     * specialClassRemark :
     * fitnessLevel : INTERMEDIATE
     */

    private int classId;
    private String title;
    private String shortDesc;
    private String description;
    private String classCategory;
    private int classSessionId;
    private String classType;
    private double price;
    private String classDate;
    private String fromTime;
    private String toTime;
    private int availableSeat;
    private int totalSeat;
    private String reminder;
    private boolean userJoinStatus;
    private int verifyStatus;
    private boolean hideClass;
    private String priceModel;
    private TrainerDetailBean trainerDetail;
    private GymBranchDetailBean gymBranchDetail;
    private int numberOfParticipants;
    private double rating;
    private String specialClassRemark;
    private String fitnessLevel;

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getClassCategory() {
        return classCategory;
    }

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    public int getClassSessionId() {
        return classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String getFromTime() {
        return fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getToTime() {
        return toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public int getAvailableSeat() {
        return availableSeat;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public String getReminder() {
        return reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean isUserJoinStatus() {
        return userJoinStatus;
    }

    public void setUserJoinStatus(boolean userJoinStatus) {
        this.userJoinStatus = userJoinStatus;
    }

    public int getVerifyStatus() {
        return verifyStatus;
    }

    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public boolean isHideClass() {
        return hideClass;
    }

    public void setHideClass(boolean hideClass) {
        this.hideClass = hideClass;
    }

    public String getPriceModel() {
        return priceModel;
    }

    public void setPriceModel(String priceModel) {
        this.priceModel = priceModel;
    }

    public TrainerDetailBean getTrainerDetail() {
        return trainerDetail;
    }

    public void setTrainerDetail(TrainerDetailBean trainerDetail) {
        this.trainerDetail = trainerDetail;
    }

    public GymBranchDetailBean getGymBranchDetail() {
        return gymBranchDetail;
    }

    public void setGymBranchDetail(GymBranchDetailBean gymBranchDetail) {
        this.gymBranchDetail = gymBranchDetail;
    }

    public int getNumberOfParticipants() {
        return numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getSpecialClassRemark() {
        return specialClassRemark;
    }

    public void setSpecialClassRemark(String specialClassRemark) {
        this.specialClassRemark = specialClassRemark;
    }

    public String getFitnessLevel() {
        return fitnessLevel;
    }

    public void setFitnessLevel(String fitnessLevel) {
        this.fitnessLevel = fitnessLevel;
    }

    public static class TrainerDetailBean {
        /**
         * id : 8232
         * firstName : Dalal
         * trainerStatus : true
         * trainerDeleted : false
         */

        private int id;
        private String firstName;
        private boolean trainerStatus;
        private boolean trainerDeleted;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public boolean isTrainerStatus() {
            return trainerStatus;
        }

        public void setTrainerStatus(boolean trainerStatus) {
            this.trainerStatus = trainerStatus;
        }

        public boolean isTrainerDeleted() {
            return trainerDeleted;
        }

        public void setTrainerDeleted(boolean trainerDeleted) {
            this.trainerDeleted = trainerDeleted;
        }
    }

    public static class GymBranchDetailBean {
        /**
         * branchId : 9
         * localityName : Shuwaikh
         * localityId : 105
         * branchName : Toby's Estate
         * address : Shuwaikh Industrial, Kuwait
         * latitude : 29.3409835
         * longitude : 47.942023500000005
         * gymName : P5M APP
         * gymId : 6
         * mediaId : 2
         * mediaUrl : http://d1zfy71n00v47t.cloudfront.net/gymprofile/c65f0652-b417-4f1d-a457-a44f1710a312.png
         * mediaThumbNailUrl : http://d1zfy71n00v47t.cloudfront.net/gymprofile/c65f0652-b417-4f1d-a457-a44f1710a312_thumbnail_.png
         * studioInstruction :
         */

        private int branchId;
        private String localityName;
        private int localityId;
        private String branchName;
        private String address;
        private double latitude;
        private double longitude;
        private String gymName;
        private int gymId;
        private int mediaId;
        private String mediaUrl;
        private String mediaThumbNailUrl;
        private String studioInstruction;

        public int getBranchId() {
            return branchId;
        }

        public void setBranchId(int branchId) {
            this.branchId = branchId;
        }

        public String getLocalityName() {
            return localityName;
        }

        public void setLocalityName(String localityName) {
            this.localityName = localityName;
        }

        public int getLocalityId() {
            return localityId;
        }

        public void setLocalityId(int localityId) {
            this.localityId = localityId;
        }

        public String getBranchName() {
            return branchName;
        }

        public void setBranchName(String branchName) {
            this.branchName = branchName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getGymName() {
            return gymName;
        }

        public void setGymName(String gymName) {
            this.gymName = gymName;
        }

        public int getGymId() {
            return gymId;
        }

        public void setGymId(int gymId) {
            this.gymId = gymId;
        }

        public int getMediaId() {
            return mediaId;
        }

        public void setMediaId(int mediaId) {
            this.mediaId = mediaId;
        }

        public String getMediaUrl() {
            return mediaUrl;
        }

        public void setMediaUrl(String mediaUrl) {
            this.mediaUrl = mediaUrl;
        }

        public String getMediaThumbNailUrl() {
            return mediaThumbNailUrl;
        }

        public void setMediaThumbNailUrl(String mediaThumbNailUrl) {
            this.mediaThumbNailUrl = mediaThumbNailUrl;
        }

        public String getStudioInstruction() {
            return studioInstruction;
        }

        public void setStudioInstruction(String studioInstruction) {
            this.studioInstruction = studioInstruction;
        }
    }
}
