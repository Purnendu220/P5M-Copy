package www.gymhop.p5m.eventbus;

import android.content.Context;

import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.storage.TempStorage;

public class EventBroadcastHelper {

    public static void sendUserUpdate(Context context, User user) {
        TempStorage.setUser(context, user);
        GlobalBus.getBus().post(new Events.UserUpdate(user));
    }

    public static void sendClassJoin(Context context, ClassModel classModel) {
        GlobalBus.getBus().post(new Events.ClassJoin(classModel));
    }
}
