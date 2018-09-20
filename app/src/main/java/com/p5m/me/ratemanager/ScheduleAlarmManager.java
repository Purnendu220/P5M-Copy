package com.p5m.me.ratemanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.p5m.me.data.main.ClassModel;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

public class ScheduleAlarmManager {
    public static void setReminder(Context context, Class<?> cls, ClassModel model)
    {
        String classDate=model.getClassDate();
        String classTime=model.getToTime();
        int DAILY_REMINDER_REQUEST_CODE=model.getClassSessionId();

        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.setTime(DateUtils.getClassDate(classDate,classTime));
        cancelReminder(context,cls,DAILY_REMINDER_REQUEST_CODE);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.putExtra(AppConstants.Pref.CLASS_MODEL,model.getClassSessionId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),pendingIntent);
    }
    public static void setReminder(Context context, Class<?> cls,String classDate,String classTime)
    {
        int DAILY_REMINDER_REQUEST_CODE=5000;

        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.setTime(DateUtils.getClassDate(classDate,classTime));
        cancelReminder(context,cls,DAILY_REMINDER_REQUEST_CODE);

        // Enable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        intent1.putExtra(AppConstants.Pref.CLASS_MODEL,5000);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.setExact(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),pendingIntent);
    }
    public static void cancelReminder(Context context,Class<?> cls,int DAILY_REMINDER_REQUEST_CODE)
    {
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
    }


}
