package com.p5m.me.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.p5m.me.MyApp;
import com.p5m.me.R;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.helper.Helper;
import com.p5m.me.restapi.NetworkCommunicator;
import com.p5m.me.restapi.ResponseModel;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
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
    private long DELAY_NAVIGATION = 1400; // 1.4 sec

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

        startTimerForGoToNextScreen();
        networkCommunicator.getActivities(this, false);

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
}