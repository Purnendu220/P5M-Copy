package www.gymhop.p5m.eventbus;

import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;

import java.util.List;

import www.gymhop.p5m.data.main.ClassActivity;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.data.main.User;
import www.gymhop.p5m.data.request.DeviceUpdate;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.restapi.ResponseModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.LoginRegister.ContinueUser;

public class EventBroadcastHelper {

    public static void sendLogin(Context context, User user) {
        TempStorage.setUser(context, user);
        MyPreferences.getInstance().setLogin(true);
    }

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

    /********************** CLASS JOINED OR PURCHASED ******************************/

    public static void sendPackagePurchased() {
        GlobalBus.getBus().post(new Events.PackagePurchased());
    }

    public static void sendClassJoin(Context context, ClassModel classModel) {
        if (classModel.isUserJoinStatus()) {
            classModel.setAvailableSeat(classModel.getAvailableSeat() - 1);
        } else {
            classModel.setAvailableSeat(classModel.getAvailableSeat() + 1);
        }
        GlobalBus.getBus().post(new Events.ClassJoin(classModel));
    }

    public static void sendPackagePurchasedForClass(ClassModel classModel) {
        if (classModel.isUserJoinStatus()) {
            classModel.setAvailableSeat(classModel.getAvailableSeat() - 1);
        } else {
            classModel.setAvailableSeat(classModel.getAvailableSeat() + 1);
        }
        GlobalBus.getBus().post(new Events.PackagePurchasedForClass(classModel));
    }

    public static void sendClassPurchased(ClassModel classModel) {
        if (classModel.isUserJoinStatus()) {
            classModel.setAvailableSeat(classModel.getAvailableSeat() - 1);
        } else {
            classModel.setAvailableSeat(classModel.getAvailableSeat() + 1);
        }
        GlobalBus.getBus().post(new Events.ClassPurchased(classModel));
    }
    /////////////////////////////////////////////////////////////////////////////

    public static void sendWishAdded(ClassModel classModel) {
        GlobalBus.getBus().post(new Events.WishAdded(classModel));
    }

    public static void sendWishRemoved(ClassModel classModel) {
        GlobalBus.getBus().post(new Events.WishRemoved(classModel));
    }

    public static void trainerFollowUnFollow(TrainerModel trainerModel, boolean isFollowed) {
        GlobalBus.getBus().post(new Events.TrainerFollowed(trainerModel, isFollowed));
    }

    public static void sendNewFilterSet() {
        GlobalBus.getBus().post(new Events.NewFilter());
    }

    public static void notificationReceived(Context context) {
        MyPreferences.initialize(context).saveNotificationCount(MyPreferences.initialize(context).getNotificationCount() + 1);
        GlobalBus.getBus().post(new Events.NotificationReceived());
    }

    public static void sendDeviceUpdate(Context context) {

        if (!MyPreferences.getInstance().isLogin()) {
            return;
        }

        String deviceToken = MyPreferences.getInstance().getDeviceToken();

        if (deviceToken == null) {
            String refreshedToken = FirebaseInstanceId.getInstance().getToken();
            LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);
            MyPreferences.getInstance().saveDeviceToken(refreshedToken);
        }

        if (deviceToken == null) {
            return;
        }

        NetworkCommunicator.getInstance(context).deviceUpdate(
                new DeviceUpdate(TempStorage.version, TempStorage.getUser().getId(), deviceToken),
                new NetworkCommunicator.RequestListener() {
                    @Override
                    public void onApiSuccess(Object response, int requestCode) {
                        try {
                            Boolean forceUpdate = ((ResponseModel<Boolean>) response).data;
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }
                    }

                    @Override
                    public void onApiFailure(String errorMessage, int requestCode) {
                    }
                }, false);
    }

}
