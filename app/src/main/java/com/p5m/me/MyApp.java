package com.p5m.me;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.p5m.me.analytics.MixPanel;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.receivers.NetworkChangeReceiver;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.LogUtils;

import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
@ReportsCrashes(formKey = "", // will not be used
        mailTo = "purnendu.mishar@p5m.me", mode = ReportingInteractionMode.TOAST, resToastText = R.string.app_crash)
@SuppressLint("NewApi")
public class MyApp extends MultiDexApplication implements NetworkChangeReceiver.OnNetworkChangeListener, Application.ActivityLifecycleCallbacks, NetworkCommunicator.RequestListener {

    public static Context context;

    public final static ApiMode apiMode = ApiMode.TESTING_ALPHA;
    public final static boolean USE_CRASH_ANALYTICS = false;
    public final static boolean USE_MIX_PANEL = false;

    public final static boolean SHOW_LOG = true;
    public final static boolean RETROFIT_SHOW_LOG = true;

    public final static List<Activity> ACTIVITIES = new ArrayList<>();

    public boolean isAppForeground;
    public long appBackgroundTime;

    public enum ApiMode {
        TESTING_ALPHA,
        TESTING_BETA,
        LIVE
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);

        context = getApplicationContext();

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
        TempStorage.version = myVersionName;

        LogUtils.debug("version : " + TempStorage.version);

        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), filter);

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
}
