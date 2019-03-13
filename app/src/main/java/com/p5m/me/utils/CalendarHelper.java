package com.p5m.me.utils;


import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.data.CalenderData;
import com.p5m.me.data.main.ClassModel;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.TimeZone;

public class CalendarHelper {

    //Remember to initialize this activityObj first, by calling initActivityObj(this) from
//your activity
    private static final String TAG = "CalendarHelper";
    public static final int CALENDARED_PERMISSION_REQUEST_CODE = 99;
    public static long oneHour = 1000 * 60 * 60;

    public static void scheduleCalenderEvent(Context caller,ClassModel model) {

        long eventStartTime = DateUtils.eventTime(model.getClassDate() + " " + model.getFromTime());
        int calendarId = CalendarHelper.getCalenderId(caller);
        if(eventStartTime-oneHour>0 && calendarId>-1){
            ContentResolver cr = caller.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(Events.DTSTART, eventStartTime - oneHour);
        values.put(Events.DTEND, eventStartTime);
        values.put(Events.TITLE, model.getTitle());
        String url = getUrlBase() + "/share/classes/" + model.getClassSessionId() + "/" + model.getTitle();
        values.put(Events.DESCRIPTION, url);
        values.put(Events.CALENDAR_ID, calendarId);
        values.put(Events.STATUS, Events.STATUS_CONFIRMED);
        values.put(Events._ID,model.getClassSessionId());
        values.put(Events.ALL_DAY, false);
        values.put(Events.HAS_ALARM, true);

        values.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Uri uri = cr.insert(Events.CONTENT_URI, values);

            ContentValues reminders = new ContentValues();
            reminders.put(Reminders.EVENT_ID, model.getClassSessionId());
            reminders.put(Reminders.METHOD, Reminders.METHOD_ALERT);
            reminders.put(Reminders.MINUTES, model.getClassSessionId());

            if (ActivityCompat.checkSelfPermission(caller, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Uri uri2 = cr.insert(Reminders.CONTENT_URI, reminders);

        }


    }

    public static void updateEvent(Context caller, ClassModel classModel ) {
        long eventStartTime = DateUtils.eventTime(classModel.getClassDate() + " " + classModel.getFromTime());
        ContentResolver cr = caller.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.TITLE, classModel.getTitle());
        values.put(Events.DTSTART, eventStartTime - oneHour);
        values.put(Events.DTEND, eventStartTime);
        values.put(Events.DESCRIPTION, caller.getString(R.string.your_class_schedule_at)+" " + classModel.getFromTime()+" - "+ classModel.getToTime());

        Uri updateUri = ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, classModel.getClassSessionId());
        int rows = caller.getContentResolver().update(updateUri, values, null, null);
        Log.i("Calendar", "Rows updated: " + rows);
    }

    public static int deleteEvent(int classSessionId, Context caller) {
        int iNumRowsDeleted = 0;

        Uri eventUri = ContentUris
                .withAppendedId(getCalendarUriBase(), classSessionId);
        iNumRowsDeleted = caller.getContentResolver().delete(eventUri, null, null);

        return iNumRowsDeleted;
    }

    private static Uri getCalendarUriBase() {
        Uri eventUri;
        if (android.os.Build.VERSION.SDK_INT <= 7) {
            // the old way

            eventUri = Uri.parse("content://calendar/events");
        } else {
            // the new way

            eventUri = Uri.parse("content://com.android.calendar/events");
        }

        return eventUri;
    }






    public static void requestCalendarReadWritePermission(Context caller) {
        List<String> permissionList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(caller, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_CALENDAR);

        }

        if (ContextCompat.checkSelfPermission(caller, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_CALENDAR);

        }

        if (permissionList.size() > 0) {
            String[] permissionArray = new String[permissionList.size()];

            for (int i = 0; i < permissionList.size(); i++) {
                permissionArray[i] = permissionList.get(i);
            }

            ActivityCompat.requestPermissions((Activity) caller,
                    permissionArray,
                    CALENDARED_PERMISSION_REQUEST_CODE);
        }

    }

    public static boolean haveCalendarReadWritePermissions(Context caller) {
        int permissionCheck = ContextCompat.checkSelfPermission(caller,
                Manifest.permission.READ_CALENDAR);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            permissionCheck = ContextCompat.checkSelfPermission(caller,
                    Manifest.permission.WRITE_CALENDAR);

            if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }

        return false;
    }


    public static ArrayList<CalenderData> listCalendarId(Context c) {

        if (haveCalendarReadWritePermissions(c)) {


            String projection[] = {"_id", "calendar_displayName","visible"};
            Uri calendars;
            calendars = Uri.parse("content://com.android.calendar/calendars");

            ContentResolver contentResolver = c.getContentResolver();
            Cursor managedCursor = contentResolver.query(calendars, projection, null, null, null);

            if (managedCursor.moveToFirst()) {
                String calName;
                String calID;
                int calStatus;

                int cont = 0;
                int nameCol = managedCursor.getColumnIndex(projection[1]);
                int idCol = managedCursor.getColumnIndex(projection[0]);
                int idVisible = managedCursor.getColumnIndex(projection[2]);
                ArrayList<CalenderData> calenderList = new ArrayList<>();
                do {
                    calName = managedCursor.getString(nameCol);
                    calID = managedCursor.getString(idCol);
                    calStatus = managedCursor.getInt(idVisible);
                    calenderList.add(new CalenderData(calID,calName,calStatus));
                    LogUtils.debug("CalendarName:" + calName + " ,id:" + calID+ " ,isVisible:" + calStatus);
                    cont++;
                } while (managedCursor.moveToNext());
                managedCursor.close();

                return calenderList;
            }

        }

        return null;

    }
    public static int getCalenderId(Context context){
        int calendar_id=-1;
        ArrayList<CalenderData> calendarIdTable = null;
         if (calendarIdTable == null) {
            calendarIdTable = CalendarHelper.listCalendarId(context);
        }
        if(calendarIdTable.size()!=0) {
            for (int i=0;i<calendarIdTable.size();i++) {
                CalenderData entry=calendarIdTable.get(i);
                if(entry.getCalenderStatus()==1){
                    calendar_id = Integer.parseInt(entry.getCalenderId());
                    break;

                }
            }
            String calendarString = "@";
            for (int i=0;i<calendarIdTable.size();i++) {
                CalenderData entry=calendarIdTable.get(i);
                  if(entry.getCalenderName().contains(calendarString) && entry.getCalenderStatus()==1){
                      calendar_id = Integer.parseInt(entry.getCalenderId());
                      break;

                  }

            }

    }
    return calendar_id;
    }
    private static String getUrlBase() {
        String urlBase = BuildConfig.BASE_URL_SHARE;

        return urlBase;
    }
}