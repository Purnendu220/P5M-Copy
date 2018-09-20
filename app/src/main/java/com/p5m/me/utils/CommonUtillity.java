package com.p5m.me.utils;

import android.content.Context;

public class CommonUtillity {

    public static String getnerateUniqueToken(Context context){
       return RefrenceWrapper.getRefrenceWrapper(context).getDeviceId()+System.currentTimeMillis();
    }

}
