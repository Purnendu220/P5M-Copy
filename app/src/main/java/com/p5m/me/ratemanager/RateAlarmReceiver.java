package com.p5m.me.ratemanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.p5m.me.R;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

public class RateAlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.debug("Alarm works");
        //ClassModel model = null;

        int classSessionId = intent.getIntExtra(AppConstants.Pref.CLASS_MODEL,-1);
        if(classSessionId>0){
            Intent myIntent2 = new Intent(context, ClassFinishNotificationService.class);
            myIntent2.putExtra(AppConstants.Pref.CLASS_MODEL,classSessionId);
            context.startService(myIntent2);



        }

    }

}
