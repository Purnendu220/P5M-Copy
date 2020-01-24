package com.p5m.me.eventbus;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.BookWithFriendData;
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
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.LoginRegister.ContinueUser;

import java.util.ArrayList;
import java.util.List;

public class EventBroadcastHelper {

    private static String deviceToken;

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

        EventBroadcastHelper.sendDeviceUpdate(context);
        MixPanel.login(context);
    }

    public static void logout(Context context) {
        try {
            User user = MyPreferences.getInstance().getUser();
            List<ClassActivity> activities = MyPreferences.getInstance().getActivities();
            List<ClassModel> list = TempStorage.getSavedClasses();
            TempStorage.removeAllSavedClasses(list, context);
            MyPreferences.getInstance().clear();
            MyPreferences.getInstance().saveUser(user);
            MyPreferences.getInstance().saveActivities(activities);

            //Remove Filters
            MyPreferences.getInstance().saveFilters(new ArrayList<ClassesFilter>());
            TempStorage.setFilterList(new ArrayList<ClassesFilter>());

            TempStorage.setAuthToken(null);

            MixPanel.logout();

            NotificationManager notificationManager = (NotificationManager) context
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
            LoginManager.getInstance().logOut();

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

    public static void refreshFragment(){
        GlobalBus.getBus().post(new Events.refreshMembershipFragment());

    }
    public static void updateUpcomingList() {
        GlobalBus.getBus().post(new Events.UpdateUpcomingClasses());
    }

    public static void sendUserUpdate(Context context, User user) {
        TempStorage.setUser(context, user);

        MixPanel.trackUserUpdate(TempStorage.getUser());

        GlobalBus.getBus().post(new Events.UserUpdate(user));
    }

    /********************** CLASS JOINED OR PURCHASED ******************************/

    public static void sendPackagePurchased() {
        GlobalBus.getBus().post(new Events.PackagePurchased());
    }

    public static void sendClassJoin(Context context, ClassModel classModel, int changeAvailableSeatsFor) {
        if(changeAvailableSeatsFor == AppConstants.Values.CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS ) {
            if (classModel.isUserJoinStatus()) {
                classModel.setAvailableSeat(classModel.getAvailableSeat() - 1);
            } else if (!classModel.isUserJoinStatus()) {
                classModel.setAvailableSeat(classModel.getAvailableSeat() + 1);
            }
        }
        else if(changeAvailableSeatsFor == AppConstants.Values.UNJOIN_BOTH_CLASS) {
            if (classModel.isUserJoinStatus()) {
                classModel.setAvailableSeat(classModel.getAvailableSeat() - 2);
            } else {
                classModel.setAvailableSeat(classModel.getAvailableSeat() + 2);
            }
        }
        else if(changeAvailableSeatsFor == AppConstants.Values.UNJOIN_FRIEND_CLASS){
                classModel.setAvailableSeat(classModel.getAvailableSeat() + 1);
        }

        GlobalBus.getBus().post(new Events.ClassJoin(classModel));
    }

    public static void waitlistClassRemove(Context context, ClassModel classModel) {
        if (classModel.getWishType() != null) {
            classModel.setWishType(null);
        } else {
            classModel.setWishType(AppConstants.ApiParamKey.WAITLIST);
        }

        GlobalBus.getBus().post(new Events.WaitlistItemRemoved(classModel));
    }

    public static void waitlistClassJoin(Context context, ClassModel classModel) {

        classModel.setWishType(AppConstants.ApiParamKey.WAITLIST);
        GlobalBus.getBus().post(new Events.WaitlistJoin(classModel));
    }

    public static void sendclassRating(Context context, String className) {
        GlobalBus.getBus().post(new Events.ClassRating(className));
    }

    public static void classAutoJoin(Context context, ClassModel classModel) {
        GlobalBus.getBus().post(new Events.ClassAutoJoin(classModel));
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

    public static void sendBookWithFriendEvent(BookWithFriendData friendData) {
        GlobalBus.getBus().post(new Events.BookWithFriend(friendData));
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

     public static void exploreApiUpdate() {
        GlobalBus.getBus().post(new Events.ExploreApiUpdate());
    }
  public static void bannerUrlHandler(int innerTab) {
        GlobalBus.getBus().post(new Events.BannerUrlHandler(innerTab));
    }

    public static void sendDeviceUpdate(Context context) {

        try {
            if (!MyPreferences.getInstance().isLogin()) {
                return;
            }

            deviceToken = MyPreferences.getInstance().getDeviceToken();

            if (deviceToken == null) {
                FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener((Activity) context, new OnSuccessListener<InstanceIdResult>() {
                    @Override
                    public void onSuccess(InstanceIdResult instanceIdResult) {
                        String refreshedToken = instanceIdResult.getToken();
                        LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);
                        MyPreferences.getInstance().saveDeviceToken(refreshedToken);
                        deviceToken = refreshedToken;
                    }
                });
               /* String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);
                MyPreferences.getInstance().saveDeviceToken(refreshedToken);
                deviceToken = refreshedToken;*/
            }

            if (deviceToken == null) {
                return;
            }

            String androidId = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);

            String osVersion = android.os.Build.MODEL + "#" + Build.VERSION.RELEASE;

            NetworkCommunicator.getInstance(context).deviceUpdate(
                    new DeviceUpdate(TempStorage.version, TempStorage.getUser().getId(), deviceToken, androidId, osVersion),
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
