package com.p5m.me.view.activity.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.inappmessaging.FirebaseInAppMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.data.PushDetailModel;
import com.p5m.me.data.SubscriptionConfigModal;
import com.p5m.me.remote_config.RemoteConfigConst;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.utils.AppConstants;
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
    private FirebaseInAppMessaging mInAppMessaging;
    public SubscriptionConfigModal mSubscriptionConfigModal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        context = this;
        mContext = this;
        activityRef = this;
        refrenceWrapper= RefrenceWrapper.getRefrenceWrapper(this);

        MixPanel.setup(activity);
        FirebaseAnalysic.setup(activity);
        RemoteConfigSetUp.setup(activity);


        mInAppMessaging = FirebaseInAppMessaging.getInstance();

        mInAppMessaging.setAutomaticDataCollectionEnabled(true);
        mInAppMessaging.setMessagesSuppressed(false);

        networkCommunicator = NetworkCommunicator.getInstance(context);
        LogUtils.debug(activityRef.getComponentName().getClassName());

        if (!showActionBar) {
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (getSupportActionBar() != null) {
                getSupportActionBar().hide();
            }
        }
        initSubscriptionConfig();

    }
    private void initSubscriptionConfig(){
        String SUBSCRIPTION_CONFIG = RemoteConfigure.getFirebaseRemoteConfig(mContext).getRemoteConfigValue(RemoteConfigConst.SUBSCRIPTION_CONFIG_VALUE);
        try{
            Gson g = new Gson();
            mSubscriptionConfigModal = g.fromJson(SUBSCRIPTION_CONFIG, new TypeToken<SubscriptionConfigModal>() {
            }.getType());
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    public void onTrackingNotification() {
        boolean booleanExtra = getIntent().getBooleanExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, false);
        if (booleanExtra) {
            PushDetailModel pushDetailModel = (PushDetailModel) getIntent().getSerializableExtra(AppConstants.DataKey.DATA_FROM_NOTIFICATION_STACK);
            MixPanel.trackPushNotificationClick(pushDetailModel);
            FirebaseAnalysic.trackPushNotificationClick(pushDetailModel);
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
