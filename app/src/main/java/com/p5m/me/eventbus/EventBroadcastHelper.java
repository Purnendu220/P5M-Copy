package com.p5m.me.eventbus;

import android.app.NotificationManager;
import android.content.Context;

import com.google.firebase.iid.FirebaseInstanceId;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.DeviceUpdate;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.LoginRegister.ContinueUser;

import java.util.ArrayList;
import java.util.List;

public class EventBroadcastHelper {

    public static void sendLogin(Context context, User user) {

        // Saving filters for previous user...
//        try {
//            if (user.getId() != TempStorage.getUser().getId()) {
//                //Reset Filter..
//                MyPreferences.getInstance().saveFilters(new ArrayList<ClassesFilter>());
//                TempStorage.setFilterList(new ArrayList<ClassesFilter>());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        TempStorage.setUser(context, user);
        MyPreferences.getInstance().setLogin(true);
        NetworkCommunicator.getInstance(context).getDefault();

//        MixPanel.setup(context);
        EventBroadcastHelper.sendDeviceUpdate(context);
    }

    public static void logout(Context context) {
        try {
            User user = MyPreferences.getInstance().getUser();
            List<ClassActivity> activities = MyPreferences.getInstance().getActivities();

            MyPreferences.getInstance().clear();
            MyPreferences.getInstance().saveUser(user);
            MyPreferences.getInstance().saveActivities(activities);

            //Remove Filters
            MyPreferences.getInstance().saveFilters(new ArrayList<ClassesFilter>());

            TempStorage.setAuthToken(null);

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();

            ContinueUser.open(context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePackage(Context context) {
        NetworkCommunicator.getInstance(context).getMyUser(new NetworkCommunicator.RequestListener() {
            @Override
            public void onApiSuccess(Object response, int requestCode) {
            }

            @Override
            public void onApiFailure(String errorMessage, int requestCode) {
            }
        }, false);
        GlobalBus.getBus().post(new Events.UpdatePackage());
    }

    public static void updateUpcomingList() {
        GlobalBus.getBus().post(new Events.UpdateUpcomingClasses());
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

    public static void sendRefreshClassList() {
        GlobalBus.getBus().post(new Events.RefreshClassList());
    }

    public static void notificationCountUpdated(Context context) {
        GlobalBus.getBus().post(new Events.NotificationCountUpdated());
    }

    public static void sendDeviceUpdate(Context context) {

        try {
            if (!MyPreferences.getInstance().isLogin()) {
                return;
            }

            String deviceToken = MyPreferences.getInstance().getDeviceToken();

            if (deviceToken == null) {
                String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);
                MyPreferences.getInstance().saveDeviceToken(refreshedToken);
                deviceToken = refreshedToken;
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

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


}