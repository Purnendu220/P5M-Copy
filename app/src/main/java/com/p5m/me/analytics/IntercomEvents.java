package com.p5m.me.analytics;

import com.p5m.me.data.PaymentConfirmationResponse;
import com.p5m.me.data.ValidityPackageList;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.data.request.ClassRatingRequest;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.Intercom;

public class IntercomEvents {

    public static void purchasedPlan(PaymentConfirmationResponse plan, String coupon, ClassModel model) {
        Timestamp timeStampDate = new Timestamp(Calendar.getInstance().getTimeInMillis());

        Map<String, Object> eventData = new HashMap<>();
        eventData.put("plan", plan!=null?plan.getPackageName():"");
        eventData.put("coupon", coupon!=null?coupon:"");
        eventData.put("purchase_date", timeStampDate);
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

    public static void purchase_drop_in(ClassModel classModel) {
        if(classModel!=null) {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("gym_name", classModel.getGymBranchDetail() != null ? classModel.getGymBranchDetail().getGymName() : "");
            Intercom.client().logEvent("Purchase_Drop_In", eventData);
        }
    }

    public static void trackUnJoinClass(ClassModel model) {
        if (model != null) {
            String hourDiff = DateUtils.findDifference(model.getClassDate() + " " + model.getFromTime(), Calendar.getInstance().getTime());
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("activity", model.getClassCategory());
            eventData.put("class_date", DateUtils.getTimespanDate(model.getClassDate() + " " + model.getFromTime()));
            eventData.put("gym_name", model.getGymBranchDetail().getGymName());
            eventData.put("locality", model.getGymBranchDetail().getLocalityName());
            eventData.put("time_difference", hourDiff);
            Intercom.client().logEvent("Cancel_Booking", eventData);
        }
    }

    public static void ratedClass(ClassRatingRequest model, ClassModel classModel) {
        if (model != null && classModel != null) {
            String hourDiff = DateUtils.findDifference(classModel.getClassDate() + " " + classModel.getToTime(), Calendar.getInstance().getTime());
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("rating", model.getmRating());
            eventData.put("class_name", classModel.getTitle());
            eventData.put("class_date", DateUtils.getTimespanDate(classModel.getClassDate() + " " + classModel.getFromTime()));
//            eventData.put("rating_date", DateUtils.getTimespanDate(DateUtils.getCurrentDateandTime()));
            eventData.put("time_difference", hourDiff);
            Intercom.client().logEvent("Rated_Class", eventData);
        }
    }

    public static void trackJoinClass(ClassModel classModel) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("activity", classModel.getClassCategory());
            eventData.put("class_date", DateUtils.getTimespanDate(classModel.getClassDate() + " " + classModel.getFromTime()));
            eventData.put("gym_name", classModel.getGymBranchDetail() == null ? "No Trainer" : classModel.getGymBranchDetail().getGymName());

            String hourDiff = DateUtils.findDifference(classModel.getClassDate() + " " + classModel.getFromTime(), Calendar.getInstance().getTime());
            eventData.put("diffHrs", hourDiff);

            eventData.put("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());
            Intercom.client().logEvent("Join_Class", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackMembershipVisit(int navigatedFrom) {
        try {
            Map<String, Object> eventData = new HashMap<>();

            Intercom.client().logEvent("Visit_Membership", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackCheckoutVisit(String packageName) {
        if (packageName.equalsIgnoreCase(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN) ||
                packageName.equalsIgnoreCase(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN_INTERCOM)
                || packageName.equalsIgnoreCase(AppConstants.Tracker.SPECIAL)) {
        } else {
            try {
                Map<String, Object> eventData = new HashMap<>();
                eventData.put("package_name", packageName);

                Intercom.client().logEvent("Visit_Checkout", eventData);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
            }
        }
    }

    public static void trackExtendedPackage(PaymentConfirmationResponse paymentResponse, ValidityPackageList selectedPacakageFromList,
                                            UserPackage userPackage) {
        try {
            Map<String, Object> eventData = new HashMap<>();
            eventData.put("package_name", userPackage != null ? userPackage.getPackageName() : "");
            eventData.put("week_extended", selectedPacakageFromList != null ? selectedPacakageFromList.getDuration() : "");
            eventData.put("days_left_in_expiry", paymentResponse != null ? DateUtils.getDaysLeftFromPackageExpiryDate(userPackage.getExpiryDate()) : "");

            Intercom.client().logEvent("Extended_Package", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackGymVisit() {
        try {
            Map<String, Object> eventData = new HashMap<>();
            Intercom.client().logEvent("Visit_Gym", eventData);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void attended_class() {
        try {
            Map<String, Object> eventData = new HashMap<>();
            Intercom.client().logEvent("activity", eventData);
            Intercom.client().logEvent("class_date", eventData);
            Intercom.client().logEvent("gym", eventData);
            Intercom.client().logEvent("locality", eventData);
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
