package com.p5m.me.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.adapters.viewholder.ProfileHeaderTabViewHolder;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CalendarHelper;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.EditProfileActivity;
import com.p5m.me.view.activity.Main.GymProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.MemberShip;
import com.p5m.me.view.activity.Main.NotificationActivity;
import com.p5m.me.view.activity.Main.SettingActivity;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;
import com.p5m.me.view.activity.Main.TransactionHistoryActivity;

import org.json.JSONObject;


import java.util.Hashtable;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Context context;
    private Hashtable<String, String> calendarIdTable;
    private int calendar_id = -1;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context = this;

        LogUtils.debug("Notifications Received");

        if (remoteMessage == null)
            return;

        try {

            LogUtils.debug("Notifications Data: " + remoteMessage.getData());
            String message = remoteMessage.getData().get("message");

            if (message != null) {

                JSONObject json = new JSONObject(message);
                handleDataMessage(json);
                handleDataMessageForNotificationSchedule(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void handleDataMessageForNotificationSchedule(JSONObject json) {
        try {


            JSONObject jsonObject = json;
            String type = jsonObject.optString(AppConstants.Notification.TYPE);

            long dataID = jsonObject.optLong(AppConstants.Notification.OBJECT_DATA_ID);
            try {
                String userIdToNotify = jsonObject.optString(AppConstants.Notification.USER_ID_TO_NOTIFY);
                int userId = Integer.parseInt(userIdToNotify);

                if (TempStorage.getUser() != null && TempStorage.getUser().getId() != userId) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }

            switch (type) {
                case "OnClassRefund":
                    if (dataID != 0L && dataID > 0) {
                        TempStorage.removeSavedClass((int) dataID, context);
                        removeEvent(jsonObject, dataID);
                    }
                    break;
                case "CustomerCancelClass":
                    if (dataID != 0L && dataID > 0) {
                        TempStorage.removeSavedClass((int) dataID, context);
                        removeEvent(jsonObject, dataID);
                    }
                    break;
                case "OnClassUpdateByGYM":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);
                   break;
                case "OnClassUpdateByTrainer":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);

                    break;
                case "OnSessionUpdateByGYM":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);

                    break;
                case "OnSessionUpdateByTrainerOfGym":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);

                   break;
                case "OnSessionUpdateByTrainer":
                    TempStorage.removeSavedClass((int) dataID, context);
                    updateEvent(jsonObject, dataID);

                    break;
                case "OnSessionUpdateByGymOfTrainer":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);

                    break;
                case "OnClassUpdateByCms":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);

                    break;
                case "OnGroupClassUpdateByCms":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);

                    break;
                case "OnJoinClass":
                    setNotification(jsonObject, dataID);
                    addEvent(jsonObject, dataID);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
        bookEvent(model);
    }

    private void removeEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
//        updateEvent(model);
        CalendarHelper.deleteEventId(model.getClassSessionId()
                ,context);
    }
    private void updateEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel classModel = new ClassModel(title, classDate, fromTime, toTime, dataID);
        CalendarHelper.updateEvent(this,classModel);
    }



    private void bookEvent(ClassModel model) {
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            CalendarHelper.scheduleCalenderEvent(this,model);
        } else {
            CalendarHelper.requestCalendarReadWritePermission(this);

        }

    }

    private void removeEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
//        updateEvent(model);
        CalendarHelper.deleteEventId(model.getClassSessionId()
                ,context);
    }
    private void updateEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
        updateEvent(model);
