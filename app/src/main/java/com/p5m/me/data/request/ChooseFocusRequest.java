package com.p5m.me.data.request;

import com.p5m.me.data.main.ClassActivity;

import java.util.List;

public class ChooseFocusRequest implements java.io.Serializable {
    private static final long serialVersionUID = -6206322713950630950L;
    private List<ClassActivity> userDetailMappingList;
    private List<Integer> deletedUserDetailIds;

    public ChooseFocusRequest(List<ClassActivity> userDetailMappingList, List<Integer> deletedUserDetailIds) {
        this.userDetailMappingList = userDetailMappingList;
        this.deletedUserDetailIds = deletedUserDetailIds;
    }
}
