package com.p5m.me.view.activity.Main;

import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;

import com.p5m.me.R;
import com.p5m.me.helper.Helper;
import com.p5m.me.notifications.HandleNotificationDeepLink;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.view.activity.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

public class DeepLinkActivity extends BaseActivity {

    private Intent navigationIntent;

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
                    if (url.contains("/aboutus")) {
                        forwardToBrowser(getIntent());
                    }
                    else if (url.contains("/privacy")) {
                        forwardToBrowser(getIntent());
                    } else if (url.contains("/terms")) {
                        forwardToBrowser(getIntent());
                    } else {
                        navigationIntent = HandleNotificationDeepLink.handleNotificationDeeplinking(context, url);


                        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                        stackBuilder.addNextIntentWithParentStack(navigationIntent);

                        stackBuilder.editIntentAt(0).putExtra(AppConstants.DataKey.IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN, true);

                        overridePendingTransition(0, 0);
                        stackBuilder.startActivities();
                    }
                }
                onTrackingNotification();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);

            overridePendingTransition(0, 0);
            HomeActivity.open(context);
        }

    }

    private void forwardToBrowser(Intent i) {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(i.getData(), i.getType());
        List<ResolveInfo> activities = getPackageManager().queryIntentActivities(intent, 0);
        ArrayList<Intent> targetIntents = new ArrayList<Intent>();
        String thisPackageName = getApplicationContext().getPackageName();
        for (ResolveInfo currentInfo : activities) {
            String packageName = currentInfo.activityInfo.packageName;
            if (!thisPackageName.equals(packageName)) {
                Intent targetIntent = new Intent(android.content.Intent.ACTION_VIEW);
                targetIntent.setDataAndType(intent.getData(), intent.getType());
                targetIntent.setPackage(intent.getPackage());
                targetIntent.setComponent(new ComponentName(packageName, currentInfo.activityInfo.name));
                targetIntents.add(targetIntent);
            }
        }
        if (targetIntents.size() > 0) {
            Intent chooserIntent = Intent.createChooser(targetIntents.remove(0), "Open with");
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetIntents.toArray(new Parcelable[]{}));
            startActivity(chooserIntent);
            finish();
        }
    }
}
