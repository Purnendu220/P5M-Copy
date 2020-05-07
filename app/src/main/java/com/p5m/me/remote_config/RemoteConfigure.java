package com.p5m.me.remote_config;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.p5m.me.BuildConfig;
import com.p5m.me.R;
import com.p5m.me.utils.LanguageUtils;

public class RemoteConfigure {
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    FirebaseRemoteConfigSettings configSettings;
    long cacheExpiration = 0;
    private Context context;

    public RemoteConfigure() {

        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(BuildConfig.DEBUG)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar"))
            mFirebaseRemoteConfig.setDefaults(R.xml.remote_configure_ar);
        else
            mFirebaseRemoteConfig.setDefaults(R.xml.remote_configure_defaults);
    }

    public void fetchRemoteConfig(Context context) {
        this.context = context;
        if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("ar")
        ) {
            mFirebaseRemoteConfig.fetch(getCacheExpiration())
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If is successful, activated fetched
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.setDefaults(R.xml.remote_configure_ar);
                                mFirebaseRemoteConfig.fetchAndActivate();

                                setArabicConfigValues();
                            }
                        }
                    });
        } else if (LanguageUtils.getLocalLanguage().equalsIgnoreCase("en")

        ) {
            mFirebaseRemoteConfig.fetch(getCacheExpiration())
                    .addOnCompleteListener((Activity) context, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            // If is successful, activated fetched
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.fetchAndActivate();
                            }
                            setValuesConfigValues();
                        }
                    });
        } else {
            setValuesConfigValues();
        }


    }

    public long getCacheExpiration() {
        // If is developer mode, cache expiration set to 0, in order to test
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        return cacheExpiration;
    }

    public void setValuesConfigValues() {
        RemoteConfigConst.CHANNEL_LIST = mFirebaseRemoteConfig.getString(RemoteConfigConst.CHANNEL_KEY);
        RemoteConfigConst.SEARCH_BAR_TEXT_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SEARCH_TEXT);
        RemoteConfigConst.BOOK_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOK_BUTTON);
        RemoteConfigConst.BOOK_IN_CLASS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOK_IN_CLASS);
        RemoteConfigConst.BOOK_WITH_FRIEND_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOK_WITH_FRIEND);
        RemoteConfigConst.BOOK_IN_CLASS_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOK_IN_CLASS_COLOR);
        RemoteConfigConst.BOOK_WITH_FRIEND_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOK_WITH_FRIEND_COLOR);
        RemoteConfigConst.BOOKED_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOKED_BUTTON);
        RemoteConfigConst.WAITLIST_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.WAITLIST_BUTTON);
        RemoteConfigConst.WAITLISTED_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.WAITLISTED_BUTTON);
        RemoteConfigConst.FULL_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.FULL_BUTTON_COLOR);
        RemoteConfigConst.BOOKED_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BOOKED_BUTTON_COLOR);
        RemoteConfigConst.BUY_CLASS_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BUY_CLASS_BUTTON_COLOR);
        RemoteConfigConst.BUY_CLASS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.BUY_CLASS);
        RemoteConfigConst.CLASS_CARD_TEXT_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.CLASS_CARD_TEXT);
        RemoteConfigConst.RECOMMENDED_FOR_YOU_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.RECOMMENDED_FOR_YOU);
        RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.RECOMMENDED_FOR_YOU_COLOR);
        RemoteConfigConst.SESSION_EXPIRED_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SESSION_EXPIRED);
        RemoteConfigConst.SESSION_EXPIRED_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SESSION_EXPIRED_COLOR);
        RemoteConfigConst.INVITE_FRIENDS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.INVITE_FRIENDS);
        RemoteConfigConst.ADD_TO_WISHLIST_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.ADD_TO_WISHLIST);
        RemoteConfigConst.JOIN_WAITLIST_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.JOIN_WAITLIST);
        RemoteConfigConst.PAYMENT_PACKAGE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PAYMENT_PACKAGE);
        RemoteConfigConst.PAYMENT_PENDING_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PAYMENT_PENDING);
        RemoteConfigConst.PAYMENT_FAILURE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PAYMENT_FAILURE);
        RemoteConfigConst.PAYMENT_CLASS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PAYMENT_CLASS);
        RemoteConfigConst.GYM_VISIT_LIMIT_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.GYM_VISIT_LIMIT_TEXT);
        RemoteConfigConst.SELECT_PLAN_TEXT_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SELECT_PLAN_TEXT);
        RemoteConfigConst.SELECT_PLAN_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SELECT_PLAN_COLOR);
        RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT);
        RemoteConfigConst.MEMBERSHIP_OFFER_COLOR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.MEMBERSHIP_OFFER_COLOR);
        RemoteConfigConst.TESTIMONIALS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.TESTIMONIALS);
        RemoteConfigConst.TESTIMONIALS_RIYADH_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.TESTIMONIALS_RIYADH);
        RemoteConfigConst.FAQ_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.FAQ);
        RemoteConfigConst.PACKAGE_TAGS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PACKAGE_TAGS);
        RemoteConfigConst.WELCOME_P5M_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.WELCOME_P5M);
        RemoteConfigConst.WELCOME_P5M_CONTAIN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.WELCOME_P5M_CONTAIN);
        RemoteConfigConst.SECTION_ONE_TITLE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_TITLE);
        RemoteConfigConst.SECTION_ONE_SUB_ONE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_ONE);
        RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN);
        RemoteConfigConst.SECTION_ONE_SUB_TWO_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_TWO);
        RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN);
        RemoteConfigConst.SECTION_ONE_SUB_THREE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_THREE);
        RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL);
        RemoteConfigConst.SECTION_ONE_SUB_FOUR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_FOUR);
        RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL);
        RemoteConfigConst.SECTION_TWO_TITLE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_TITLE);
        RemoteConfigConst.SECTION_TWO_SUB_ONE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_ONE);
        RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL);
        RemoteConfigConst.SECTION_TWO_SUB_TWO_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_TWO);
        RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL);
        RemoteConfigConst.SECTION_TWO_SUB_THREE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_THREE);
        RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL);
        RemoteConfigConst.SECTION_TWO_SUB_FOUR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_FOUR);
        RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL);
        RemoteConfigConst.PLAN_DESCRIPTION_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PLAN_DESCRIPTION);
        RemoteConfigConst.DROP_IN_COST_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.DROP_IN_COST);
        RemoteConfigConst.SHOW_SELECTION_OPTIONS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SHOW_SELECTION_OPTIONS);
        RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN);
        RemoteConfigConst.ON_BOARDING_DATA_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.ON_BOARDING_DATA);


    }


    public void setArabicConfigValues() {
        RemoteConfigConst.SEARCH_BAR_TEXT_VALUE = context.getString(R.string.search_hint);
        RemoteConfigConst.BOOK_VALUE =context.getString(R.string.book);
        RemoteConfigConst.BOOK_IN_CLASS_VALUE = context.getString(R.string.reserve_class);
        RemoteConfigConst.BOOK_WITH_FRIEND_VALUE =context.getString(R.string.reserve_class_with_friend );
        RemoteConfigConst.BOOKED_VALUE = context.getString(R.string.booked );
        RemoteConfigConst.WAITLIST_VALUE = context.getString(R.string.waitlist );
        RemoteConfigConst.WAITLISTED_VALUE = context.getString(R.string.waitlisted );
        RemoteConfigConst.BUY_CLASS_VALUE = context.getString(R.string. buy_classes);
        RemoteConfigConst.CLASS_CARD_TEXT_VALUE = context.getString(R.string. reserve_class);
        RemoteConfigConst.RECOMMENDED_FOR_YOU_VALUE = context.getString(R.string.recomended_for_you);
        RemoteConfigConst.SESSION_EXPIRED_VALUE = context.getString(R.string.expired );
        RemoteConfigConst.INVITE_FRIENDS_VALUE = context.getString(R.string.share );
        RemoteConfigConst.ADD_TO_WISHLIST_VALUE = context.getString(R.string.add_to_WishList );
        RemoteConfigConst.JOIN_WAITLIST_VALUE = context.getString(R.string.join_waitlist );
        RemoteConfigConst.PAYMENT_PACKAGE_VALUE =context.getString(R.string.book_classes );
        RemoteConfigConst.PAYMENT_PENDING_VALUE = context.getString(R.string.payment_history );
        RemoteConfigConst.PAYMENT_FAILURE_VALUE = context.getString(R.string.payment_history );
        RemoteConfigConst.PAYMENT_CLASS_VALUE = context.getString(R.string. view_schedule);
        RemoteConfigConst.GYM_VISIT_LIMIT_VALUE = context.getString(R.string.view_gym_visit_limits );
        RemoteConfigConst.SELECT_PLAN_TEXT_VALUE = context.getString(R.string.select_plan );
        RemoteConfigConst.GYM_VISIT_LIMIT_DETAIL_TEXT_VALUE = context.getString(R.string.package_limit_header );



        RemoteConfigConst.TESTIMONIALS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.TESTIMONIALS);
        RemoteConfigConst.TESTIMONIALS_RIYADH_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.TESTIMONIALS_RIYADH);
        RemoteConfigConst.FAQ_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.FAQ);
        RemoteConfigConst.PACKAGE_TAGS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PACKAGE_TAGS);
        RemoteConfigConst.WELCOME_P5M_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.WELCOME_P5M);
        RemoteConfigConst.WELCOME_P5M_CONTAIN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.WELCOME_P5M_CONTAIN);
        RemoteConfigConst.SECTION_ONE_TITLE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_TITLE);
        RemoteConfigConst.SECTION_ONE_SUB_ONE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_ONE);
        RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_ONE_CONTAIN);
        RemoteConfigConst.SECTION_ONE_SUB_TWO_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_TWO);
        RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_TWO_CONTAIN);
        RemoteConfigConst.SECTION_ONE_SUB_THREE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_THREE);
        RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_THREE_DETAIL);
        RemoteConfigConst.SECTION_ONE_SUB_FOUR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_FOUR);
        RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_ONE_SUB_FOUR_DETAIL);
        RemoteConfigConst.SECTION_TWO_TITLE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_TITLE);
        RemoteConfigConst.SECTION_TWO_SUB_ONE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_ONE);
        RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_ONE_DETAIL);
        RemoteConfigConst.SECTION_TWO_SUB_TWO_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_TWO);
        RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_TWO_DETAIL);
        RemoteConfigConst.SECTION_TWO_SUB_THREE_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_THREE);
        RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_THREE_DETAIL);
        RemoteConfigConst.SECTION_TWO_SUB_FOUR_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_FOUR);
        RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SECTION_TWO_SUB_FOUR_DETAIL);
        RemoteConfigConst.PLAN_DESCRIPTION_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PLAN_DESCRIPTION);
        RemoteConfigConst.DROP_IN_COST_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.DROP_IN_COST);
        RemoteConfigConst.SHOW_SELECTION_OPTIONS_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.SHOW_SELECTION_OPTIONS);
        RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.PLAN_DESCRIPTION_DROP_IN);
        RemoteConfigConst.ON_BOARDING_DATA_VALUE = mFirebaseRemoteConfig.getString(RemoteConfigConst.ON_BOARDING_DATA);

    }
}