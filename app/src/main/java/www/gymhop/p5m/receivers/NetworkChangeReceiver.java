package www.gymhop.p5m.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import www.gymhop.p5m.utils.NetworkUtil;

/**
 * Created by MyU10 on 1/9/2017.
 */

public class NetworkChangeReceiver extends BroadcastReceiver {

    private static List<OnNetworkChangeListener> onNetworkChangeListeners = new ArrayList<>();

    @Override
    public void onReceive(final Context context, final Intent intent) {
        try {
            NetworkUtil.getInstance(context).initialize();

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
