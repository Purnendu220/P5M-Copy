package com.p5m.me.data;

import java.util.List;

public class LimitExceedResponseData {
        Integer gymId;
        Integer numberOfWeek;
        Integer readyPckSize;
        Boolean generalPck;
        List<BookWithFriendData> userList;


    public LimitExceedResponseData(Integer gymId, Integer numberOfWeek, Integer readyPckSize, Boolean generalPck, List<BookWithFriendData> userList) {
        this.gymId = gymId;
        this.numberOfWeek = numberOfWeek;
        this.readyPckSize = readyPckSize;
        this.generalPck = generalPck;
        this.userList = userList;
    }

    public Integer getGymId() {
        return gymId;
    }

    public void setGymId(Integer gymId) {
        this.gymId = gymId;
    }

    public Integer getNumberOfWeek() {
        return numberOfWeek;
    }

    public void setNumberOfWeek(Integer numberOfWeek) {
        this.numberOfWeek = numberOfWeek;
    }

    public Integer getReadyPckSize() {
        return readyPckSize;
    }

    public void setReadyPckSize(Integer readyPckSize) {
        this.readyPckSize = readyPckSize;
    }

    public Boolean getGeneralPck() {
        return generalPck;
    }

    public void setGeneralPck(Boolean generalPck) {
        this.generalPck = generalPck;
    }

    public List<BookWithFriendData> getUserList() {
        return userList;
    }

    public void setUserList(List<BookWithFriendData> userList) {
        this.userList = userList;
    }
}
