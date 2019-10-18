package com.p5m.me.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.p5m.me.MyApp;
import com.p5m.me.R;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.remote_config.RemoteConfigSetUp;
import com.p5m.me.remote_config.RemoteConfigure;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DialogUtils;
import com.p5m.me.utils.LanguageUtils;
import com.p5m.me.utils.PermissionUtility;
import com.p5m.me.view.activity.Main.ForceUpdateActivity;
import com.p5m.me.view.activity.Main.HomeActivity;
import com.p5m.me.view.activity.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Splash extends BaseActivity implements NetworkCommunicator.RequestListener {

    public static void open(Context context) {
        Intent intent = new Intent(context, Splash.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private Handler handler;
    private Runnable nextScreenRunnable;
    private long DELAY_NAVIGATION = 2500; // 1.4 sec

    @BindView(R.id.imageViewImage)
    public ImageView imageViewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        showActionBar = false;
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        boolean booleanExtra = getIntent().getBooleanExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, false);
        if (booleanExtra) {
            finish();
            return;
        }

        ButterKnife.bind(activity);

        handler = new Handler();

        imageViewImage.animate().scaleXBy(0.3f).scaleYBy(0.3f).setDuration(1000).setInterpolator(new BounceInterpolator()).start();

        if (MyPreferences.getInstance().isLogin()) {
            networkCommunicator.getMyUser(Splash.this, false);
            EventBroadcastHelper.sendDeviceUpdate(context);
        }
        networkCommunicator.getActivities(this, false);
            (new RemoteConfigure()).fetchRemoteConfig(context);
//        RemoteConfigSetUp.getValues();

        if (Build.VERSION.SDK_INT >= 23) {
            if (PermissionUtility.verifyLocationPermissions(Splash.this)) {
                startTimerForGoToNextScreen();

            }
        } else {
            startTimerForGoToNextScreen();
        }
        getToken();
    }

    private void getToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener( Splash.this,  new OnSuccessListener<InstanceIdResult>() {
            @Override
            public void onSuccess(InstanceIdResult instanceIdResult) {
                String newToken = instanceIdResult.getToken();
                Log.e("newToken",newToken);
            }
        });
    }

    private void startTimerForGoToNextScreen() {

        nextScreenRunnable = new Runnable() {
            @Override
            public void run() {

                if (MyPreferences.getInstance().isLogin()) {
                    /////////// HomeActivity Screen ////////////

                    HomeActivity.open(context);

                } else {

                    Helper.handleLogin(context);
                }

                finish();
            }
        };

        handler.postDelayed(nextScreenRunnable, DELAY_NAVIGATION);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(nextScreenRunnable);
    }

    @Override
    public void onApiSuccess(Object response, int requestCode) {
        switch (requestCode) {
            case NetworkCommunicator.RequestCode.DEVICE:

                Boolean forceUpdate = ((ResponseModel<Boolean>) response).data;
                if (forceUpdate) {
                    ForceUpdateActivity.openActivity(MyApp.context, "", "");
                }

                break;
        }
    }

    @Override
    public void onApiFailure(String errorMessage, int requestCode) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case PermissionUtility.REQUEST_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startTimerForGoToNextScreen();


                } else {
                    showPermissionImportantAlert(context.getResources().getString(R.string.permission_message_media));

                }
                break;

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void showPermissionImportantAlert(String message) {
        DialogUtils.showBasicMessage(context, context.getResources().getString(R.string.permission_alert), message,
                context.getResources().getString(R.string.go_to_settings), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        Intent i = new Intent();
                        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        i.addCategory(Intent.CATEGORY_DEFAULT);
                        i.setData(Uri.parse("package:" + context.getPackageName()));
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        context.startActivity(i);
                        dialog.dismiss();
                        finish();


                    }
                }, context.getResources().getString(R.string.cancel), new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                        startTimerForGoToNextScreen();
                    }
                });
    }
}
