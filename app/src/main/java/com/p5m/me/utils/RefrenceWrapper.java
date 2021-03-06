package com.p5m.me.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.custom.CustomRateAlertDialog;


public class RefrenceWrapper {
    public static RefrenceWrapper refrenceWrapper;
    private Context context,appContext;
    private CustomRateAlertDialog customRateAlertDialog;
    private HomeActivity activity;
    private RefrenceWrapper(Context activity) {
        this.context = activity;
    }
    private int myProfileTabPosition= ProfileHeaderTabViewHolder.TAB_1;
    private String deviceId;
    private  LatLng position = null;


    public static RefrenceWrapper getRefrenceWrapper(Context context) {
        if (refrenceWrapper == null) {
            refrenceWrapper = new RefrenceWrapper(context);
        }
        return refrenceWrapper;
    }

    public CustomRateAlertDialog getCustomRateAlertDialog() {
        return customRateAlertDialog;
    }

    public void setCustomRateAlertDialog(CustomRateAlertDialog customRateAlertDialog) {
        this.customRateAlertDialog = customRateAlertDialog;
    }

    public HomeActivity getActivity() {
        return activity;
    }

    public void setActivity(HomeActivity activity) {
        this.activity = activity;
    }

    public int getMyProfileTabPosition() {
        return myProfileTabPosition;
    }

    public void setMyProfileTabPosition(int myProfileTabPosition) {
        this.myProfileTabPosition = myProfileTabPosition;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Context getAppContext() {
        return appContext;
    }

    public void setAppContext(Context appContext) {
        this.appContext = appContext;
    }

    public void setLatLng(LatLng position){
        if(position!=null){
            this.position=position;
        }
    }
    public LatLng getLatLng(){
        return position;
    }


}