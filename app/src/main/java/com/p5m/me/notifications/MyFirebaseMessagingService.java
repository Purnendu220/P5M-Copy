package com.p5m.me.notifications;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.p5m.me.R;
import com.p5m.me.data.Join5MinModel;
import com.p5m.me.data.PushDetailModel;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.CalendarHelper;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import io.intercom.android.sdk.push.IntercomPushClient;

import static com.p5m.me.utils.AppConstants.Pref.MEMBERSHIP_INFO_STATE_NO_PACKAGE;
import static com.p5m.me.utils.AppConstants.Tab.TAB_MY_MEMBERSHIP;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Context context;
    private Hashtable<String, String> calendarIdTable;
    private int calendar_id = -1;
    PushDetailModel pushDetailModel;
    private final IntercomPushClient intercomPushClient = new IntercomPushClient();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        context = this;

        LogUtils.debug("Notifications Received");
        if (remoteMessage == null)
            return;

        try {

            LogUtils.debug("Notifications Data: " + remoteMessage.getData());

            String message;
            if (remoteMessage.getData() != null) {
                message = remoteMessage.getData().get("message");
                Map dataMessage = remoteMessage.getData();
                if (intercomPushClient.isIntercomPush(dataMessage)) {
                    intercomPushClient.handlePush(getApplication(), dataMessage);
//                    Intercom.client().handlePushMessage();

                } else if (message != null) {
                    if (!message.equalsIgnoreCase("fcm")) {
                        JSONObject json = new JSONObject(message);
                        handleDataMessage(json);
                        handleDataMessageForNotificationSchedule(json);
                    }

                } else {
                    Intent navgationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
                    if (remoteMessage.getNotification() != null) {
                        handleNotification(navgationIntent, remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
                    }

                }
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
                case "CustomerCancelClass":
                    if (dataID != 0L && dataID > 0) {
                        TempStorage.removeSavedClass((int) dataID, context);
                        removeEvent(jsonObject, dataID);
                    }
                    break;
                case "OnClassUpdateByGYM":
                case "OnClassUpdateByTrainer":

                case "OnSessionUpdateByGYM":

                case "OnSessionUpdateByTrainerOfGym":
                case "OnSessionUpdateByGymOfTrainer":

                case "OnClassUpdateByCms":

                case "OnGroupClassUpdateByCms":
                    TempStorage.removeSavedClass((int) dataID, context);
                    setNotification(jsonObject, dataID);
                    updateEvent(jsonObject, dataID);
                    break;

                case "OnSessionUpdateByTrainer":
                    TempStorage.removeSavedClass((int) dataID, context);
                    updateEvent(jsonObject, dataID);

                    break;

                case "OnJoinClass":
                case "OnSeatAvailableForWishlistFromClassUpdate":
                case "OnSeatAvailableForWishlist":
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
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            CalendarHelper.scheduleCalenderEvent(this, model);
        }
    }

    private void removeEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            CalendarHelper.deleteEvent(model.getClassSessionId(), context);

        }

    }

    private void updateEvent(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel classModel = new ClassModel(title, classDate, fromTime, toTime, dataID);
        if (CalendarHelper.haveCalendarReadWritePermissions(this)) {
            CalendarHelper.updateEvent(this, classModel);


        }
    }

    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject jsonObject = json;
            String type = jsonObject.optString(AppConstants.Notification.TYPE);
            String message = jsonObject.optString(AppConstants.Notification.BODY);
            String notifyUrl = jsonObject.optString(AppConstants.Notification.URL);
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
                case "OnSeatAvailableForWishlist":
                case "OnSeatAvailableForWishlistFromClassUpdate":

                    MyPreferences.initialize(context).saveNotificationCount(MyPreferences.initialize(context).getNotificationCount() + 1);
                    EventBroadcastHelper.notificationCountUpdated(context);
                    EventBroadcastHelper.updateUpcomingList();
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

              /*  case "OnSeatAvailableForWishlist":
                    EventBroadcastHelper.updateUpcomingList();
                    navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, AppConstants.Tab.TAB_MY_SCHEDULE_WISH_LIST);
                    break;*/

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
                case "OnSeatAvailableForWishlist":
                case "OnSeatAvailableForWishlistFromClassUpdate":

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
                    navigationIntent = HomeActivity.showMembership(context, TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
                    break;
                case "OnFinishedPackage":
                case "OnLowBalance":
                case "OnAssignPackageFromCMS":

                    switch (type) {
                        case "onPackageExpired":
                            title = "Package Expired";
                            break;
                        case "OnFinishedPackage":
                            title = "Finished Package";
                            if (TempStorage.isOpenMembershipInfo() != 2) {
                                TempStorage.setOpenMembershipInfo(MEMBERSHIP_INFO_STATE_NO_PACKAGE);
                            }

                            break;
                        case "OnLowBalance":
                            title = "Low Balance";
                            break;
                        case "OnAssignPackageFromCMS":
                            title = "Packaged Assigned";
                            break;
                    }
                    navigationIntent = HomeActivity.showMembership(context, TAB_MY_MEMBERSHIP, AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION);
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
                        navigationIntent = HandleNotificationDeepLink.handleNotificationDeeplinking(context, url);

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

//            if (navigationIntent == null) {
//                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
//            }
            if (navigationIntent != null)
                handleNotification(navigationIntent, title, message);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void handleNotification(Intent navigationIntent, String title, String message) {
        navigationIntent.putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);
        navigationIntent.putExtra(AppConstants.DataKey.DATA_FROM_NOTIFICATION_STACK, pushDetailModel);
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


    private void setPushDetail(String type, String message, String url) {
        pushDetailModel = new PushDetailModel();
        pushDetailModel.setMessage(message);
        pushDetailModel.setType(type);
        pushDetailModel.setUrl(url);
        Log.v("PushDetail", "type: " + type
                + " msg: " + message
                + " url: " + url);
    }

    private void setNotification(JSONObject jsonObject, long dataID) {
        String title = jsonObject.optString(AppConstants.Notification.CLASS_TITLE);
        String classDate = jsonObject.optString(AppConstants.Notification.CLASS_DATE);
        String fromTime = jsonObject.optString(AppConstants.Notification.CLASS_FROM_TIME);
        String toTime = jsonObject.optString(AppConstants.Notification.CLASS_TO_TIME);
        ClassModel model = new ClassModel(title, classDate, fromTime, toTime, dataID);
        TempStorage.setSavedClasses(model, context);
    }

    @Override
    public void onNewToken(String refreshedToken) {
        super.onNewToken(refreshedToken);
        LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);
        MyPreferences.getInstance().saveDeviceToken(refreshedToken);
        intercomPushClient.sendTokenToIntercom(getApplication(), refreshedToken);

    }

}
