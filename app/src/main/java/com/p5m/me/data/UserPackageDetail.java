package com.p5m.me.data;

import com.p5m.me.data.main.GymBranchDetail;

public class UserPackageDetail {

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
}
