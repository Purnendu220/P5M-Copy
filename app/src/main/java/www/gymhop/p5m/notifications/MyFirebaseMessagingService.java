package www.gymhop.p5m.notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import www.gymhop.p5m.R;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.Main.ClassProfileActivity;
import www.gymhop.p5m.view.activity.Main.HomeActivity;
import www.gymhop.p5m.view.activity.Main.MemberShip;
import www.gymhop.p5m.view.activity.Main.TrainerProfileActivity;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        context = this;

        LogUtils.debug("Notifications Received");

        EventBroadcastHelper.notificationReceived(context);

        if (remoteMessage == null)
            return;

        try {

//            LogUtils.debug("Notifications Data: " + remoteMessage.getData());
            String message = remoteMessage.getData().get("message");

            if (message != null) {
                LogUtils.debug("Notifications Message: " + message);

                JSONObject json = new JSONObject(message);
                handleDataMessage(json);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void handleDataMessage(JSONObject json) {
        LogUtils.debug("Notifications Data: " + json.toString());

        try {
            JSONObject jsonObject = json;
            String type = jsonObject.getString(AppConstants.Notification.TYPE);
            String message = jsonObject.optString(AppConstants.Notification.BODY);
            String userIdToNotify = jsonObject.getString(AppConstants.Notification.USER_ID_TO_NOTIFY);

            long dataID = 0;
            if (!jsonObject.getString(AppConstants.Notification.OBJECT_DATA_ID).equalsIgnoreCase("null")) {
                dataID = jsonObject.getLong(AppConstants.Notification.OBJECT_DATA_ID);
            }

            String title = "P5M";

            // If no message body found then its a silent push (just to increase the notification count)
            if (message.isEmpty()) {
                return;
            }

            Intent navigationIntent = null;

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
                    /////////////////////////////////////////////////////
                    break;

                //******************** UNUSED **************************//
             /*   case "FollowUser":
                case "CustomerCancelClassOfTrainer":
                case "CustomerCancelClassOfGym":
                case "CustomerJoinClassOfTrainer":
                case "CustomerJoinClassOfGym":
                case "OnClassDeleteByGymOfTrainer":
                case "OnClassUpdateByTrainerOfGym":
                case "OnClassUpdateByGYMOfTrainer":
                case "OnClassCreationNotifyGym":
                case "onMappedTrainerToTrainer":
                case "onMappedTrainerToGym":
                case "OnClassApprovalToTrainer":
                case "OnClassApprovalToGym":
                case "OnClassApprovedToTrainer":
                case "OnClassApprovedToGym":
                    break;*/
                /////////////////////////////////////////////////////////////
            }

            if (navigationIntent == null) {
                navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
            }

            handleNotification(navigationIntent, title, message);

        } catch (JSONException e) {
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
