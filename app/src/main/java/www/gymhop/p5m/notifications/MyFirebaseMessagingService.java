package www.gymhop.p5m.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        LogUtils.debug("Notifications Received");

        EventBroadcastHelper.notificationReceived();

        if (remoteMessage == null)
            return;

        try {

            LogUtils.debug("Notifications Data: " + remoteMessage.getData());
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
            JSONObject jsonObject = new JSONObject(json.getString(AppConstants.Notification.MESSAGE));
            String type = jsonObject.getString(AppConstants.Notification.TYPE);
            String message = jsonObject.getString(AppConstants.Notification.BODY);
            String notificationType = jsonObject.getString(AppConstants.Notification.NOTIFICATION_TYPE);

            long dataID = 0;
            if (!jsonObject.getString(AppConstants.Notification.OBJECT_DATA_ID).equalsIgnoreCase("null")) {
                dataID = jsonObject.getLong(AppConstants.Notification.OBJECT_DATA_ID);
            }

            switch (type) {

                case "OnUserComingClass":
                    // Upcoming Class..
                    break;

                case "OnUserWishListComing":
                    // WishList..
                    break;

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
                    // Class Cancelled
                    break;

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
                    // Class Updated
                    break;

                case "OnClassCreation":
                    //Trainer Profile..
                    // P5M
                    break;

                case "onPackageExpired":
                    // Package Expired
                case "OnFinishedPackage":
                    // Finished Package
                case "OnLowBalance":
                    // Low Balance
                case "OnAssignPackageFromCMS":
                    //Membership..
                    break;

                    ///////////SILENT PUSH/////////////
                case "OnClassRefund":
                case "CustomerCancelClass":
                case "OnEmailVerification":
                    ////////////////////////
                    break;


//                case "FollowUser":
//                case "CustomerCancelClassOfTrainer":
//                case "CustomerCancelClassOfGym":
//                case "CustomerJoinClassOfTrainer":
//                case "CustomerJoinClassOfGym":
//                case "OnClassDeleteByGymOfTrainer":
//                case "OnClassUpdateByTrainerOfGym":
//                case "OnClassUpdateByGYMOfTrainer":
//                case "OnClassCreationNotifyGym":
//                case "onMappedTrainerToTrainer":
//                case "onMappedTrainerToGym":
//                case "OnClassApprovalToTrainer":
//                case "OnClassApprovalToGym":
//                case "OnClassApprovedToTrainer":
//                case "OnClassApprovedToGym":
//                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }
}
