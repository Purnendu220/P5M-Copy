package com.p5m.me.ratemanager;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.p5m.me.R;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.HomeActivity;

public class ClassFinishNotificationService extends Service {
    private NotificationManager mNM;
    private int NOTIFICATION = 23323;
    private Context mContext;

    @Override
    public void onCreate() {
        mContext=this;
        mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);
        ClassModel model= null;
        int classSessionId = intent.getIntExtra(AppConstants.DataKey.CLASS_MODEL,-1);
        LogUtils.debug("intent.getIntExtra(AppConstants.Pref.CLASS_MODEL "+classSessionId);


        if(classSessionId>0){
            if(TempStorage.getSavedClasses()!=null&&TempStorage.getSavedClasses().size()>0){
            for(ClassModel classModel:TempStorage.getSavedClasses()){
                if(classModel.getClassSessionId()==classSessionId){
                    model= classModel;
                }
            }
            }
            if(model!=null){
                showNotification(model);
try {
    TempStorage.removeSavedClassOnly(model.getClassSessionId(),mContext);

}catch (Exception e){
    e.printStackTrace();
}

            }
        }


        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        // Cancel the persistent notification.
        mNM.cancel(NOTIFICATION);

        // Tell the user we stopped.
        Toast.makeText(this, R.string.local_service_stopped, Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void showNotification(ClassModel model) {
        NOTIFICATION=model.getClassSessionId();
        Intent navigationIntent = HomeActivity.createIntent(mContext, AppConstants.Tab.TAB_FIND_CLASS, 0,model);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
        stackBuilder.addNextIntentWithParentStack(navigationIntent);
        stackBuilder.editIntentAt(0).putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);

        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(NOTIFICATION, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        CharSequence text = String.format(getString(R.string.rate_class_message),model.getTitle());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "p5m_channel_" + System.currentTimeMillis(); // The id of the channel.
            CharSequence name = "p5m"; // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.app_icon_small)
                    .setContentTitle(getText(R.string.app_name))
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.app_icon))
                    .setContentIntent(resultPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setContentText(text)
                    .build();
            notificationManager.notify(NOTIFICATION, notification);

        }else{
            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.bigText(text);

            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_small)
                    .setContentTitle(getText(R.string.app_name))
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                            R.mipmap.app_icon))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)
                    .setContentText(text)
                    .build();
            notificationManager.notify(NOTIFICATION, notification);

        }
    }
}