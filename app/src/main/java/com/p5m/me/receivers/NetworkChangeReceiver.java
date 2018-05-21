package com.p5m.me.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.p5m.me.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

import com.p5m.me.utils.NetworkUtil;

/**
 * Created by MyU10 on 1/9/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static List<OnNetworkChangeListener> onNetworkChangeListeners = new ArrayList<>();

    @Override
    public void onReceive(final Context context, final Intent intent) {

        LogUtils.debug("NetworkChangeReceiver onReceive");

        try {
            NetworkUtil.getInstance(context).initialize();
            LogUtils.debug("NetworkChangeReceiver " + NetworkUtil.isInternetAvailable);

//        if (BaseActivity.isForeground) {
//            MyuApplication.startXmpp(context);
//        }

            for (OnNetworkChangeListener onNetworkChangeListener : onNetworkChangeListeners) {
                try {
                    onNetworkChangeListener.onNetworkChange(NetworkUtil.isInternetAvailable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(OnNetworkChangeListener onNetworkChangeListener) {
        try {
            if (!onNetworkChangeListeners.contains(onNetworkChangeListener)) {
                onNetworkChangeListeners.add(onNetworkChangeListener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void unregister(OnNetworkChangeListener onNetworkChangeListener) {
        try {
            onNetworkChangeListeners.remove(onNetworkChangeListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface OnNetworkChangeListener {
        void onNetworkChange(boolean isConnected);
    }

}
