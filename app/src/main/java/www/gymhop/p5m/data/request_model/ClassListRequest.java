package www.gymhop.p5m.data.request_model;

import www.gymhop.p5m.data.CityLocality;

public class ClassListRequest implements java.io.Serializable {
    private static final long serialVersionUID = -9109485815849361072L;
    private CityLocality[] locationList;
    private String classDate;
    private String[] timingList;
    private int size;
    private int page;
    private String[] activityList;
    private int userId;
    private String[] genderList;

    public ClassListRequest() {
    }

    public CityLocality[] getLocationList() {
        return locationList;
    }

    public void setLocationList(CityLocality[] locationList) {
        this.locationList = locationList;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public String[] getTimingList() {
        return timingList;
    }

    public void setTimingList(String[] timingList) {
        this.timingList = timingList;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String[] getActivityList() {
        return activityList;
    }

    public void setActivityList(String[] activityList) {
        this.activityList = activityList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String[] getGenderList() {
        return genderList;
    }

    public void setGenderList(String[] genderList) {
        this.genderList = genderList;
    }
}
