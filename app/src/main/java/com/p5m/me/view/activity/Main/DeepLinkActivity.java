package com.p5m.me.view.activity.Main;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.p5m.me.R;
import com.p5m.me.helper.Helper;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

public class DeepLinkActivity extends BaseActivity {

    public static void open(Context context, String url) {
        context.startActivity(new Intent(context, DeepLinkActivity.class).setData(Uri.parse(url)).setAction("android.intent.action.VIEW"));
    }

    public static String url = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_link);

        LogUtils.debug("DeepLinkActivity DATA " + getIntent().getData() + "  ACTION " + getIntent().getAction());

        try {
            if (getIntent() != null && getIntent().getData() != null && getIntent().getAction() != null &&
                    getIntent().getAction().equals("android.intent.action.VIEW")) {

                String url = getIntent().getData().toString();

                if (!MyPreferences.initialize(context).isLogin()) {

                    DeepLinkActivity.url = url;

                    finish();
                    Helper.handleLogin(context);
                    return;

                } else {

                    DeepLinkActivity.url = null;

                    Intent navigationIntent = null;

                    if (getIntent().getData().toString().contains("/share/classes/")) {
                        int classSessionId = Integer.valueOf(url.substring(url.indexOf("/share/classes/") + "/share/classes/".length(), url.lastIndexOf("/")));
                        navigationIntent = ClassProfileActivity.createIntent(context, classSessionId, AppConstants.AppNavigation.NAVIGATION_FROM_SHARE);

                    } else if (getIntent().getData().toString().contains("/share/gym/")) {
                        int gymId = Integer.valueOf(url.substring(url.indexOf("/share/gym/") + "/share/gym/".length(), url.lastIndexOf("/")));
                        navigationIntent = GymProfileActivity.createIntent(context, gymId, AppConstants.AppNavigation.NAVIGATION_FROM_SHARE);

                    } else if (getIntent().getData().toString().contains("/share/trainer/")) {
                        int trainerId = Integer.valueOf(url.substring(url.indexOf("/share/trainer/") + "/share/trainer/".length(), url.lastIndexOf("/")));
                        navigationIntent = TrainerProfileActivity.createIntent(context, trainerId, AppConstants.AppNavigation.NAVIGATION_FROM_SHARE);

                    }
                   else if (getIntent().getData().toString().contains("/classes/")) {
                        int classSessionId = Integer.valueOf(url.substring(url.indexOf("/classes/") + "/classes/".length(), url.lastIndexOf("/")));
                        navigationIntent = ClassProfileActivity.createIntent(context, classSessionId, AppConstants.AppNavigation.NAVIGATION_FROM_SHARE);

                    } else if (getIntent().getData().toString().contains("/gym/")) {
                        int gymId = Integer.valueOf(url.substring(url.indexOf("/gym/") + "/gym/".length(), url.lastIndexOf("/")));
                        navigationIntent = GymProfileActivity.createIntent(context, gymId, AppConstants.AppNavigation.NAVIGATION_FROM_SHARE);

                    } else if (getIntent().getData().toString().contains("/trainer/")) {
                        int trainerId = Integer.valueOf(url.substring(url.indexOf("/trainer/") + "/trainer/".length(), url.lastIndexOf("/")));
                        navigationIntent = TrainerProfileActivity.createIntent(context, trainerId, AppConstants.AppNavigation.NAVIGATION_FROM_SHARE);

                    }
                    else if (getIntent().getData().toString().contains("/settings")) {
                        navigationIntent = MemberShip.createIntent(context, AppConstants.AppNavigation.NAVIGATION_FROM_DEEPLINK_ACTIVITY);

                    }
                    else if(getIntent().getData().toString().contains("/trainers")){
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_TRAINER, 0);

                    }
                    else if(getIntent().getData().toString().contains("/userschedule")){
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_SCHEDULE, 0);

                    }
                    else if(getIntent().getData().toString().contains("/notifications")){
                        navigationIntent = NotificationActivity.createIntent(context);

                    }
                    else if(getIntent().getData().toString().contains("/editprofile")){
                        navigationIntent = EditProfileActivity.createIntent(context);

                    }
                    else if(getIntent().getData().toString().contains("/profile")){
                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_MY_PROFILE, 0);
                    }

                    else {

                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
                    }

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(navigationIntent);

                    stackBuilder.editIntentAt(0).putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);

                    overridePendingTransition(0,0);
                    stackBuilder.startActivities();
                }
                onTrackingNotification();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);

            overridePendingTransition(0,0);
            HomeActivity.open(context);
        }

        finish();
    }
}
