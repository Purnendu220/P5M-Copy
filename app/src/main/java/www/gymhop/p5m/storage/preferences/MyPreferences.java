package www.gymhop.p5m.storage.preferences;

import android.content.Context;

import com.shawnlin.preferencesmanager.PreferencesManager;

import java.util.List;

import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;


public class MyPreferences {

    private Context context;
    private static MyPreferences myPreferences;

    private MyPreferences(Context context) {
        this.context = context;
    }

    public static MyPreferences getInstance(Context context) {
        if (myPreferences == null) {
            myPreferences = new MyPreferences(context);
        }
        return myPreferences;
    }


    public boolean isLogin() {
        return false;
    }

    public static List<ClassActivity> getActivities() {
        return PreferencesManager.getObject("activities", null);
    }

    public void saveActivities(List<ClassActivity> activities) {
        PreferencesManager.putObject("activities", activities);
    }

    public static List<City> getCities() {
        return PreferencesManager.getObject("cities", null);
    }

    public void saveCities(List<City> activities) {
        PreferencesManager.putObject("cities", activities);
    }
}
