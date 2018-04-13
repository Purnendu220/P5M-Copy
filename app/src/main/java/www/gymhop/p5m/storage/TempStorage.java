package www.gymhop.p5m.storage;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.ClassesFilter;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.storage.preferences.MyPreferences;

public class TempStorage {

    private static int userId;
    public static String version;
    private static String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM3NSwiY3JlYXRlZCI6MTUyMjkxMDgwNDQ2MywiaWQiOiIxNjdlNTBhZC02YmViLTQ3ZGEtYThmNC1lMTYxMGEyNTczYzIiLCJleHAiOjIxMjc3MTA4MDR9.MRfwMBagPtrQTkb8WiRr2ifx6wEWXJ5BZb0cFFRFPsk";

    private static List<ClassActivity> activities;
    private static List<City> cities;
    private static User user;

    private static List<ClassesFilter> filterList;


    public static List<City> getCities() {
//        if (cities == null) {
//            cities = MyPreferences.createFragment().getCities();
//        }
        return cities;
    }

    public static void setCities(List<City> cities) {
        TempStorage.cities = cities;
    }

    public static List<ClassActivity> getActivities() {
//        if (activities == null) {
//            activities = MyPreferences.createFragment().getActivities();
//        }
        return activities;
    }

    public static void setActivities(List<ClassActivity> activities) {
        TempStorage.activities = activities;
    }

    public static String getAuthToken() {
        return authToken;
    }

    public static void setAuthToken(String authToken) {
        TempStorage.authToken = authToken;
    }

    public static List<ClassesFilter> getFilters() {
        if (filterList == null) {
            filterList = MyPreferences.getInstance().getFilters();
            if (filterList == null) {
                filterList = new ArrayList<ClassesFilter>();
            }
        }
        return filterList;
    }

    public static void setFilterList(List<ClassesFilter> filterList) {
        TempStorage.filterList = filterList;
        MyPreferences.getInstance().saveFilters(filterList);
    }

    public static User getUser() {
        if (user == null) {
            user = MyPreferences.getInstance().getUser();
        }
        return user;
    }

    public static void setUser(Context context, User user) {
        TempStorage.user = user;
        MyPreferences.getInstance().saveUser(user);
    }
}
