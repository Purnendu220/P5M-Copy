package com.p5m.me.storage.preferences;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.data.City;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.RatingParamModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.User;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.JsonUtils;
import com.p5m.me.utils.LogUtils;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MyPreferences {

    public static Context context;
    private static MyPreferences myPreferences;
    private Gson gson;

    private MyPreferences(Context context) {
        this.context = context;
        gson = new Gson();
    }

    public static MyPreferences initialize(Context context) {
        new PreferencesManager(context).setName(AppConstants.Pref.NAME).init();

        if (myPreferences == null) {
            myPreferences = new MyPreferences(context);
        }
        return myPreferences;
    }

    public static MyPreferences getInstance() {
        if (myPreferences == null) {
            LogUtils.debug("MyPreferences not initialized");
        }
        return myPreferences;
    }

    public void clear() {
        PreferencesManager.clear();
    }

    public String getAuthToken() {
        return PreferencesManager.getString(AppConstants.Pref.AUTH_TOKEN, "");
    }

    public void saveAuthToken(String authToken) {
        if (authToken == null) {
            authToken = "";
        }
        PreferencesManager.putString(AppConstants.Pref.AUTH_TOKEN, authToken);
    }

    public boolean isLogin() {
        return PreferencesManager.getBoolean(AppConstants.Pref.LOGIN, false);
    }

    public void setLogin(boolean isLogin) {
        PreferencesManager.putBoolean(AppConstants.Pref.LOGIN, isLogin);
    }

    public String getDeviceToken() {
        return PreferencesManager.getString(AppConstants.Pref.DEVICE_TOKEN, null);
    }

    public void saveDeviceToken(String deviceToken) {
        PreferencesManager.putString(AppConstants.Pref.DEVICE_TOKEN, deviceToken);
    }

    public int getNotificationCount() {
        LogUtils.debug("Notification getNotificationCount" + PreferencesManager.getInt(AppConstants.Pref.NOTIFICATION_COUNT, 0));
        return PreferencesManager.getInt(AppConstants.Pref.NOTIFICATION_COUNT, 0);
    }

    public void saveNotificationCount(int notificationCount) {
        PreferencesManager.putInt(AppConstants.Pref.NOTIFICATION_COUNT, notificationCount);
        LogUtils.debug("Notification saveNotificationCount" + getNotificationCount());
    }

    public List<City> getCities() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.CITIES), new TypeToken<List<City>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveCities(List<City> activities) {
        try {
            PreferencesManager.putString(AppConstants.Pref.CITIES, gson.toJson(activities).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public List<ClassActivity> getActivities() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.ACTIVITIES), new TypeToken<List<ClassActivity>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public void saveActivities(List<ClassActivity> activities) {
        try {
            PreferencesManager.putString(AppConstants.Pref.ACTIVITIES, gson.toJson(activities).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public void saveRatingParams(List<RatingParamModel> ratingParamList) {
        try {
            PreferencesManager.putString(AppConstants.Pref.RATING_PARAM, gson.toJson(ratingParamList).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public List<RatingParamModel> getRatingParams() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.RATING_PARAM), new TypeToken<List<RatingParamModel>>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
    public List<ClassesFilter> getFilters() {
        List<ClassesFilter> classesFilters = new ArrayList<>();
        try {

            JSONArray jsonArray = new JSONArray(PreferencesManager.getString(AppConstants.Pref.FILTERS));

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = jsonArray.getJSONObject(index);

                ClassesFilter classesFilter = new ClassesFilter(
                        jsonObject.getString("id"),
                        false,
                        jsonObject.getString("objectClassName"),
                        jsonObject.getString("name"),
                        jsonObject.getInt("iconResource"),
                        jsonObject.getInt("type"));

                JSONObject object = jsonObject.getJSONObject("object");

                if (classesFilter.getObjectClassName().equals("CityLocality")) {

                    CityLocality model = new CityLocality();
                    model.setId(object.getInt("id"));
                    model.setLatitude(object.getDouble("latitude"));
                    model.setLongitude(object.getDouble("longitude"));
                    model.setName(object.getString("name"));
                    classesFilter.setObject(model);

                } else if (classesFilter.getObjectClassName().equals("ClassActivity")) {

                    ClassActivity model = new ClassActivity(object.getString("name"), object.getInt("id"));
                    classesFilter.setObject(model);

                } else if (classesFilter.getObjectClassName().equals("Time")) {

                    Filter.Time model = new Filter.Time(object.getString("id"), object.getString("name"));
                    classesFilter.setObject(model);

                } else if (classesFilter.getObjectClassName().equals("Gender")) {

                    Filter.Gender model = new Filter.Gender(object.getString("id"), object.getString("name"));
                    classesFilter.setObject(model);

                }
                else if (classesFilter.getObjectClassName().equals("Gym")) {

                    Filter.Gym model = new Filter.Gym(object.getInt("id")+"", object.getString("name"));
                    classesFilter.setObject(model);

                }

                classesFilters.add(classesFilter);
            }

            return classesFilters;
//            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.FILTERS), new TypeToken<List<ClassesFilter>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classesFilters;
    }

    public void saveFilters(List<ClassesFilter> filterList) {
        try {
            PreferencesManager.putString(AppConstants.Pref.FILTERS, gson.toJson(filterList).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public void saveJoinedClassList(List<ClassModel> classList) {
        try {
            String joinedClassesList = JsonUtils.toJson(classList);
            PreferencesManager.putString(AppConstants.Pref.JOINED_CLASSES,joinedClassesList);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public List<ClassModel> getJoinedClassList() {
        try{
            String joinedClassList = PreferencesManager.getString(AppConstants.Pref.JOINED_CLASSES);
            if (joinedClassList != null) {
                List<ClassModel> classList = gson.fromJson(joinedClassList, new TypeToken<List<ClassModel>>(){}.getType());
                return classList;
            }
        }catch (Exception e){
e.printStackTrace();
        }
        return null;
        }




    public User getUser() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.USER), new TypeToken<User>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveUser(User user) {
        try {
            PreferencesManager.putString(AppConstants.Pref.USER, gson.toJson(user).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public DefaultSettingServer getDefaultSettingServer() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.DEFAULT_SETTING_SERVER), new TypeToken<DefaultSettingServer>() {
            }.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void saveDefaultSettingServer(DefaultSettingServer defaultSettingServer) {
        try {
            PreferencesManager.putString(AppConstants.Pref.DEFAULT_SETTING_SERVER, gson.toJson(defaultSettingServer).toString());
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
}
