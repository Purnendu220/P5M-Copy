package com.p5m.me.remote_config;

import android.app.Activity;
import android.content.Context;
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

import static com.p5m.me.remote_config.RemoteConfigConst.BOOKED_BUTTON_COLOR_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOKED_BUTTON_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_BUTTON_COLOR_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_BUTTON_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_WITH_FRIEND_BUTTON_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_COLOR_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_VALUE;

public class RemoteConfigSetUp {
    public static final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();

    private static Activity context;


    public static void setup(Activity activity) {

        firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());

        context = activity;

    }

    public static void setConfig(Activity activity, final View view, final String key, final String defaultValue, final RemoteConfigConst.ConfigStatus configStatus) {


        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(key, defaultValue);
        firebaseRemoteConfig.setDefaults(defaults);
        final Task<Void> fetch = firebaseRemoteConfig.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toSeconds(1));
        fetch.addOnSuccessListener(activity, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
                methodOfUpdate(view, key, defaultValue, configStatus);
            }

        });
    }

    public static void setValue(final int constValue, final String key, final String defaultValue) {

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(key, defaultValue);
        firebaseRemoteConfig.setDefaults(defaults);

        final Task<Void> fetch = firebaseRemoteConfig.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toSeconds(1));
        fetch.addOnSuccessListener(context, new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                firebaseRemoteConfig.activateFetched();
                String keyValue = firebaseRemoteConfig.getString(key);
                firebaseRemoteConfig.activateFetched();
                switch (constValue) {
                    case BOOKED_BUTTON_VALUE:
                        RemoteConfigConst.BOOKED = keyValue;
                        break;
                    case BOOK_BUTTON_VALUE:
                        RemoteConfigConst.BOOK = keyValue;
                        break;
                    case FULL_BUTTON_VALUE:
                        RemoteConfigConst.FULL = keyValue;
                        break;
                     case BOOK_WITH_FRIEND_BUTTON_VALUE:
                        RemoteConfigConst.BOOK_WITH_FRIEND_VALUE = keyValue;
                        break;
                    case BOOKED_BUTTON_COLOR_VALUE:
                        RemoteConfigConst.BOOKED_COLOR = keyValue;
                        break;
                    case BOOK_BUTTON_COLOR_VALUE:
                        RemoteConfigConst.BOOK_COLOR = keyValue;
                        break;
                    case FULL_BUTTON_COLOR_VALUE:
                        RemoteConfigConst.FULL_COLOR = keyValue;
                        break;


                }
            }
        });
    }


    public static void methodOfUpdate(View view, String key, String defaultValue, RemoteConfigConst.ConfigStatus configStatus) {
        String keyValue = firebaseRemoteConfig.getString(key);
        firebaseRemoteConfig.activateFetched();
        if (keyValue != null)
            onSetValue(view, keyValue, configStatus);
        else
            onSetValue(view, defaultValue, configStatus);
    }

    private static void onSetValue(View view, String keyValue, RemoteConfigConst.ConfigStatus configStatus) {
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

    public static void setColor(View view, String keyValue) {
        int color;
        try {
            color = Color.parseColor(keyValue);
        } catch (IllegalArgumentException e) {
            color = Color.parseColor("#3d85ea");
        }
        if (view instanceof TextView) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), color);
                setBackgroundDrawable(view, wrappedDrawable);


            } else
                ((TextView) view).setBackgroundColor(color);

        } else if (view instanceof Button) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), color);
                setBackgroundDrawable(view, wrappedDrawable);
            } else
                ((Button) view).setBackgroundColor(color);
        }
    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    private static void setText(View view, String keyValue) {
        if (view instanceof TextView)
            ((TextView) view).setText(keyValue);
        else if (view instanceof Button)
            ((Button) view).setText(keyValue);
    }

    private static void setHint(View view, String keyValue) {
        if (view instanceof TextView)
            ((TextView) view).setHint(keyValue);
    }

}
