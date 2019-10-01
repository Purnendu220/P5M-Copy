package com.p5m.me.remote_config;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.graphics.drawable.DrawableCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.fxn.utility.Constants;
import com.p5m.me.utils.LanguageUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static com.facebook.login.widget.ProfilePictureView.TAG;
import static com.p5m.me.remote_config.RemoteConfigConst.ADD_TO_WISHLIST_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOKED_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOKED_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_IN_CLASS_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_IN_CLASS_PROFILE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_WITH_FRIEND_BACKGROUND_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_WITH_FRIEND_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BOOK_WITH_FRIEND_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BUY_CLASS_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.BUY_CLASS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.CLASS_CARD_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FAQ_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.GYM_VISIT_LIMIT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.INVITE_FRIENDS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.JOIN_WAITLIST_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.MEMBERSHIP_OFFER_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PACKAGE_TAGS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_CLASS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_FAILURE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_PACKAGE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_PENDING_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.RECOMMENDED_FOR_YOU_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SEARCH_BAR_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SELECT_PLAN_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SELECT_PLAN_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SESSION_EXPIRED_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.TESTIMONIALS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WAITLISTED_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WAITLIST_BUTTON_KEY;

public class RemoteConfigSetUp {

    public static FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    private static Activity context;

    public static void reInitialize() {
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
    }

    public static void setup(Activity activity) {
        context = activity;


        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(3600)
                .build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);

