package com.p5m.me.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * Created by MyU10 on 3/13/2018.
 */

public class DateUtils {

    public static DateFormatSymbols dfs = new DateFormatSymbols(Locale.ENGLISH);

    private static SimpleDateFormat classTime = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
    private static SimpleDateFormat classTimeFormat = new SimpleDateFormat("h:mma", Locale.ENGLISH);
    private static SimpleDateFormat classDate = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
    private static SimpleDateFormat classDateSec = new SimpleDateFormat("dd-mm-yyyy", Locale.ENGLISH);
    private static SimpleDateFormat classDateExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
    private static SimpleDateFormat classDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy", Locale.ENGLISH);
    private static SimpleDateFormat packageDateFormat = new SimpleDateFormat("MMM d, yyyy", Locale.ENGLISH);
    private static SimpleDateFormat notificationDate = new SimpleDateFormat("h:mm a, MMM d", Locale.ENGLISH);

    public static String getMonthName(int monthCode) {
        String month = "wrong";
        String[] months = dfs.getShortMonths();
        if (monthCode >= 0 && monthCode <= 11) {
            month = months[monthCode];
        }
        return month;
    }

    public static String getWeekDaysName(int monthCode) {
        String month = "wrong";
        String[] months = dfs.getShortWeekdays();
        if (monthCode >= 0 && monthCode <= 11) {
            month = months[monthCode];
        }
        return month;
    }

    public static String getFormattedDobFromServer(Date date) {
        try {
            return classDate.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static String getFormattedDobFromDisplay(Date date) {
        try {
            return classDateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static int getDaysLeftFromPackageExpiryDate(String date) {
        try {
            date = date + " 23:59:00";
            Date expiryDate = classDateExpiry.parse(date);

            Date today = Calendar.getInstance(Locale.ENGLISH).getTime();
            long diff = expiryDate.getTime() - today.getTime();
//            LogUtils.debug("getDaysLeftFromPackageExpiryDate hours : " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS));
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return 0;
    }

    public static int canJoinClass(String classDateText, String packageExpiryDateText) {
        try {
            return classDate.parse(packageExpiryDateText).compareTo(classDate.parse(classDateText));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return 0;
    }

    public static String getClassTime(String from, String till) {
        try {
            return "  " + classTimeFormat.format(classTime.parse(from)).replace(".", "").toUpperCase() + " - " +
                    classTimeFormat.format(classTime.parse(till)).replace(".", "").toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static String getClassDate(String date) {
        try {
            return classDateFormat.format(classDate.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static String getPackageClassDate(String date) {
        try {
            return packageDateFormat.format(classDate.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static String getClassDateNotification(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        try {
            return notificationDate.format(new Date(time)).replace(".", "").toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static String getTransactionDate(long date) {
        try {
            Date time = Calendar.getInstance(Locale.ENGLISH).getTime();
            time.setTime(date);
            return classDateFormat.format(time);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }


    public static float hoursLeft(String date) {
        try {
            Date expiryTime = classDateExpiry.parse(date);
            Date today = Calendar.getInstance(Locale.ENGLISH).getTime();
            long diff = expiryTime.getTime() - today.getTime();
            float minute = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            return minute / 60f;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 2;
    }
}
