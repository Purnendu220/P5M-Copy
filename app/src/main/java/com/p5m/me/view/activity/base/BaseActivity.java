package com.p5m.me.view.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.p5m.me.analytics.MixPanel;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;

import java.util.ArrayList;
import java.util.List;


public class BaseActivity extends AppCompatActivity {

    public static boolean isForeground = false;
    public boolean showActionBar = true;

    public static Context mContext;
    public static Activity activityRef;

    public Activity activity;
    public Context context;
    public NetworkCommunicator networkCommunicator;
    public RefrenceWrapper refrenceWrapper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        context = this;
        mContext = this;
        activityRef = this;
        refrenceWrapper= RefrenceWrapper.getRefrenceWrapper(this);

        MixPanel.setup(activity);

        networkCommunicator = NetworkCommunicator.getInstance(context);
        LogUtils.debug(activityRef.getComponentName().getClassName());

        if (!showActionBar) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.xmppDebug("base activity onTrimMemory " + level);
    }

    private static List<FragmentsCallbacks> fragmentsCallbacksList = new ArrayList<>();

    public static void registerFragmentsCallbacks(FragmentsCallbacks fragmentsCallbacks) {
        fragmentsCallbacksList.add(fragmentsCallbacks);
    }

    public static void unregisterFragmentsCallbacks(FragmentsCallbacks fragmentsCallbacks) {
        fragmentsCallbacksList.remove(fragmentsCallbacks);
    }

    public static interface FragmentsCallbacks {
        void onAppForeground();

        void onAppBackground();
    }
}
