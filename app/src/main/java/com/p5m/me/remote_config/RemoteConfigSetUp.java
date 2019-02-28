package com.p5m.me.remote_config;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.widget.Button;
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


    public void setConfig(final Activity context, final View view, final String key, String defaultValue, final RemoteConfigConst.ConfigStatus configStatus) {
        this.context = context;
        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(key, defaultValue);
        firebaseRemoteConfig.setDefaults(defaults);
        final Task<Void> fetch = firebaseRemoteConfig.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toSeconds(2));
        fetch.addOnSuccessListener(context, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
                methodOfUpdate(view, key, configStatus);
            }

        });
    }


    public void methodOfUpdate(View view, String key, RemoteConfigConst.ConfigStatus configStatus) {
        String keyValue = firebaseRemoteConfig.getString(key);
        firebaseRemoteConfig.activateFetched();
        onSetValue(view, keyValue, configStatus);

    }

    private void onSetValue(View view, String keyValue, RemoteConfigConst.ConfigStatus configStatus) {
        switch (configStatus) {
            case TEXT:
                setText(view, keyValue);
                break;
            case HINT:
                setHint(view, keyValue);
                break;
            case COLOR:
                setColor(view, keyValue);
                break;
        }
    }

    private void setColor(View view, String keyValue) {
        if (view instanceof TextView) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), Color.parseColor(keyValue));
                setBackgroundDrawable(view, wrappedDrawable);
            }
            else
                ((TextView) view).setBackgroundColor(Color.parseColor(keyValue));

        } else if (view instanceof Button) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), Color.parseColor(keyValue));
                setBackgroundDrawable(view, wrappedDrawable);
            }else
                ((Button) view).setBackgroundColor(Color.parseColor(keyValue));
        }
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    private void setText(View view, String keyValue) {
        if (view instanceof TextView)
            ((TextView) view).setText(keyValue);
        else if (view instanceof Button)
            ((Button) view).setText(keyValue);
    }

    private void setHint(View view, String keyValue) {
        if (view instanceof TextView)
            ((TextView) view).setHint(keyValue);
    }

}
