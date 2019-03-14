package com.p5m.me.utils;

import com.p5m.me.fxn.utility.Constants;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

/**
 * Created by MyU10 on 3/13/2018.
 */

public class DateUtils {

    public static DateFormatSymbols dfs = new DateFormatSymbols(Locale.getDefault());

    private static SimpleDateFormat classTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    private static SimpleDateFormat classTimeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
    private static SimpleDateFormat classDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private static SimpleDateFormat classDateSec = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
    private static SimpleDateFormat classDateExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
    private static SimpleDateFormat classDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
    private static SimpleDateFormat eventDateTime = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());

    private static SimpleDateFormat classDateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
    private static SimpleDateFormat packageDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
    private static SimpleDateFormat notificationDate = new SimpleDateFormat("h:mm a, MMM d", Locale.getDefault());
    private static SimpleDateFormat classRatingDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
    private static SimpleDateFormat classTime24Format = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private static SimpleDateFormat classTime12Format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
    private static SimpleDateFormat ExtendedDateFormat = new SimpleDateFormat("d MMM ", Locale.getDefault());


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
    public static int getPackageNumberOfDays(int duration,String pakageValidity){
        int numberOfDays = duration;
        switch (pakageValidity) {
            case "DAYS":
                numberOfDays *= 1;
                break;
            case "WEEKS":
                numberOfDays *= 7;
                break;
            case "MONTHS":
                numberOfDays *= 30;
                break;
            case "YEARS":
                numberOfDays *= 365;
                break;
        }
        return numberOfDays;
    }

    public static int getDaysLeftFromPackageExpiryDate(String date) {
        try {
            date = date + " 23:59:00";
            Date expiryDate = classDateExpiry.parse(date);

            Date today = Calendar.getInstance().getTime();
            long diff = expiryDate.getTime() - today.getTime();
//            LogUtils.debug("getDaysLeftFromPackageExpiryDate hours : " + TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS));
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return 0;
    }

    public static String getExtendedExpiryDate(String date, int days) {
        try {
            date = date + " 23:59:00";
            Date expiryDate = classDateExpiry.parse(date);
            return addDaysToDate(expiryDate, days * 7);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return date;
    }

    public static String addDaysToDate(Date date, int days) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, days);
        String output = ExtendedDateFormat.format(c.getTime());
        return output;
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
            return " " + classTimeFormat.format(classTime.parse(from)).replace(".", "").toUpperCase() + " - " +
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


    public static String getDateFormatter(Date date) {
        try {
            return packageDateFormat.format(date);
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

    public static String getRatingDate(long time) {
        try {
            return classRatingDate.format(new Date(time));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }


    public static String getTransactionDate(long date) {
        try {
            Date time = Calendar.getInstance().getTime();
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
            Date today = Calendar.getInstance().getTime();
            long diff = expiryTime.getTime() - today.getTime();
            float minute = TimeUnit.MINUTES.convert(diff, TimeUnit.MILLISECONDS);
            return minute / 60f;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 2;
    }

    public static long eventStartTime(String date) {
        long today = Calendar.getInstance().getTimeInMillis();
        try {
            long expiryTime = classDateExpiry.parse(date).getTime();


            long diff = (expiryTime );
            long minute = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS);
            return minute;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return today;
    }
    public static long eventTime(String date) {
        long today = Calendar.getInstance().getTimeInMillis();
        SimpleDateFormat eventDateTimeLocal = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        try {
            String timezoneID = TimeZone.getDefault().getID();
            eventDateTimeLocal.setTimeZone(TimeZone.getTimeZone(timezoneID));
            long expiryTime = eventDateTimeLocal.parse(date).getTime();


            long diff = (expiryTime );

            long minute = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS);
            return minute;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return today;
    }

    public static String getHourDiff(float hourDiff) {
        String diffHrs = "";
        if (hourDiff <= 6) {
            diffHrs = "less than 6 hrs";
        } else if (hourDiff <= 12) {
            diffHrs = "6 to 12 hrs";
        } else if (hourDiff <= 24) {
            diffHrs = "12 to 24 hrs";
        } else {
            diffHrs = "greater than 24 hrs";
        }
        return diffHrs;
    }

    public static String getDayTiming(String time) {
        String timing = "";

        try {
            Date dateTime = classDateExpiry.parse(time);
            Calendar timeCal = Calendar.getInstance();
            timeCal.setTime(dateTime);

            int hourOfDay = timeCal.get(Calendar.HOUR_OF_DAY);

            if (hourOfDay < 12) {
                timing = "Morning_Time";
            } else if (hourOfDay < 16) {
                timing = "Afternoon";
            } else {
                timing = "Evening_Time";
            }

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        return timing;
    }

    public static Date getClassDate(String classDate, String classTime) {
        try {
            TimeZone tz = TimeZone.getDefault();
            classDateTime.setTimeZone(tz);
            String time24 = classTime24Format.format(classTime12Format.parse(classTime));
            Date classDateObject = classDateTime.parse(classDate + " " + time24);

            return classDateObject;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }

    public static Date getClassDate24hour(String classDate, String classtime) {
        try {
            TimeZone tz = TimeZone.getDefault();
            classDateTime.setTimeZone(tz);
            String time24 = classTime.format(classtime);
            Date classDateObject = classDateTime.parse(classDate + " " + time24);

            return classDateObject;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

    }
}
