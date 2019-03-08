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
import com.p5m.me.R;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static com.p5m.me.remote_config.RemoteConfigConst.ADD_TO_WISHLIST_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOKED_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOKED_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_IN_CLASS_PROFILE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_WITH_FRIEND_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_WITH_FRIEND_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BUY_CLASS_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BUY_CLASS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.INVITE_FRIENDS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_CLASS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_FAILURE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_PACKAGE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_PENDING_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.RECOMMENDED_FOR_YOU_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SEARCH_BAR_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.CLASS_CARD_TEXT_KEY;

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
                    case BOOKED_BUTTON_KEY:
                        RemoteConfigConst.BOOKED_VALUE = keyValue;
                        break;
                    case BOOK_BUTTON_KEY:
                        RemoteConfigConst.BOOK_VALUE = keyValue;
                        break;
                    case FULL_BUTTON_KEY:
                        RemoteConfigConst.FULL_VALUE = keyValue;
                        break;
                    case BOOK_WITH_FRIEND_BUTTON_KEY:
                        RemoteConfigConst.BOOK_WITH_FRIEND_VALUE = keyValue;
                        break;
                    case BOOK_WITH_FRIEND_COLOR_KEY:
                        RemoteConfigConst.BOOK_WITH_FRIEND_COLOR_VALUE = keyValue;
                        break;
                    case SEARCH_BAR_TEXT_KEY:
                        RemoteConfigConst.SEARCH_BAR_TEXT_VALUE = keyValue;
                        break;
                    case BOOK_IN_CLASS_PROFILE_KEY:
                        RemoteConfigConst.BOOK_IN_CLASS_VALUE = keyValue;
                        break;
                    case BOOKED_BUTTON_COLOR_KEY:
                        RemoteConfigConst.BOOKED_COLOR_VALUE = keyValue;
                        break;
                    case BOOK_BUTTON_COLOR_KEY:
                        RemoteConfigConst.BOOK_COLOR_VALUE = keyValue;
                        break;
                    case FULL_BUTTON_COLOR_KEY:
                        RemoteConfigConst.FULL_COLOR_VALUE = keyValue;
                        break;
                    case CLASS_CARD_TEXT_KEY:
                        RemoteConfigConst.CLASS_CARD_TEXT_VALUE = keyValue;
                        break;

                    case RECOMMENDED_FOR_YOU_KEY:
                        RemoteConfigConst.RECOMMENDED_FOR_YOU_VALUE = keyValue;
                        break;

                    case RECOMMENDED_FOR_YOU_COLOR_KEY:
                        RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_VALUE = keyValue;
                        break;

                    case BUY_CLASS_KEY:
                        RemoteConfigConst.BUY_CLASS_VALUE = keyValue;
                        break;

                    case BUY_CLASS_COLOR_KEY:
                        RemoteConfigConst.BUY_CLASS_COLOR_VALUE = keyValue;
                        break;
                    case ADD_TO_WISHLIST_KEY:
                        RemoteConfigConst.ADD_TO_WISHLIST_VALUE = keyValue;
                        break;
                    case INVITE_FRIENDS_KEY:
                        RemoteConfigConst.INVITE_FRIENDS_VALUE = keyValue;
                        break;
                    case PAYMENT_PACKAGE_KEY:
                        RemoteConfigConst.PAYMENT_PACKAGE_VALUE = keyValue;
                        break;
                    case PAYMENT_PENDING_KEY:
                        RemoteConfigConst.PAYMENT_PENDING_VALUE = keyValue;
                        break;
                    case PAYMENT_FAILURE_KEY:
                        RemoteConfigConst.PAYMENT_FAILURE_VALUE = keyValue;
                        break;
                    case PAYMENT_CLASS_KEY:
                        RemoteConfigConst.PAYMENT_CLASS_VALUE = keyValue;
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
//            case COLOR:
//                setBackgroundColor(view, keyValue);
//                break;
        }
    }

    public static void setBackgroundColor(View view, String keyValue, int defaultColor) {
        int color;
        try {
            color = Color.parseColor(keyValue);
        } catch (IllegalArgumentException e) {
            color = defaultColor;
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

    public static void setTextColor(View view, String keyValue, int defaultColor) {
        int color;
        try {
            color = Color.parseColor(keyValue);
        } catch (IllegalArgumentException e) {
            color = defaultColor;
        }
        if (view instanceof TextView)
            ((TextView) view).setTextColor(color);
        else if (view instanceof Button)
            ((Button) view).setTextColor(color);
    }

    private static void setHint(View view, String keyValue) {
        if (view instanceof TextView)
            ((TextView) view).setHint(keyValue);
    }

    public static void getValues() {
        /* Text Changes */
        setValue(RemoteConfigConst.BOOKED_BUTTON_KEY,
                RemoteConfigConst.BOOKED_BUTTON,
                context.getString(R.string.booked));

        setValue(RemoteConfigConst.FULL_BUTTON_KEY,
                RemoteConfigConst.FULL_BUTTON,
                context.getString(R.string.full));

        setValue(RemoteConfigConst.BOOK_BUTTON_KEY,
                RemoteConfigConst.BOOK_BUTTON,
                context.getString(R.string.book));

        setValue(RemoteConfigConst.BOOK_WITH_FRIEND_BUTTON_KEY,
                RemoteConfigConst.BOOK_WITH_FRIEND,
                context.getString(R.string.reserve_class_with_friend));

        setValue(BOOK_IN_CLASS_PROFILE_KEY,
                RemoteConfigConst.BOOK_IN_CLASS,
                context.getString(R.string.reserve_class));

        setValue(SEARCH_BAR_TEXT_KEY,
                RemoteConfigConst.SEARCH_TEXT,
                context.getString(R.string.search_hint));

        /* Color Changes */
        setValue(RemoteConfigConst.FULL_BUTTON_COLOR_KEY,
                RemoteConfigConst.FULL_BUTTON_COLOR, "#3d85ea");

        setValue(RemoteConfigConst.BOOK_BUTTON_COLOR_KEY,
                RemoteConfigConst.BOOK_BUTTON_COLOR, "#3d85ea");

        setValue(RemoteConfigConst.BOOKED_BUTTON_COLOR_KEY,
                RemoteConfigConst.BOOKED_BUTTON_COLOR, "#3d85ea");

        setValue(RemoteConfigConst.CLASS_CARD_TEXT_KEY,
                RemoteConfigConst.CLASS_CARD_TEXT, context.getString(R.string.reserve_class));

        setValue(RemoteConfigConst.RECOMMENDED_FOR_YOU_KEY,
                RemoteConfigConst.RECOMMENDED_FOR_YOU, context.getString(R.string.recomended_for_you));

        setValue(RemoteConfigConst.BUY_CLASS_KEY,
                RemoteConfigConst.BUY_CLASS, context.getString(R.string.buy_classes));
        setValue(RemoteConfigConst.BUY_CLASS_COLOR_KEY,
                RemoteConfigConst.BUY_CLASS_BUTTON_COLOR, "#3d85ea");
        setValue(RemoteConfigConst.BOOK_BUTTON_COLOR_KEY,
                RemoteConfigConst.BOOK_IN_CLASS_COLOR, "#3d85ea");
        setValue(BOOK_WITH_FRIEND_COLOR_KEY,
                RemoteConfigConst.BOOK_WITH_FRIEND_COLOR, "#3d85ea");


        setValue(RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_KEY,
                RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR, "#3d85ea");
        setValue(RemoteConfigConst.SESSION_EXPIRED_COLOR_KEY,
                RemoteConfigConst.SESSION_EXPIRED_COLOR, "#CCFF0000");

        setValue(RemoteConfigConst.SESSION_EXPIRED_KEY,
                RemoteConfigConst.SESSION_EXPIRED, context.getResources().getString(R.string.expired));

        setValue(RemoteConfigConst.INVITE_FRIENDS_KEY,
                RemoteConfigConst.INVITE_FRIENDS, context.getResources().getString(R.string.invite_friends));

        setValue(ADD_TO_WISHLIST_KEY,
                RemoteConfigConst.ADD_TO_WISHLIST, context.getResources().getString(R.string.add_to_WishList));
        setValue(PAYMENT_PACKAGE_KEY,
                RemoteConfigConst.PAYMENT_PACKAGE, context.getResources().getString(R.string.book_classes));
        setValue(PAYMENT_CLASS_KEY,
                RemoteConfigConst.PAYMENT_CLASS, context.getResources().getString(R.string.view_schedule));
        setValue(PAYMENT_PENDING_KEY,
                RemoteConfigConst.PAYMENT_PENDING, context.getResources().getString(R.string.payment_history));
        setValue(PAYMENT_FAILURE_KEY,
                RemoteConfigConst.PAYMENT_FAILURE, context.getResources().getString(R.string.payment_history));


    }
}
