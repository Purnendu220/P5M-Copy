package com.p5m.me;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.p5m.me.analytics.FirebaseAnalysic;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.receivers.NetworkChangeReceiver;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.RefrenceWrapper;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;

@SuppressLint("NewApi")
public class MyApp extends MultiDexApplication implements NetworkChangeReceiver.OnNetworkChangeListener, Application.ActivityLifecycleCallbacks, NetworkCommunicator.RequestListener {

    public static Context context;

    public final static boolean USE_CRASH_ANALYTICS = BuildConfig.IS_PRODUCTION;
    public final static boolean USE_MIX_PANEL = BuildConfig.IS_PRODUCTION;

    public final static boolean SHOW_LOG = BuildConfig.IS_DEBUG;
    public final static boolean RETROFIT_SHOW_LOG = BuildConfig.IS_DEBUG;

    public final static List<Activity> ACTIVITIES = new ArrayList<>();

    public final static String MIX_PANEL_TOKEN = "705daac4d807e105c1ddc350c9324ca2";
//    public final static String MIX_PANEL_TOKEN = "ac8ac225ea9618bad16a7fe25dfd548e";
    public final static String GOOGLE_API_PROJECT = "109210713388";

    public boolean isAppForeground;
    public long appBackgroundTime;
    private FirebaseOptions options;
    private boolean hasBeenInitialized;
    private FirebaseApp finestayApp;


    @Override
    public void onCreate() {
        super.onCreate();


        context = getApplicationContext();
        RefrenceWrapper.getRefrenceWrapper(this).setAppContext(this);

        MultiDex.install(this);
        Fresco.initialize(this);

        if (USE_CRASH_ANALYTICS) {
            Fabric.with(this, new Crashlytics());
        }

        registerActivityLifecycleCallbacks(this);

        NetworkChangeReceiver.register(this);

        String myVersionName = "not available";
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TempStorage.setUser(this, MyPreferences.initialize(this).getUser());
        TempStorage.version = BuildConfig.VERSION_NAME_API;
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        RefrenceWrapper.getRefrenceWrapper(this).setDeviceId(android_id);
        LogUtils.debug("version : " + TempStorage.version);

        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), filter);
        firebaseDataSet();
        if (MyPreferences.getInstance().isLogin()) {
            NetworkCommunicator.getInstance(context).getDefault();
        }

    }

    @Override
    public void onNetworkChange(boolean isConnected) {
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle bundle) {
        ACTIVITIES.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {
        if (!isAppForeground) {
            isAppForeground = true;
            onAppForeground();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        ACTIVITIES.remove(activity);
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
        LogUtils.debug("MyuApplication onTrimMemory " + level);

        if (level == TRIM_MEMORY_UI_HIDDEN) {
            if (isAppForeground) {
                isAppForeground = false;
                onAppBackground();
            }
        }
    }

    private void onAppForeground() {

        LogUtils.debug("App is in Foreground");

        //Greater then 5 mins..
        if (appBackgroundTime != 0 && System.currentTimeMillis() - appBackgroundTime >= (5000 * 60)) {
            EventBroadcastHelper.sendRefreshClassList();
        }

        try {
            if (MyPreferences.getInstance().isLogin()) {
                EventBroadcastHelper.sendDeviceUpdate(this);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private void onAppBackground() {

        LogUtils.debug("App is in Background");

        appBackgroundTime = System.currentTimeMillis();

        MixPanel.flush();
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }

    private void firebaseDataSet() {
        if(BuildConfig.FIREBASE_IS_PRODUCTION){
            options = new FirebaseOptions.Builder()
                    .setApplicationId("1:109210713388:android:e83033ee42a596eb") // Required for Analytics.
                    .setApiKey("AIzaSyA6XFUdbw_d56dCnlGa6EcFcqdWEpE8ir4") // Required for Auth.
                    .setDatabaseUrl("https://gymhop-p5m-1524059965243.firebaseio.com") // Required for RTDB.
                    .build();
//            FirebaseApp.initializeApp(getApplicationContext(), options);
        }
        else {
            options = new FirebaseOptions.Builder()
                    .setApplicationId("1:955940869604:android:e83033ee42a596eb") // Required for Analytics.
                    .setApiKey("AIzaSyCxrLj88gOD1JjEIsc1qK38lOqagX7IdvY") // Required for Auth.
                    .setDatabaseUrl("https://pro5ios-e0bb0.firebaseio.com") // Required for RTDB.
                    .build();
//            FirebaseApp.initializeApp(getApplicationContext(), options);
        }

        List<FirebaseApp> firebaseApps = FirebaseApp.getApps(context);
        for(FirebaseApp app : firebaseApps){
            if(app.getName().equals(FirebaseApp.DEFAULT_APP_NAME)){
                hasBeenInitialized=true;
                finestayApp = app;
            }
        }

        if(!hasBeenInitialized) {
            finestayApp = FirebaseApp.initializeApp(context, options);
        }
    }
}
