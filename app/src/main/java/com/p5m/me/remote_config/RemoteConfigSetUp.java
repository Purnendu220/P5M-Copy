package com.p5m.me.remote_config;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.p5m.me.BuildConfig;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class RemoteConfigSetUp {
    public FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    public Activity context;


    public void setConfig(final  Activity context, final TextView textView, final String key, String defaultValue) {
        this.context = context;
        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());

        HashMap<String,Object> defaults = new HashMap<>();
        defaults.put(key,defaultValue);
        firebaseRemoteConfig.setDefaults(defaults);
        final Task<Void> fetch= firebaseRemoteConfig.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toSeconds(2));
        fetch.addOnSuccessListener(context, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
                methodOfUpdate(textView,key);
            }

        });
    }


    public void methodOfUpdate(TextView textView, String key) {
        String buyClassString=firebaseRemoteConfig.getString(key);
        firebaseRemoteConfig.activateFetched();
        textView.setText(buyClassString);
    }

}
