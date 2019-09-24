package com.p5m.me.ratemanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.p5m.me.data.Join5MinModel;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

import org.json.JSONObject;

import java.util.List;

public class RateAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.debug("Alarm works");
        //ClassModel model = null;

        int classSessionId = intent.getIntExtra(AppConstants.Pref.CLASS_MODEL,-1);
        if(classSessionId>0){
            try{
                Intent myIntent2 = new Intent(context, ClassFinishNotificationService.class);
                myIntent2.putExtra(AppConstants.Pref.CLASS_MODEL,classSessionId);
                context.startService(myIntent2);
            }catch (Exception e){
                e.printStackTrace();
            }
            }

    }




}
