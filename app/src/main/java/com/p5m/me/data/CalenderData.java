package com.p5m.me.data;

public class CalenderData {
    public String calenderId;
    public String calenderName;
    public int calenderStatus;

    public CalenderData(String calenderId, String calenderName, int calenderStatus) {
        this.calenderId = calenderId;
        this.calenderName = calenderName;
        this.calenderStatus = calenderStatus;
    }

    public String getCalenderId() {
        return calenderId;
    }

    public void setCalenderId(String calenderId) {
        this.calenderId = calenderId;
    }

    public String getCalenderName() {
        return calenderName;
    }

    public void setCalenderName(String calenderName) {
        this.calenderName = calenderName;
    }

    public int getCalenderStatus() {
        return calenderStatus;
    }

    public void setCalenderStatus(int calenderStatus) {
        this.calenderStatus = calenderStatus;
    }
}
