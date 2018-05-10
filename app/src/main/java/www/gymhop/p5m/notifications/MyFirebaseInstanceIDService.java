package www.gymhop.p5m.notifications;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.utils.LogUtils;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements NetworkCommunicator.RequestListener {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);

        MyPreferences.getInstance().saveDeviceToken(refreshedToken);
        EventBroadcastHelper.sendDeviceUpdate(this);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
    }
}