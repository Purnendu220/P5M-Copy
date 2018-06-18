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
                        navigationIntent = ClassProfileActivity.createIntent(context, classSessionId);

                    } else if (getIntent().getData().toString().contains("/share/gym/")) {

                        int gymId = Integer.valueOf(url.substring(url.indexOf("/share/gym/") + "/share/gym/".length(), url.lastIndexOf("/")));
                        navigationIntent = GymProfileActivity.createIntent(context, gymId);

                    } else if (getIntent().getData().toString().contains("/share/trainer/")) {

                        int trainerId = Integer.valueOf(url.substring(url.indexOf("/share/trainer/") + "/share/trainer/".length(), url.lastIndexOf("/")));
                        navigationIntent = TrainerProfileActivity.createIntent(context, trainerId);

                    } else {

                        navigationIntent = HomeActivity.createIntent(context, AppConstants.Tab.TAB_FIND_CLASS, 0);
                    }

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(navigationIntent);

                    overridePendingTransition(0,0);
                    stackBuilder.startActivities();
                }

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
