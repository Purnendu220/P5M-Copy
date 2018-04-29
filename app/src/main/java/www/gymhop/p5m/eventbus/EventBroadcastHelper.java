package www.gymhop.p5m.eventbus;

import android.content.Context;

import java.util.List;

import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.view.activity.LoginRegister.ContinueUser;

public class EventBroadcastHelper {

    public static void logout(Context context) {
        try {
            User user = MyPreferences.getInstance().getUser();
            List<ClassActivity> activities = MyPreferences.getInstance().getActivities();

            MyPreferences.getInstance().clear();
            MyPreferences.getInstance().saveUser(user);
            MyPreferences.getInstance().saveActivities(activities);

            TempStorage.setAuthToken(null);

            ContinueUser.open(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendUserUpdate(Context context, User user) {
        TempStorage.setUser(context, user);
        GlobalBus.getBus().post(new Events.UserUpdate(user));
    }

    public static void sendClassJoin(Context context, ClassModel classModel) {
        GlobalBus.getBus().post(new Events.ClassJoin(classModel));
    }

    public static void sendPackagePurchased() {
        GlobalBus.getBus().post(new Events.PackagePurchased());
    }

}
