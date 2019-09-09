package com.p5m.me.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.p5m.me.restapi.NetworkCommunicator;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseMessagingService implements NetworkCommunicator.RequestListener {

//    @Override
//    public void Notifications Received() {
//        // Get updated InstanceID token.
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);
//
////        MyPreferences.getInstance().saveDeviceToken(refreshedToken);
//    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
    }

    
}