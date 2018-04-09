package www.gymhop.p5m.data.gym_class;

public class ClassModel implements java.io.Serializable {
    private static final long serialVersionUID = -7121053493736355823L;
    private int numberOfParticipants;
    private String classDate;
    private ClassMedia classMedia;
    private String reminder;
    private boolean hideClass;
    private String description;
    private String priceModel;
    private int availableSeat;
    private String classCategory;
    private String title;
    private boolean userJoinStatus;
    private int classId;
    private TrainerDetail trainerDetail;
    private int verifyStatus;
    private String specialNote;
    private int classSessionId;
    private String fromTime;
    private String shortDesc;
    private String classType;
    private String toTime;
    private int totalSeat;
    private GymBranchDetail gymBranchDetail;

    public int getNumberOfParticipants() {
        return this.numberOfParticipants;
    }

    public void setNumberOfParticipants(int numberOfParticipants) {
        this.numberOfParticipants = numberOfParticipants;
    }

    public String getClassDate() {
        return this.classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public ClassMedia getClassMedia() {
        return this.classMedia;
    }

    public void setClassMedia(ClassMedia classMedia) {
        this.classMedia = classMedia;
    }

    public String getReminder() {
        return this.reminder;
    }

    public void setReminder(String reminder) {
        this.reminder = reminder;
    }

    public boolean getHideClass() {
        return this.hideClass;
    }

    public void setHideClass(boolean hideClass) {
        this.hideClass = hideClass;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriceModel() {
        return this.priceModel;
    }

    public void setPriceModel(String priceModel) {
        this.priceModel = priceModel;
    }

    public int getAvailableSeat() {
        return this.availableSeat;
    }

    public void setAvailableSeat(int availableSeat) {
        this.availableSeat = availableSeat;
    }

    public String getClassCategory() {
        return this.classCategory;
    }

    public void setClassCategory(String classCategory) {
        this.classCategory = classCategory;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean getUserJoinStatus() {
        return this.userJoinStatus;
    }

    public void setUserJoinStatus(boolean userJoinStatus) {
        this.userJoinStatus = userJoinStatus;
    }

    public int getClassId() {
        return this.classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public TrainerDetail getTrainerDetail() {
        return this.trainerDetail;
    }

    public void setTrainerDetail(TrainerDetail trainerDetail) {
        this.trainerDetail = trainerDetail;
    }

    public int getVerifyStatus() {
        return this.verifyStatus;
    }

    public void setVerifyStatus(int verifyStatus) {
        this.verifyStatus = verifyStatus;
    }

    public String getSpecialNote() {
        return this.specialNote;
    }

    public void setSpecialNote(String specialNote) {
        this.specialNote = specialNote;
    }

    public int getClassSessionId() {
        return this.classSessionId;
    }

    public void setClassSessionId(int classSessionId) {
        this.classSessionId = classSessionId;
    }

    public String getFromTime() {
        return this.fromTime;
    }

    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    public String getShortDesc() {
        return this.shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public String getClassType() {
        return this.classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getToTime() {
        return this.toTime;
    }

    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    public int getTotalSeat() {
        return this.totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public GymBranchDetail getGymBranchDetail() {
        return this.gymBranchDetail;
    }

    public void setGymBranchDetail(GymBranchDetail gymBranchDetail) {
        this.gymBranchDetail = gymBranchDetail;
    }
}
