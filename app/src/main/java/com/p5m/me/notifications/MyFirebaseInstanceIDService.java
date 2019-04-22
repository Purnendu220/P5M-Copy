package com.p5m.me.notifications;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.InstanceIdResult;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.LogUtils;

/**
 * Created by Varun John on 4/12/2018.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements NetworkCommunicator.RequestListener {

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        LogUtils.debug("Notifications onTokenRefresh " + refreshedToken);

//        MyPreferences.getInstance().saveDeviceToken(refreshedToken);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
    }

    
}