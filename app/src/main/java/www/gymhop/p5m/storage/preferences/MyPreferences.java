package www.gymhop.p5m.storage.preferences;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnlin.preferencesmanager.PreferencesManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.CityLocality;
import www.gymhop.p5m.data.ClassesFilter;
import www.gymhop.p5m.data.Filter;
import www.gymhop.p5m.data.main.ClassActivity;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;


public class MyPreferences {

    private Context context;
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
        return PreferencesManager.getString(AppConstants.Pref.AUTH_TOKEN, null);
    }

    public void saveAuthToken(String authToken) {
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
}
