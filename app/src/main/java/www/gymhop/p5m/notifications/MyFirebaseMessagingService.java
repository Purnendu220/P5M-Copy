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

        if (remoteMessage == null)
            return;

        LogUtils.debug("Notifications From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null) {
            LogUtils.debug("Notifications Body: " + remoteMessage.getNotification().getBody());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataMessage(json);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
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

//            case .CustomerCancelClass, .FollowUser, .CustomerCancelClassOfTrainer, .CustomerCancelClassOfGym,
//             .CustomerJoinClassOfTrainer, .CustomerJoinClassOfGym, .OnClassCreation, .OnClassDeleteByTrainer,
//             .OnClassDeleteByGym, .OnClassDeleteByTrainerOfGym, .OnClassDeleteByGymOfTrainer, .OnSessionDeleteByTrainer,
//             .OnSessionDeleteByGYM, .OnSessionDeleteByTrainerOfGym, .OnSessionDeleteByGymOfTrainer,
//             .OnSessionUpdateByTrainer, .OnSessionUpdateByGYM, .OnSessionUpdateByTrainerOfGym, .OnSessionUpdateByGymOfTrainer,
//             .OnPaymentSuccess, .OnClassRefund, .OnClassUpdateByTrainer, .OnClassUpdateByGYM, .OnClassUpdateByTrainerOfGym,
//             .OnClassUpdateByGYMOfTrainer, .OnSlotDeleteByTrainer, .OnSlotDeleteByGym, .OnSlotDeletByTrainerOfGym,
//             .OnSlotDeleteByGymOfTrainer, .OnClassCreationNotifyGym, .OnEmailVerification, .onMappedTrainerToTrainer,
//             .onMappedTrainerToGym, .OnClassInActive, .OnClassCancelByCMS, .OnBookingCancelToCustomerByCMS,
//             .OnBookingCancelToGymByCMS, .OnBookingCancelToTrainerByCMS, .OnClassUpdateByCms, .OnNewTrainerAssign,
//             .OnGroupClassUpdateByCms, .OnAssignPackageFromCMS: OnBookingCancelByCMS

            switch (type) {
                case "CustomerCancelClass":
                    break;
            }

            EventBroadcastHelper.notificationReceived();
        } catch (JSONException e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }
}
