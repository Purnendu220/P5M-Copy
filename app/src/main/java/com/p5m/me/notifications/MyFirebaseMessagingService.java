package com.p5m.me.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.p5m.me.R;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.Main.ClassProfileActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.Main.MemberShip;
import com.p5m.me.view.activity.Main.TrainerProfileActivity;

import org.json.JSONObject;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Context context;

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
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void handleDataMessage(JSONObject json) {

        try {
            JSONObject jsonObject = json;
            String type = jsonObject.optString(AppConstants.Notification.TYPE);
            String message = jsonObject.optString(AppConstants.Notification.BODY);
            String userIdToNotify = jsonObject.optString(AppConstants.Notification.USER_ID_TO_NOTIFY);
            long dataID  = jsonObject.optLong(AppConstants.Notification.OBJECT_DATA_ID);

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
            if (message.isEmpty()) {
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
                    navigationIntent = ClassProfileActivity.createIntent(context, (int) dataID);
                    break;
                ////////////////////////////////////////////////////////////

                //********************TRAINER PROFILE********************//
                case "OnClassCreation":
                    //Trainer Profile..
                    navigationIntent = TrainerProfileActivity.createIntent(context, (int) dataID);
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
                case "CustomerCancelClass":
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

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify((int) (System.currentTimeMillis() - 10000000), notification);
    }
}