//        CalendarHelper.deleteEventId(model.getTitle()+DateUtils.getClassTime(model.getFromTime(), model.getToTime())
//                ,context);
    }



    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject jsonObject = json;
            String type = jsonObject.optString(AppConstants.Notification.TYPE);
            String message = jsonObject.optString(AppConstants.Notification.BODY);

            long dataID = jsonObject.optLong(AppConstants.Notification.OBJECT_DATA_ID);

            try {
                String userIdToNotify = jsonObject.optString(AppConstants.Notification.USER_ID_TO_NOTIFY);
                int userId = Integer.parseInt(userIdToNotify);

                if (TempStorage.getUser() != null && TempStorage.getUser().getId() != userId) {
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }

            Intent navigationIntent = null;

            /**************************** Refresh ********************************/
            switch (type) {

                case "OnSessionDeleteByGYM":
                case "OnClassDeleteByTrainer":
                case "OnSessionDeleteByGymOfTrainer":
                case "OnSessionDeleteByTrainerOfGym":
                case "OnSessionDeleteByTrainer":
                case "OnClassDeleteByGym":
                case "OnClassDeleteByTrainerOfGym":
                case "OnClassInActive":
                case "OnSlotDeleteByTrainer":
                case "OnSlotDeleteByGymOfTrainer":
                case "OnSlotDeleteByGym":
                case "OnSlotDeletByTrainerOfGym":
                case "OnPaymentSuccess":
                case "OnClassCancelByCMS":
                case "OnBookingCancelToCustomerByCMS":
                case "OnBookingCancelToGymByCMS":
                case "OnBookingCancelToTrainerByCMS":
                case "OnBookingCancelByCMS":
                case "OnClassUpdateByGYM":
                case "OnClassUpdateByTrainer":
                case "OnSessionUpdateByGYM":
                case "OnSessionUpdateByTrainerOfGym":
                case "OnSessionUpdateByTrainer":
                case "OnSessionUpdateByGymOfTrainer":
                case "OnClassUpdateByCms":
                case "OnClassUpdateByCMS":
                case "OnGroupClassUpdateByCms":
                case "OnNewTrainerAssign":

                    EventBroadcastHelper.updateUpcomingList();

                    break;

                case "onPackageExpired":
                case "OnFinishedPackage":
                case "OnLowBalance":
                case "OnAssignPackageFromCMS":
                case "OnClassRefund":

                    EventBroadcastHelper.updatePackage(this);

                    break;
                case "OnSessionDeleteSilentPush":
                    break;

            }

            /****************************Notification Batch Count********************************/
            switch (type) {

                case "CustomerCancelClass":
                case "FollowUser":
                case "CustomerCancelClassOfTrainer":
                case "CustomerCancelClassOfGym":

                case "CustomerJoinClassOfTrainer":
                case "CustomerJoinClassOfGym":
                case "OnClassCreation":
                case "OnClassDeleteByTrainer":

                case "OnClassDeleteByGym":
                case "OnClassDeleteByTrainerOfGym":
                case "OnClassDeleteByGymOfTrainer":
                case "OnSessionDeleteByTrainer":

                case "OnSessionDeleteByGYM":
                case "OnSessionDeleteByTrainerOfGym":
                case "OnSessionDeleteByGymOfTrainer":

                case "OnSessionUpdateByTrainer":
                case "OnSessionUpdateByGYM":
                case "OnSessionUpdateByTrainerOfGym":
                case "OnSessionUpdateByGymOfTrainer":
                case "OnSlotDeleteByTrainer":

                case "OnPaymentSuccess":
                case "OnClassRefund":
                case "OnClassUpdateByTrainer":
                case "OnClassUpdateByGYM":
                case "OnClassUpdateByTrainerOfGym":

                case "OnClassUpdateByGYMOfTrainer":
                case "OnSlotDeleteByGym":
                case "OnSlotDeletByTrainerOfGym":

                case "OnSlotDeleteByGymOfTrainer":
                case "OnClassCreationNotifyGym":
                case "OnEmailVerification":
                case "onMappedTrainerToTrainer":

                case "onMappedTrainerToGym":
                case "OnClassInActive":
                case "OnClassCancelByCMS":
                case "OnBookingCancelToCustomerByCMS":

                case "OnBookingCancelToGymByCMS":
                case "OnBookingCancelToTrainerByCMS":
                case "OnClassUpdateByCms":
                case "OnNewTrainerAssign":

                case "OnGroupClassUpdateByCms":
                case "OnAssignPackageFromCMS":
                case "OnClassUpdateByCMS":

                    MyPreferences.initialize(context).saveNotificationCount(MyPreferences.initialize(context).getNotificationCount() + 1);
                    EventBroadcastHelper.notificationCountUpdated(context);

                    break;
            }

            String title = "P5M";

            // If no message body found then its a silent push (just to increase the notification count)
            if (message == null || message.isEmpty() || message.equalsIgnoreCase("null")) {
                return;
            }

            switch (type) {
                //********************UPCOMING CLASS TAB********************//
                case "OnUserComingClass":
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, AppConstants.Tab.TAB_MY_SCHEDULE_UPCOMING);
                    break;
                /////////////////////////////////////////////////////////////

                //********************WISH LIST TAB********************//
                case "OnUserWishListComing":
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, AppConstants.Tab.TAB_MY_SCHEDULE_WISH_LIST);
                    break;
                //////////////////////////////////////////////////////////

                //********************FIND A CLASS********************//
                case "OnSessionDeleteByGYM":
                case "OnClassDeleteByTrainer":
                case "OnSessionDeleteByGymOfTrainer":
                case "OnSessionDeleteByTrainerOfGym":
                case "OnSessionDeleteByTrainer":
                case "OnClassDeleteByGym":
                case "OnClassDeleteByTrainerOfGym":
                case "OnClassInActive":
                case "OnSlotDeleteByTrainer":
                case "OnSlotDeleteByGymOfTrainer":
                case "OnSlotDeleteByGym":
                case "OnSlotDeletByTrainerOfGym":
                case "OnPaymentSuccess":
                case "OnClassCancelByCMS":
                case "OnBookingCancelToCustomerByCMS":
                case "OnBookingCancelToGymByCMS":
                case "OnBookingCancelToTrainerByCMS":
                case "OnBookingCancelByCMS":
                    // Find my class..
                    title = "Class Cancelled";
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
                    break;
                ///////////////////////////////////////////////////////////

                //********************TRAINER PROFILE********************//
                case "OnClassUpdateByGYM":
                case "OnClassUpdateByTrainer":
                case "OnSessionUpdateByGYM":
                case "OnSessionUpdateByTrainerOfGym":
                case "OnSessionUpdateByTrainer":
                case "OnSessionUpdateByGymOfTrainer":
                case "OnClassUpdateByCms":
                case "OnClassUpdateByCMS":
                case "OnGroupClassUpdateByCms":
                case "OnNewTrainerAssign":
                    // Class Details..
                    title = "Class Updated";
                    navigationIntent = ClassProfileActivity.createIntent(context, (int) dataID, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
                    break;
                ////////////////////////////////////////////////////////////

                //********************TRAINER PROFILE********************//
                case "OnClassCreation":
                    //Trainer Profile..
                    navigationIntent = TrainerProfileActivity.createIntent(context, (int) dataID, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
                    break;
                /////////////////////////////////////////////////////////

                //********************MEMBERSHIP********************//
                case "onPackageExpired":
                case "OnFinishedPackage":
                case "OnLowBalance":
                case "OnAssignPackageFromCMS":

                    switch (type) {
                        case "onPackageExpired":
                            title = "Package Expired";
                            break;
                        case "OnFinishedPackage":
                            title = "Finished Package";
                            break;
                        case "OnLowBalance":
                            title = "Low Balance";
                            break;
                        case "OnAssignPackageFromCMS":
                            title = "Packaged Assigned";
                            break;
                    }

                    navigationIntent = MemberShip.createIntent(context, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
                    break;
                ////////////////////////////////////////////////////

                //********************SILENT PUSH********************//
                case "OnClassRefund":
                    break;
                case "CustomerCancelClass":
                    break;
                case "OnEmailVerification":
                case "FollowUser":

                case "CustomerCancelClassOfTrainer":
                case "CustomerJoinClassOfGym":
                case "CustomerCancelClassOfGym":
                case "CustomerJoinClassOfTrainer":
                case "OnClassDeleteByGymOfTrainer":
                case "OnClassUpdateByTrainerOfGym":
                case "OnClassUpdateByGYMOfTrainer":
                case "OnClassCreationNotifyGym":
                case "onMappedTrainerToTrainer":
                case "onMappedTrainerToGym":
                    /////////////////////////////////////////////////////
                    break;
                case "CMS":
                    String url = jsonObject.optString(AppConstants.Notification.URL);
                    if (url != null) {
                        navigationIntent = handleNotificationDeeplinking(url);

                    } else {
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                    }


                    break;

                //******************** UNUSED **************************//
//                case "OnClassApprovalToTrainer":
//                case "OnClassApprovalToGym":
//                case "OnClassApprovedToTrainer":
//                case "OnClassApprovedToGym":
                /////////////////////////////////////////////////////////////
            }

            if (navigationIntent == null) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
            }

            handleNotification(navigationIntent, title, message);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void handleNotification(Intent navigationIntent, String title, String message) {

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(navigationIntent);

        stackBuilder.editIntentAt(0).putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);

        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String CHANNEL_ID = "p5m_channel_" + System.currentTimeMillis(); // The id of the channel.
            CharSequence name = "p5m"; // The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.app_icon_small)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.app_icon))
                    .setContentIntent(resultPendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setContentText(message)
                    .build();
            notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);

        } else {

            Notification.BigTextStyle bigTextStyle = new Notification.BigTextStyle();
            bigTextStyle.bigText(message);

            Notification notification = new Notification.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon_small)
                    .setContentTitle(title)
                    .setAutoCancel(true)
                    .setStyle(bigTextStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                            R.mipmap.app_icon))
                    .setPriority(Notification.PRIORITY_MAX)
                    .setDefaults(Notification.DEFAULT_VIBRATE)
                    .setContentIntent(resultPendingIntent)
                    .setContentText(message)
                    .build();
            notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);

        }
    }

    private Intent handleNotificationDeeplinking(String url) {
        Intent navigationIntent;

        try {
            if (url.contains(BuildConfig.BASE_URL + "/classes/") || url.contains(BuildConfig.BASE_URL + "/share/classes/") || url.contains(BuildConfig.BASE_URL_HTTPS + "/classes/") || url.contains(BuildConfig.BASE_URL_HTTPS + "/share/classes/")) {
                String classId = null;
                String[] stringlist = url.split("/classes/");
                if (stringlist != null && stringlist.length > 1) {
                    classId = stringlist[1].split("/")[0];
                }
                try {
                    navigationIntent = ClassProfileActivity.createIntent(context, Integer.parseInt(classId), AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                } catch (Exception e) {
                    e.printStackTrace();
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                }

            } else if (url.contains(BuildConfig.BASE_URL + "/share/gym/") || url.contains(BuildConfig.BASE_URL + "/gym/") || url.contains(BuildConfig.BASE_URL_HTTPS + "/share/gym/") || url.contains(BuildConfig.BASE_URL_HTTPS + "/gym/")) {
                String gymId = null;
                String[] stringlist = url.split("/gym/");
                if (stringlist != null && stringlist.length > 1) {
                    gymId = stringlist[1].split("/")[0];
                }
                try {
                    navigationIntent = GymProfileActivity.createIntent(context, Integer.parseInt(gymId), AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                } catch (Exception e) {
                    e.printStackTrace();
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                }

            } else if (url.contains(BuildConfig.BASE_URL + "/share/trainer/") || url.contains(BuildConfig.BASE_URL + "/trainer/") || url.contains(BuildConfig.BASE_URL_HTTPS + "/share/trainer/") || url.contains(BuildConfig.BASE_URL_HTTPS + "/trainer/")) {
                String trainerId = null;
                String[] stringlist = url.split("/trainer/");
                if (stringlist != null && stringlist.length > 1) {
                    trainerId = stringlist[1].split("/")[0];
                }
                try {
                    navigationIntent = TrainerProfileActivity.createIntent(context, Integer.parseInt(trainerId), AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


                } catch (Exception e) {
                    e.printStackTrace();
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

                }
            } else if (url.contains(BuildConfig.BASE_URL + "/classes") || url.contains(BuildConfig.BASE_URL_HTTPS + "/classes")) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);


            } else if (url.contains(BuildConfig.BASE_URL + "/trainers") || url.contains(BuildConfig.BASE_URL_HTTPS + "/trainers")) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_TRAINER, 0);


            } else if (url.contains(BuildConfig.BASE_URL + "/userschedule") || url.contains(BuildConfig.BASE_URL_HTTPS + "/userschedule")) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, 0);


            } else if (url.contains(BuildConfig.BASE_URL + "/profile?type=favTrainer") || url.contains(BuildConfig.BASE_URL_HTTPS + "/profile?type=favTrainer")) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0, ProfileHeaderTabViewHolder.TAB_1);


            } else if (url.contains(BuildConfig.BASE_URL + "/profile?type=finishedClasses") || url.contains(BuildConfig.BASE_URL_HTTPS + "/profile?type=finishedClasses")) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0, ProfileHeaderTabViewHolder.TAB_2);


            } else if (url.contains(BuildConfig.BASE_URL + "/profile") || url.contains(BuildConfig.BASE_URL_HTTPS + "/profile")) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0);


            } else if (url.contains(BuildConfig.BASE_URL + "/editprofile") || url.contains(BuildConfig.BASE_URL_HTTPS + "/editprofile")) {
                navigationIntent = EditProfileActivity.createIntent(context);


            } else if (url.contains(BuildConfig.BASE_URL + "/settings/membership") || url.contains(BuildConfig.BASE_URL_HTTPS + "/settings/membership")) {
                navigationIntent = MemberShip.createIntent(context, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


            } else if (url.contains(BuildConfig.BASE_URL + "/settings/transaction") || url.contains(BuildConfig.BASE_URL_HTTPS + "/settings/transaction")) {
                navigationIntent = TransactionHistoryActivity.createIntent(context, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);


            } else if (url.contains(BuildConfig.BASE_URL + "/settings/notification") || url.contains(BuildConfig.BASE_URL_HTTPS + "/settings/notification")) {
                navigationIntent = NotificationActivity.createIntent(context);

            } else if (url.contains(BuildConfig.BASE_URL + "/settings/aboutus") || url.contains(BuildConfig.BASE_URL_HTTPS + "/settings/aboutus")) {
                navigationIntent = SettingActivity.createIntent(context, AppConstants.Tab.OPEN_ABOUT_US);

            }
            else if(url.contains(BuildConfig.BASE_URL+"/settings/notifications")||url.contains(BuildConfig.BASE_URL_HTTPS+"/settings/notifications")){
                navigationIntent = NotificationActivity.createIntent(context);

            }
            else if(url.contains(BuildConfig.BASE_URL+"/searchresults/")||url.contains(BuildConfig.BASE_URL_HTTPS+"/searchresults/")){
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

            }
            else {

                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

            }
        } catch (Exception e) {
            e.printStackTrace();
            navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);

        }


        return navigationIntent;

    }


    private void setNotification(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
        TempStorage.setSavedClasses(model, context);
    }
}
