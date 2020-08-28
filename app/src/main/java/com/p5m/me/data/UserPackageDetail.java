package com.p5m.me.data;

import com.p5m.me.data.main.GymBranchDetail;

public class UserPackageDetail {

    String gymName;
    long classDate;
    int credit;

    String numberofGymVisit;
    GymBranchDetail gymBranchResponseDto;

    public String getNumberofGymVisit() {
        return numberofGymVisit;
    }

    public void setNumberofGymVisit(String numberofGymVisit) {
        this.numberofGymVisit = numberofGymVisit;
    }

    public GymBranchDetail getGymBranchResponseDto() {
        return gymBranchResponseDto;
    }

    public void setGymBranchResponseDto(GymBranchDetail gymBranchResponseDto) {
        this.gymBranchResponseDto = gymBranchResponseDto;
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public long getClassJoinDate() {
        return classDate;
    }

    public void setClassJoinDate(long classJoinDate) {
        this.classDate = classJoinDate;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }
}
