package www.gymhop.p5m;

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

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import www.gymhop.p5m.receivers.NetworkChangeReceiver;
import www.gymhop.p5m.restapi.NetworkCommunicator;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.utils.LogUtils;

public class MyApp extends MultiDexApplication implements NetworkChangeReceiver.OnNetworkChangeListener, Application.ActivityLifecycleCallbacks, NetworkCommunicator.RequestListener {

    public static Context context;

    public final static ApiMode apiMode = ApiMode.TESTING_ALPHA;
    public final static boolean SHOW_LOG = true;
    public final static boolean RETROFIT_SHOW_LOG = false;
    public final static boolean USE_CRASH_ANALYTICS = false;

    public final static List<Activity> ACTIVITIES = new ArrayList<>();

    public static boolean isAppForeground;

    public enum ApiMode {
        TESTING_ALPHA,
        TESTING_BETA,
        LIVE
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

        MultiDex.install(this);
        Fresco.initialize(this);

        Fabric.with(this, new Crashlytics());

        registerActivityLifecycleCallbacks(this);

        NetworkChangeReceiver.register(this);

        MyPreferences.initialize(context);

        String myVersionName = "not available";
        PackageManager packageManager = getPackageManager();
        String packageName = getPackageName();

        try {
            myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        TempStorage.version = myVersionName;

        LogUtils.debug("version : " + TempStorage.version);

        IntentFilter filter = new IntentFilter();
        filter.addAction(android.net.ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(new NetworkChangeReceiver(), filter);

        NetworkCommunicator.getInstance(context).getDefault();
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
            LogUtils.debug("App is in Foreground");
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
                LogUtils.debug("App is in Background");
                onAppBackground();
            }
        }
    }

    private void onAppForeground() {
        //ToastUtils.show(this, "AppForeground");
//        EventBroadcastHelper.sendDeviceUpdate(this);
    }

    private void onAppBackground() {
        // ToastUtils.show(this, "AppBackground");
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {

    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {

    }
}
