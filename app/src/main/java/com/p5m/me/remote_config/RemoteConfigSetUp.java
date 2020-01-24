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

import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.fxn.utility.Constants;

import java.util.HashMap;
import java.util.Locale;

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
import static com.p5m.me.remote_config.RemoteConfigConst.DROP_IN_COST_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FAQ_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.FULL_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.GYM_VISIT_LIMIT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.INVITE_FRIENDS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.JOIN_WAITLIST_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.MEMBERSHIP_OFFER_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.ON_BOARDING_DATA_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PACKAGE_TAGS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_CLASS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_FAILURE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_PACKAGE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PAYMENT_PENDING_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.PLAN_DESCRIPTION_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.RECOMMENDED_FOR_YOU_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SEARCH_BAR_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_FOUR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_ONE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_THREE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_SUB_TWO_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_ONE_TITLE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_FOUR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_ONE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_THREE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_SUB_TWO_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SECTION_TWO_TITLE_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SELECT_PLAN_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SELECT_PLAN_TEXT_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SESSION_EXPIRED_COLOR_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.SHOW_SELECTION_OPTIONS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.TESTIMONIALS_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.TESTIMONIALS_RIYADH_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.TESTIMONIALS_RIYADH_VALUE;
import static com.p5m.me.remote_config.RemoteConfigConst.WAITLISTED_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WAITLIST_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WAITLIST_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WAITLIST_BUTTON_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WELCOME_P5M_CONTAIN_KEY;
import static com.p5m.me.remote_config.RemoteConfigConst.WELCOME_P5M_KEY;

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

    }


    public static void setValue(final int constValue, final String key, final String defaultValue) {
        long cacheExpiration = 0;
        HashMap<String, Object> defaults = new HashMap<>();
        defaults.put(key, defaultValue);
        firebaseRemoteConfig.setDefaultsAsync(defaults);
        setValueOfField(constValue, defaultValue);
        if (Constants.LANGUAGE == Locale.ENGLISH || constValue == TESTIMONIALS_KEY ||
                constValue == FAQ_KEY ||constValue == ON_BOARDING_DATA_KEY || constValue == SECTION_ONE_SUB_ONE_KEY || constValue == WELCOME_P5M_KEY || constValue == WELCOME_P5M_CONTAIN_KEY ||
                constValue == SECTION_ONE_TITLE_KEY || constValue == PACKAGE_TAGS_KEY || constValue == SECTION_ONE_SUB_ONE_CONTAIN_KEY || constValue == SECTION_ONE_SUB_TWO_KEY ||
                constValue == SECTION_ONE_SUB_TWO_CONTAIN_KEY || constValue == SECTION_ONE_SUB_THREE_KEY || constValue == SECTION_ONE_SUB_THREE_DETAIL_KEY || constValue == SECTION_ONE_SUB_FOUR_KEY ||
                constValue == SECTION_ONE_SUB_FOUR_DETAIL_KEY || constValue == SECTION_TWO_TITLE_KEY || constValue == SECTION_TWO_SUB_ONE_KEY || constValue == SECTION_TWO_SUB_ONE_DETAIL_KEY ||
                constValue == SECTION_TWO_SUB_TWO_KEY || constValue == SECTION_TWO_SUB_TWO_DETAIL_KEY || constValue == SECTION_TWO_SUB_THREE_KEY || constValue == SECTION_TWO_SUB_THREE_DETAIL_KEY ||
                constValue == SECTION_TWO_SUB_FOUR_KEY || constValue == SECTION_TWO_SUB_FOUR_DETAIL_KEY

        ) {

            if (BuildConfig.FIREBASE_IS_PRODUCTION) {
                firebaseRemoteConfig.fetchAndActivate()
                        .addOnCompleteListener(context, new OnCompleteListener<Boolean>() {
                            @Override
                            public void onComplete(@NonNull Task<Boolean> task) {
                                if (task.isSuccessful()) {
                                    boolean updated = task.getResult();
                                    firebaseRemoteConfig.activate();
                                    String keyValue = firebaseRemoteConfig.getString(key);
                                    firebaseRemoteConfig.activate();
                                    if (!keyValue.isEmpty())
                                        setValueOfField(constValue, keyValue);

                                }
                            }
                        });
            } else {
                firebaseRemoteConfig.fetch(cacheExpiration)
                        .addOnCompleteListener(context, new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    firebaseRemoteConfig.activate();
                                    String keyValue = firebaseRemoteConfig.getString(key);
                                    firebaseRemoteConfig.activate();
                                    if (!keyValue.isEmpty())
                                        setValueOfField(constValue, keyValue);

                                } else {

                                }
                            }
                        });
            }


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
                 case TESTIMONIALS_RIYADH_KEY:
                    RemoteConfigConst.TESTIMONIALS_RIYADH_VALUE = keyValue;
                    break;
                case FAQ_KEY:
                    RemoteConfigConst.FAQ_VALUE = keyValue;
                    break;

                case PACKAGE_TAGS_KEY:
                    RemoteConfigConst.PACKAGE_TAGS_VALUE = keyValue;
                    break;
                case WELCOME_P5M_KEY:
                    RemoteConfigConst.WELCOME_P5M_VALUE = keyValue;
                    break;
                case RemoteConfigConst.WELCOME_P5M_CONTAIN_KEY:
                    RemoteConfigConst.WELCOME_P5M_CONTAIN_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_ONE_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_ONE_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_TWO_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_TWO_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_TITLE_KEY:
                    RemoteConfigConst.SECTION_ONE_TITLE_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_THREE_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_THREE_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_FOUR_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_FOUR_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_KEY:
                    RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_TITLE_KEY:
                    RemoteConfigConst.SECTION_TWO_TITLE_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_ONE_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_ONE_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_TWO_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_TWO_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_THREE_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_THREE_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_FOUR_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_FOUR_VALUE = keyValue;
                    break;
                case RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_KEY:
                    RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_VALUE = keyValue;
                    break;
                case PLAN_DESCRIPTION_KEY:
                    RemoteConfigConst.PLAN_DESCRIPTION_VALUE = keyValue;
                    break;
                case DROP_IN_COST_KEY:
                    RemoteConfigConst.DROP_IN_COST_VALUE = keyValue;
                    break;
                case SHOW_SELECTION_OPTIONS_KEY:
                    RemoteConfigConst.SHOW_SELECTION_OPTIONS_VALUE = keyValue;
                    break;
                case PLAN_DESCRIPTION_DROP_IN_KEY:
                    RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN_VALUE = keyValue;
                    break;
                case ON_BOARDING_DATA_KEY:
                    RemoteConfigConst.ON_BOARDING_DATA_VALUE = keyValue;
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
        setValue(WELCOME_P5M_KEY,
                RemoteConfigConst.WELCOME_P5M, context.getResources().getString(R.string.welcome_to_p5m));
        setValue(RemoteConfigConst.WELCOME_P5M_CONTAIN_KEY,
                RemoteConfigConst.WELCOME_P5M_CONTAIN, context.getResources().getString(R.string.on_p5m_you));

        setValue(RemoteConfigConst.SECTION_ONE_TITLE_KEY,
                RemoteConfigConst.SECTION_ONE_TITLE, context.getResources().getString(R.string.with_the_p5m_membership_you_can));
        setValue(RemoteConfigConst.SECTION_ONE_SUB_ONE_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_ONE, context.getResources().getString(R.string.variety_of_activities));
        setValue(RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN, context.getResources().getString(R.string.variety_of_activity_details));


        setValue(RemoteConfigConst.SECTION_ONE_SUB_TWO_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_TWO, context.getResources().getString(R.string.visit_gym_feature));


        setValue(RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN, context.getResources().getString(R.string.exploreDetail));

        setValue(RemoteConfigConst.SECTION_ONE_SUB_THREE_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_THREE, context.getResources().getString(R.string.save_percent));

        setValue(RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL, context.getResources().getString(R.string.save_detail));

        setValue(RemoteConfigConst.SECTION_ONE_SUB_FOUR_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_FOUR, context.getResources().getString(R.string.instant_booking));
        setValue(RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_KEY,
                RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL, context.getResources().getString(R.string.instant_booking_detail));

        setValue(RemoteConfigConst.SECTION_TWO_TITLE_KEY,
                RemoteConfigConst.SECTION_TWO_TITLE, context.getResources().getString(R.string.how_its_work));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_ONE_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_ONE, context.getResources().getString(R.string.explore));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL, context.getResources().getString(R.string.exploreDetail));
        setValue(RemoteConfigConst.SECTION_TWO_SUB_TWO_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_TWO, context.getResources().getString(R.string.pick_a_plan));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_TWO_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_TWO, context.getResources().getString(R.string.pick_a_plan));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL, context.getResources().getString(R.string.that_work_for_you));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_THREE_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_THREE, context.getResources().getString(R.string.train));


        setValue(RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL, context.getResources().getString(R.string.workout_any_gym));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_FOUR_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_FOUR, context.getResources().getString(R.string.get_discount));

        setValue(RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_KEY,
                RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL, context.getResources().getString(R.string.go_to_different_gym));

        setValue(SELECT_PLAN_TEXT_KEY,
                RemoteConfigConst.SELECT_PLAN_TEXT, context.getResources().getString(R.string.select_plan));

        setValue(PLAN_DESCRIPTION_KEY,
                RemoteConfigConst.PLAN_DESCRIPTION, context.getResources().getString(R.string.plan_description));

        setValue(DROP_IN_COST_KEY,
                RemoteConfigConst.DROP_IN_COST, context.getResources().getString(R.string.show_drop_in_cost));

        setValue(SHOW_SELECTION_OPTIONS_KEY,
                RemoteConfigConst.SHOW_SELECTION_OPTIONS, context.getResources().getString(R.string.show_selection_option));
        setValue(PLAN_DESCRIPTION_DROP_IN_KEY,
                RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN, context.getResources().getString(R.string.drop_in_description));
        setValue(ON_BOARDING_DATA_KEY,
                RemoteConfigConst.ON_BOARDING_DATA, "");

    }


}
