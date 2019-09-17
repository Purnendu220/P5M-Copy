package com.p5m.me.analytics;

import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.request.ClassRatingRequest;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import java.util.HashMap;
import java.util.Map;
import io.intercom.android.sdk.Intercom;

public class IntercomEvents {

    public static void purchasedPlan(String plan, String coupon, ClassModel model) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("plan", plan);
        eventData.put("coupon", coupon);
//        eventData.put("purchase_date", DateUtils.getPackageClassDate(purchase_date) );
        eventData.put("purchase_date", DateUtils.getTimespanDate(model.getClassDate()+" "+model.getFromTime()) );
        Intercom.client().logEvent("Purchase_Plan", eventData);
    }

    public static void renewedPlan(String new_plan, String old_plan, String renewal_type, String current_purchase_date, String last_purchase_date) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("new_plan", new_plan);
        eventData.put("old_plan", old_plan);
        eventData.put("renewal_type", renewal_type);
        eventData.put("current_purchase_date", current_purchase_date);
        eventData.put("last_purchase_date", last_purchase_date);
        Intercom.client().logEvent("renewed_plan", eventData);
    }

    public static void purchase_drop_in(String gymName, String packageName) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("gym_name", gymName);
        eventData.put("package_name", packageName);
        Intercom.client().logEvent("Purchase_Drop_In", eventData);

    }

    public static void trackUnJoinClass(ClassModel model) {
        float hourDiff = DateUtils.hoursLeft(model.getClassDate() + " " + model.getFromTime());
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("activity", model.getClassCategory());
        eventData.put("time", DateUtils.getTimespanDate(model.getClassDate()+" "+ model.getFromTime()));
        eventData.put("gym_name", model.getGymBranchDetail().getGymName());
        eventData.put("locality", model.getGymBranchDetail().getLocalityName());
        eventData.put("time_difference",  DateUtils.getHourDiff(hourDiff));
        Intercom.client().logEvent("Cancel_Booking", eventData);
    }

    public static void ratedClass(ClassRatingRequest model, ClassModel classModel) {
        Map<String, Object> eventData = new HashMap<>();
        eventData.put("rating", model.getmRating());
        eventData.put("class_name", classModel.getTitle());
        eventData.put("class_time", DateUtils.getTimespanDate(classModel.getClassDate() + " " + classModel.getFromTime()));
        eventData.put("rating_time",System.currentTimeMillis());
        Intercom.client().logEvent("Rated_Class", eventData);
    }

    public static void trackJoinClass(ClassModel classModel) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("activity", classModel.getClassCategory());
            eventData.put("class_timing", DateUtils.getTimespanDate(classModel.getClassDate() + " " + classModel.getFromTime()));
            eventData.put("gym_name", classModel.getGymBranchDetail() == null ? "No Trainer" : classModel.getGymBranchDetail().getGymName());

            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            eventData.put("diffHrs", DateUtils.getHourDiff(hourDiff));

            eventData.put("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());
            Intercom.client().logEvent("Join_Class", eventData);
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
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("origin", origin);

            Intercom.client().logEvent("Visit_Membership", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


    public static void trackCheckoutVisit(String packageName) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("package_name", packageName);

            Intercom.client().logEvent("Visit_Checkout", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackExtendedPackage(String packageName, Integer weekExtended, int daysLeft) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("package_name", packageName);
            eventData.put("week_extended", weekExtended);
            eventData.put("days_left_in_expiry", daysLeft);

            Intercom.client().logEvent("Extended_Package", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackGymVisit(String gymName) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("gym_name", gymName);

            Intercom.client().logEvent("Visit_Gym", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


    public static void trackClassVisit(String className) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("class_name", className);

            Intercom.client().logEvent("Viewed_Class", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

}
