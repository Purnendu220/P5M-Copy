package www.gymhop.p5m.storage.preferences;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.ClassesFilter;
import www.gymhop.p5m.data.User;
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

    public String getAuthToken() {
        return PreferencesManager.getString(AppConstants.Pref.AUTH_TOKEN);
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

    public List<City> getCities() {
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.CITIES), new TypeToken<List<City>>(){}.getType());
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
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.ACTIVITIES), new TypeToken<List<ClassActivity>>(){}.getType());
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
        try {
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.FILTERS), new TypeToken<List<ClassesFilter>>(){}.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
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
            return gson.fromJson(PreferencesManager.getString(AppConstants.Pref.USER), new TypeToken<User>(){}.getType());
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
