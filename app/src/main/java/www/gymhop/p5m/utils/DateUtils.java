package www.gymhop.p5m.utils;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by MyU10 on 3/13/2018.
 */

public class DateUtils {

    public static DateFormatSymbols dfs = new DateFormatSymbols();

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

    private static SimpleDateFormat classTime = new SimpleDateFormat("HH:mm:ss");
    private static SimpleDateFormat classTimeFormat = new SimpleDateFormat("h:mma");
    private static SimpleDateFormat classDate = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat classDateExpiry = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static SimpleDateFormat classDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

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
            return "  " + classTimeFormat.format(classTime.parse(from)) + " - " +
                    classTimeFormat.format(classTime.parse(till));
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


    public static int hoursLeft(String date) {
        try {
            Date expiryTime = classDateExpiry.parse(date);
            Date today = Calendar.getInstance().getTime();
            long diff = expiryTime.getTime() - today.getTime();
            return (int) TimeUnit.HOURS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 3;
    }
}
