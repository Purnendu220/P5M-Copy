package com.p5m.me.analytics;

import android.app.Activity;
import android.os.Bundle;

import com.crashlytics.android.answers.AppMeasurementEventLogger;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.User;
import com.p5m.me.helper.Helper;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;

import org.json.JSONObject;


public class FirebaseAnalysic {
    private static FirebaseAnalytics mFirebaseAnalytics;



    public static void trackMembershipPurchase(String couponCode, String packageName) {
        try {
            Bundle props = new Bundle();
            props.putString("package_name", packageName);
            props.putString("Coupon_Code", couponCode);

            mFirebaseAnalytics.logEvent( "Membership_Purchase",props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public static void trackRegister(String origin, User user) {
        try {
            Bundle props = new Bundle();
            props.putString("origin", origin);
            props.putString("gender", user.getGender());

            mFirebaseAnalytics.logEvent( "Registered_User",props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackMembershipVisit(int navigatedFrom) {

        String origin = "";

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_RESERVE_CLASS) {
            origin = AppConstants.Tracker.JOIN_CLASS;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_MY_PROFILE) {
            origin = AppConstants.Tracker.RECHARGE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SETTING) {
            origin = AppConstants.Tracker.SETTINGS;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_FIND_CLASS) {
            origin = AppConstants.Tracker.FIND_CLASS;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            Bundle props = new Bundle();
            props.putString("origin", origin);

            mFirebaseAnalytics.logEvent( "Open_Membership",props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


    public static void trackCheckoutVisit(String packageName) {
        try {
            Bundle props = new Bundle();
            props.putString("package_name", packageName);

            mFirebaseAnalytics.logEvent( "Open_Checkout",props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


    public static void trackJoinClass(String origin, ClassModel classModel) {
        try {
            Bundle props = new Bundle();
            props.putString("className", classModel.getTitle());
            props.putString("classTiming", DateUtils.getDayTiming(classModel.getClassDate() + " " + classModel.getFromTime()));
            props.putString("origin", origin);
            props.putString("trainerName", classModel.getTrainerDetail() == null ? "No Trainer" : classModel.getTrainerDetail().getFirstName());
            props.putString("gymName", classModel.getGymBranchDetail() == null ? "No Trainer" : classModel.getGymBranchDetail().getGymName());
            props.putString("Classgender", Helper.getClassGenderTextForTracker(classModel.getClassType()));

            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            props.putString("diffHrs", DateUtils.getHourDiff(hourDiff));

            props.putString("ActivityPrefered", classModel.getClassCategory());
            props.putString("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());

            mFirebaseAnalytics.logEvent( "Join_Class",props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackJoinClass(int shownIn, ClassModel classModel) {

        String origin = "";
        if (shownIn == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            origin = AppConstants.Tracker.FIND_CLASS;
        } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SEARCH) {
            origin = AppConstants.Tracker.SEARCH;
        } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            origin = AppConstants.Tracker.VIEW_ALL_RESULTS;
        } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE) {
            origin = AppConstants.Tracker.GYM_PROFILE;
        } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_WISH_LIST) {
            origin = AppConstants.Tracker.WISH_LIST;
        } else if (shownIn == AppConstants.AppNavigation.SHOWN_IN_SCHEDULE_UPCOMING) {
            origin = AppConstants.Tracker.UP_COMING;
        } else if (shownIn == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (shownIn == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (shownIn == AppConstants.AppNavigation.NAVIGATION_FROM_SHARE) {
            origin = AppConstants.Tracker.SHARED_CLASS;
        }

        trackJoinClass(origin, classModel);
    }

    public static void trackUnJoinClass(String origin, ClassModel classModel) {
        try {

            Bundle props = new Bundle();
            props.putString("className", classModel.getTitle());
            props.putString("classTiming", DateUtils.getDayTiming(classModel.getClassDate() + " " + classModel.getFromTime()));
            props.putString("origin", origin);
            props.putString("trainerName", classModel.getTrainerDetail() == null ? "No Trainer" : classModel.getTrainerDetail().getFirstName());
            props.putString("gymName", classModel.getGymBranchDetail() == null ? "No Trainer" : classModel.getGymBranchDetail().getGymName());
            props.putString("Classgender", Helper.getClassGenderTextForTracker(classModel.getClassType()));

            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            props.putString("diffHrs", DateUtils.getHourDiff(hourDiff));

            props.putString("ActivityPrefered", classModel.getClassCategory());
            props.putString("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());

            mFirebaseAnalytics.logEvent( "Unjoin",props);
            mFirebaseAnalytics.logEvent( "Booking_Cancel",props);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


    public static void setup(Activity activity) {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(activity);

    }
}
