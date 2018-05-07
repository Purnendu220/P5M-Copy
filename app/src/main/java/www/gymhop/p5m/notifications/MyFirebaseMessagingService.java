package www.gymhop.p5m.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
//        LogUtils.debug("Notifications From: " + remoteMessage.getFrom());
//
//        if (remoteMessage.getNotification() != null) {
//            LogUtils.debug("Notifications Message Body: " + remoteMessage.getNotification().getBody());
//            ToastUtils.show(this, remoteMessage.getNotification().getBody());
//        }
    }

}
