package www.gymhop.p5m.utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;

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
    private static SimpleDateFormat classDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

    public static String getClassTime(String from, String till) {
        try {
            return classTimeFormat.format(classTime.parse(from)) + " - " +
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


}
