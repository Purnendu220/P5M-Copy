package www.gymhop.p5m.data.request_model;

import java.util.List;

import www.gymhop.p5m.data.CityLocality;

public class ClassListRequest implements java.io.Serializable {
    private static final long serialVersionUID = -9109485815849361072L;

    private List<CityLocality> locationList;
    private List<String> timingList;
    private List<String> activityList;
    private List<String> genderList;

    private String classDate;
    private int userId;
    private int size;
    private int page;

    public ClassListRequest() {
    }

    public List<CityLocality> getLocationList() {
        return locationList;
    }

    public void setLocationList(List<CityLocality> locationList) {
        this.locationList = locationList;
    }

    public String getClassDate() {
        return classDate;
    }

    public void setClassDate(String classDate) {
        this.classDate = classDate;
    }

    public List<String> getTimingList() {
        return timingList;
    }

    public void setTimingList(List<String> timingList) {
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

    public List<String> getActivityList() {
        return activityList;
    }

    public void setActivityList(List<String> activityList) {
        this.activityList = activityList;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public List<String> getGenderList() {
        return genderList;
    }

    public void setGenderList(List<String> genderList) {
        this.genderList = genderList;
    }
}
