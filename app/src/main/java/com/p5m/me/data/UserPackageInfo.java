package com.p5m.me.data;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;

/**
 * Created by Varun John on 4/16/2018.
 */

public class UserPackageInfo {

    public boolean havePackages = false;

    public boolean haveGeneralPackage = false;
    public boolean haveDropInPackage = false;
    public int dropInPackageCount = 0;
    public int generalPackageCount = 0;
    public boolean haveActiveSubscription = false;

    public UserPackage userPackageGeneral = null;
    public List<UserPackage> userPackageReady = null;


    public UserPackageInfo(User user) {

        if (user.getUserPackageDetailDtoList() != null && !user.getUserPackageDetailDtoList().isEmpty()) {
            userPackageReady = new ArrayList<>(user.getUserPackageDetailDtoList().size());

            for (UserPackage userPackage : user.getUserPackageDetailDtoList()) {
                if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL)&&userPackage.getBalance()>0) {
                    haveGeneralPackage = true;
                    userPackageGeneral = userPackage;
                    generalPackageCount++;
                    havePackages = true;
                    if(userPackage.isSubscriped()&&userPackage.getSubscriptionStatus()!=null&&(userPackage.getSubscriptionStatus().equalsIgnoreCase(AppConstants.SubscriptionStatus.ACTIVE)||userPackage.getSubscriptionStatus().equalsIgnoreCase(AppConstants.SubscriptionStatus.UPGRADE))){
                        haveActiveSubscription = true;
                    }
                }
            }
        } else {
            havePackages = false;
        }

    }


}