        /*firebaseRemoteConfig.setConfigSettings(new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(true)
                .build());*/

    }

    public static void setValue(final int constValue, final String key, final String defaultValue) {

        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(key, defaultValue);
        firebaseRemoteConfig.setDefaultsAsync(defaults);
        setValueOfField(constValue, defaultValue);
        if (Constants.LANGUAGE == Locale.ENGLISH || constValue == TESTIMONIALS_KEY ||
                constValue == FAQ_KEY || constValue == PACKAGE_TAGS_KEY) {
            /*final Task<Void> fetch = firebaseRemoteConfig.fetch(BuildConfig.DEBUG ? 0 : TimeUnit.HOURS.toSeconds(10));
            fetch.addOnSuccessListener(context, new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    firebaseRemoteConfig.activate();
                    String keyValue = firebaseRemoteConfig.getString(key);
                    firebaseRemoteConfig.activate();
                    if (!keyValue.isEmpty())
                        setValueOfField(constValue, keyValue);
                }
            });*/

            firebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener(context, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            if (task.isSuccessful()) {
                                boolean updated = task.getResult();
                                Log.d(TAG, "Config params updated: " + updated);
                                firebaseRemoteConfig.activate();
                                String keyValue = firebaseRemoteConfig.getString(key);
                                firebaseRemoteConfig.activate();
                                if (!keyValue.isEmpty())
                                    setValueOfField(constValue, keyValue);

                            }
                        }
                    });
        }

    }

    private static void setValueOfField(int constValue, String keyValue) {
        if (!keyValue.isEmpty()) {
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
                case WAITLISTED_BUTTON_KEY:
                    RemoteConfigConst.WAITLISTED_VALUE = keyValue;
                    break;
                case WAITLIST_BUTTON_KEY:
                    RemoteConfigConst.WAITLIST_VALUE = keyValue;
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
                case JOIN_WAITLIST_KEY:
                    RemoteConfigConst.JOIN_WAITLIST_VALUE = keyValue;
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
                case GYM_VISIT_LIMIT_KEY:
                    RemoteConfigConst.GYM_VISIT_LIMIT_VALUE = keyValue;
                    break;
                case SELECT_PLAN_TEXT_KEY:
                    RemoteConfigConst.SELECT_PLAN_TEXT_VALUE = keyValue;
                    break;
                case SELECT_PLAN_COLOR_KEY:
                    RemoteConfigConst.SELECT_PLAN_COLOR_VALUE = keyValue;
                    break;
                case BOOK_WITH_FRIEND_BACKGROUND_COLOR_KEY:
                    RemoteConfigConst.BOOK_WITH_FRIEND_BACKGROUND_COLOR_VALUE = keyValue;
                    break;
                case GYM_VISIT_LIMIT_DETAIL_TEXT_KEY:
                    RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT_VALUE = keyValue;
                    break;
                case MEMBERSHIP_OFFER_COLOR_KEY:
                    RemoteConfigConst.MEMBERSHIP_OFFER_COLOR_VALUE = keyValue;
                    break;
                case SESSION_EXPIRED_COLOR_KEY:
                    RemoteConfigConst.SESSION_EXPIRED_COLOR_VALUE = keyValue;
                    break;
                case BOOK_IN_CLASS_COLOR_KEY:
                    RemoteConfigConst.BOOK_IN_CLASS_COLOR_VALUE = keyValue;
                    break;

                case TESTIMONIALS_KEY:
                    RemoteConfigConst.TESTIMONIALS_VALUE = keyValue;
                    break;
                case FAQ_KEY:
                    RemoteConfigConst.FAQ_VALUE = keyValue;
                    break;

                case PACKAGE_TAGS_KEY:
                    RemoteConfigConst.PACKAGE_TAGS_VALUE = keyValue;
                    break;
            }
        }
     /*   }
        else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")) {
            if (!keyValue.isEmpty()) {
                switch (constValue) {
                    case TESTIMONIALS_KEY:
                        RemoteConfigConst.TESTIMONIALS_VALUE = keyValue;
                        break;
                    case FAQ_KEY:
                        RemoteConfigConst.FAQ_VALUE = keyValue;
                        break;

                    case PACKAGE_TAGS_KEY:
                        RemoteConfigConst.PACKAGE_TAGS_VALUE = keyValue;
                        break;
                }
            }
        }*/

    }

    public static void setBackgroundColor(View view, String keyValue, int defaultColor) {
        int color = defaultColor;
        if (!keyValue.isEmpty()) {
            try {
                color = Color.parseColor(keyValue);
            } catch (Exception e) {
                color = defaultColor;
            }
        }
        if (view instanceof TextView) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), color);
                setBackgroundDrawable(view, wrappedDrawable);
            } else
                view.setBackgroundColor(color);

        } else if (view instanceof Button) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), color);
                setBackgroundDrawable(view, wrappedDrawable);
            } else
                view.setBackgroundColor(color);
        } else if (view instanceof LinearLayout) {
            Drawable wrappedDrawable = DrawableCompat.wrap(view.getBackground());
            if (wrappedDrawable != null) {
                DrawableCompat.setTint(wrappedDrawable.mutate(), color);
                setBackgroundDrawable(view, wrappedDrawable);
            } else
                view.setBackgroundColor(color);
        }


    }

    public static void setBackgroundDrawable(View view, Drawable drawable) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(drawable);
        } else {
            view.setBackground(drawable);
        }
    }

    public static void setTextColor(View view, String keyValue, int defaultColor) {
        int color = defaultColor;
        if (!keyValue.isEmpty()) {
            try {
                color = Color.parseColor(keyValue);
            } catch (Exception e) {
                color = defaultColor;
            }
        }

        if (view instanceof TextView)
            ((TextView) view).setTextColor(color);
        else if (view instanceof Button)
            ((Button) view).setTextColor(color);
    }

    public static void getValues() {
        /* Text Changes */
        setValue(RemoteConfigConst.BOOKED_BUTTON_KEY,
                RemoteConfigConst.BOOKED_BUTTON,
                context.getString(R.string.booked));

        setValue(RemoteConfigConst.WAITLISTED_BUTTON_KEY,
                RemoteConfigConst.WAITLISTED_BUTTON,
                context.getString(R.string.waitlisted));
        setValue(WAITLIST_BUTTON_KEY,
                RemoteConfigConst.WAITLIST_BUTTON,
                context.getString(R.string.waitlist));

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
                RemoteConfigConst.FULL_BUTTON_COLOR, "#FF0000");

        setValue(RemoteConfigConst.BOOK_BUTTON_COLOR_KEY,
                RemoteConfigConst.BOOK_BUTTON_COLOR, "#3d85ea");

        setValue(RemoteConfigConst.BOOKED_BUTTON_COLOR_KEY,
                RemoteConfigConst.BOOKED_BUTTON_COLOR, "#FF8C00");

        setValue(RemoteConfigConst.CLASS_CARD_TEXT_KEY,
                RemoteConfigConst.CLASS_CARD_TEXT, context.getString(R.string.reserve_class));

        setValue(RemoteConfigConst.RECOMMENDED_FOR_YOU_KEY,
                RemoteConfigConst.RECOMMENDED_FOR_YOU, context.getString(R.string.recomended_for_you));

        setValue(RemoteConfigConst.BUY_CLASS_KEY,
                RemoteConfigConst.BUY_CLASS, context.getString(R.string.buy_classes));

        setValue(RemoteConfigConst.BUY_CLASS_COLOR_KEY,
                RemoteConfigConst.BUY_CLASS_BUTTON_COLOR, "#3d85ea");

        setValue(BOOK_IN_CLASS_COLOR_KEY,
                RemoteConfigConst.BOOK_IN_CLASS_COLOR, "#3d85ea");

        setValue(BOOK_WITH_FRIEND_COLOR_KEY,
                RemoteConfigConst.BOOK_WITH_FRIEND_COLOR, "#3d85ea");
        setValue(RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_KEY,
                RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR, "#262626");
        setValue(SESSION_EXPIRED_COLOR_KEY,
                RemoteConfigConst.SESSION_EXPIRED_COLOR, "#FF0000");

        setValue(RemoteConfigConst.SESSION_EXPIRED_KEY,
                RemoteConfigConst.SESSION_EXPIRED, context.getResources().getString(R.string.expired));

        setValue(RemoteConfigConst.INVITE_FRIENDS_KEY,
                RemoteConfigConst.INVITE_FRIENDS, context.getResources().getString(R.string.invite_friends));

        setValue(ADD_TO_WISHLIST_KEY,
                RemoteConfigConst.ADD_TO_WISHLIST, context.getResources().getString(R.string.add_to_WishList));

        setValue(JOIN_WAITLIST_KEY,
                RemoteConfigConst.JOIN_WAITLIST, context.getResources().getString(R.string.join_waitlist));
        setValue(PAYMENT_PACKAGE_KEY,
                RemoteConfigConst.PAYMENT_PACKAGE, context.getResources().getString(R.string.book_classes));
        setValue(PAYMENT_CLASS_KEY,
                RemoteConfigConst.PAYMENT_CLASS, context.getResources().getString(R.string.view_schedule));
        setValue(PAYMENT_PENDING_KEY,
                RemoteConfigConst.PAYMENT_PENDING, context.getResources().getString(R.string.payment_history));
        setValue(PAYMENT_FAILURE_KEY,
                RemoteConfigConst.PAYMENT_FAILURE, context.getResources().getString(R.string.payment_history));
        setValue(GYM_VISIT_LIMIT_KEY,
                RemoteConfigConst.GYM_VISIT_LIMIT_TEXT, context.getResources().getString(R.string.view_gym_visit_limits));
        setValue(SELECT_PLAN_TEXT_KEY,
                RemoteConfigConst.SELECT_PLAN_TEXT, context.getResources().getString(R.string.select_plan));

        setValue(SELECT_PLAN_COLOR_KEY,
                RemoteConfigConst.SELECT_PLAN_COLOR, "#3d85ea");

        setValue(GYM_VISIT_LIMIT_DETAIL_TEXT_KEY,
                RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT, context.getResources().getString(R.string.package_limit_header));

        setValue(MEMBERSHIP_OFFER_COLOR_KEY,
                RemoteConfigConst.MEMBERSHIP_OFFER_COLOR, "#1FB257");

        setValue(TESTIMONIALS_KEY,
                RemoteConfigConst.TESTIMONIALS, "[{\"name\":\"XEINA\", \"message_ar\": \"\", \"message_eng\":\"I love how P5M now includes the option of booking classes with friends.\"},{\"name\":\"RANA\", \"message_ar\": \"\", \"message_eng\":\"Great way to try out new gyms & classes and decide where you wanna go.\"}]");
        setValue(FAQ_KEY,
                RemoteConfigConst.FAQ, "[{\"english_question\": \"Why are you use P5M?\", \"english_answer\": \"P5M app enables you to visit multiple gyms and attend diffent activities with only one membership. You can even buy monthly subscriptions if you wish that is how you would like to roll.\", \"arabic_question\": \"\", \"arabic_answer\": \"\"}]");
        setValue(PACKAGE_TAGS_KEY,
                RemoteConfigConst.PACKAGE_TAGS, "[{\"id\": 2, \"tag_en\": \"\", \"tag_ar\": \"\"}, {\"id\": 3, \"tag_en\": \"\", \"tag_ar\": \"\"}, {\"id\": 4, \"tag_en\": \"\", \"tag_ar\": \"\"}, {\"id\": 10, \"tag_en\": \"\", \"tag_ar\": \"\"}, {\"id\": 11, \"tag_en\": \"\", \"tag_ar\": \"\"}]");

    }

}
