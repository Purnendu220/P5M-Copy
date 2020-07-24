package com.p5m.me.utils;

import android.util.Log;

import com.p5m.me.data.main.ClassModel;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
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

    public static void reInitialize() {

        dfs = new DateFormatSymbols(Locale.getDefault());

        classTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        classTimeFormat = new SimpleDateFormat("h:mma", Locale.getDefault());
        classDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        classDateSec = new SimpleDateFormat("dd-mm-yyyy", Locale.getDefault());
        classDateExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        classDateTime = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.getDefault());
        eventDateTime = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());

        classDateFormat = new SimpleDateFormat("EEE, d MMM yyyy", Locale.getDefault());
        packageDateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
        notificationDate = new SimpleDateFormat("h:mm a, MMM d", Locale.getDefault());
        classRatingDate = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        classTime24Format = new SimpleDateFormat("HH:mm", Locale.getDefault());
        classTime12Format = new SimpleDateFormat("hh:mm aa", Locale.getDefault());
        ExtendedDateFormat = new SimpleDateFormat("d MMM ", Locale.getDefault());
    }

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

    public static String getHours(String time) {
        try {
            String[] o = (time.split(":"));
            return o[0];
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

    public static int getPackageNumberOfDays(int duration, String pakageValidity) {
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

    public static boolean isDateisPast(String date, String date1) {
        try {
            Date dateObjectOne = classDate.parse(date);
            Date dateObjectTwo = classDate.parse(date1);

            return dateObjectOne.before(dateObjectTwo);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }

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

    public static Timestamp getTimespanDate(String date) {
        try {
//            Date date1 = classDateExpiry.parse(date);
            Date date1 = classDate.parse(date);
            Timestamp timeStampDate = new Timestamp(date1.getTime());
            return timeStampDate;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return new Timestamp(System.nanoTime());
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


            long diff = (expiryTime);
            long minute = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS);
            return minute;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return today;
    }

    public static long eventTime(String date) {
        long today = Calendar.getInstance().getTimeInMillis();
        try {
            long expiryTime = parseTime(date);
            long diff = (expiryTime);
            long minute = TimeUnit.MILLISECONDS.convert(diff, TimeUnit.MILLISECONDS);
            return minute;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return today;
    }

    private static long parseTime(String date) {
        long today = Calendar.getInstance().getTimeInMillis();

        SimpleDateFormat eventDateTimeLocal = new SimpleDateFormat("dd-MM-yyyy hh:mm aa", Locale.getDefault());
        SimpleDateFormat eventDateTimeLocalOne = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

        List<SimpleDateFormat> knownPatterns = new ArrayList<SimpleDateFormat>();
        knownPatterns.add(eventDateTimeLocal);
        knownPatterns.add(eventDateTimeLocalOne);

        for (SimpleDateFormat pattern : knownPatterns) {
            try {
                String timezoneID = TimeZone.getDefault().getID();
                pattern.setTimeZone(TimeZone.getTimeZone(timezoneID));
                long classTime = pattern.parse(date).getTime();
                return classTime;
            } catch (ParseException pe) {
            }
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

    public static String getCurrentDateandTime() {
        try {

            Log.d("DATEEEEEEEE", DateFormat.getDateTimeInstance().format(new Date()));
            return DateFormat.getDateTimeInstance().format(new Date());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
        return "";
    }

    public static String findDifference(String date, Date endDate) {
        //milliseconds
        try {
            Date startDate = classDateExpiry.parse(date);
            long different = endDate.getTime() - startDate.getTime();

            long secondsInMilli = 1000;
            long minutesInMilli = secondsInMilli * 60;
            long hoursInMilli = minutesInMilli * 60;
            long daysInMilli = hoursInMilli * 24;

            long elapsedDays = different / daysInMilli;
            different = different % daysInMilli;

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            long elapsedMinutes = different / minutesInMilli;
            different = different % minutesInMilli;

            long elapsedSeconds = different / secondsInMilli;
            if (elapsedDays < 0)
                elapsedDays = -elapsedDays;
            String timeDifference = elapsedDays + " days " + elapsedHours + " hours " + elapsedMinutes + " minutes";
            return timeDifference;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
       /* System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);*/
    }

    public static double find5MinDifference(Date startDate, Date endDate) {
        try {

            long mills = endDate.getTime() - startDate.getTime();
            long hours = mills / (1000 * 60 * 60);
            long mins = (mills / (1000 * 60)) % 60;
//            long diffMinutes = diff / (60 * 1000) % 60;
            if (hours < 1)
                return mins;
            else
                return 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 10;

    }

    public static boolean isTimePassed(String date, String fromTime) {
        try {
            date = date + " " + fromTime;
            Date endDate = classDateExpiry.parse(date);
            Date startDate = Calendar.getInstance().getTime();
            if (startDate.after(endDate)) {
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;

    }
    public static boolean isTimeCame(String date, String fromTime) {
        try {
            date = date + " " + fromTime;
            Date endDate = classDateExpiry.parse(date);
            Date startDate = Calendar.getInstance().getTime();
            long diff = endDate.getTime() - startDate.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;
            if(diffMinutes<=AppConstants.TIME_START_DURATION){
                return true;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;

    }
    public static boolean isTimeCame(ClassModel model){
        try {
            String dateStartString= model.getClassDate() + " " + model.getFromTime();
            Date dateStart = classDateExpiry.parse(dateStartString);
            Date dateNow = Calendar.getInstance().getTime();

          return dateNow.after(dateStart);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static double getClassCompletionPercentage(ClassModel model,long timeInClass){
        try {
            Date start = classTime.parse(model.getFromTime());
            Date end = classTime.parse(model.getToTime());
            long diff = end.getTime()-start.getTime();
            long classTimeInSeconds = diff/1000;
            double percentage = ((double) timeInClass/classTimeInSeconds)*100;
            return percentage;
        } catch (ParseException e) {
            e.printStackTrace();
        }
    return 0;
    }

    public static String getDateEnglish(String date){
        String dateStr=date;
        try {
        SimpleDateFormat classDateEng = new SimpleDateFormat("yyyy-MM-dd", new Locale("en"));

            Date cla = classDate.parse(date);
            dateStr = classDateEng.format(cla);


        } catch (ParseException e) {
            e.printStackTrace();
        }

    return dateStr;
    }

}
