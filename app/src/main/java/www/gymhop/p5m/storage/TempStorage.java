package www.gymhop.p5m.storage;

import java.util.List;

import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.storage.preferences.MyPreferences;

public class TempStorage {

    private static int userId;
    public static String version;
    private static String authToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOjM3NSwiY3JlYXRlZCI6MTUyMjkxMDgwNDQ2MywiaWQiOiIxNjdlNTBhZC02YmViLTQ3ZGEtYThmNC1lMTYxMGEyNTczYzIiLCJleHAiOjIxMjc3MTA4MDR9.MRfwMBagPtrQTkb8WiRr2ifx6wEWXJ5BZb0cFFRFPsk";

    private static List<ClassActivity> activities;
    private static List<City> cities;

    public static List<City> getCities() {
        if (cities == null) {
            cities = MyPreferences.getCities();
        }
        return cities;
    }

    public static void setCities(List<City> cities) {
        TempStorage.cities = cities;
    }

    public static List<ClassActivity> getActivities() {
        if (activities == null) {
            activities = MyPreferences.getActivities();
        }
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
}
