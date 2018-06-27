package com.p5m.me.analytics;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.p5m.me.MyApp;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.User;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun John on 6/18/2018.
 */

public class MixPanel {

    private static HandlerThread handlerThread;
    private static Handler handlerBg;

    public static void setup(Context context) {

        if (handlerThread == null) {
            handlerThread = new HandlerThread("HandlerThreadMixPanel");
            handlerThread.start();
        }

        if (handlerBg == null) {
            handlerBg = new Handler(handlerThread.getLooper());
        }

        if (MyPreferences.getInstance().isLogin()) {
            if (MyApp.mixPanel != null) {
                MyApp.mixPanel.identify(MyApp.mixPanel.getDistinctId());
            }
        }

        try {
            JSONObject props = new JSONObject();
            props.put("Source", "Android");
            MyApp.mixPanel.registerSuperProperties(props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackPastLogin(String pastLogin) {
        try {
            JSONObject props = new JSONObject();
            props.put("pastLogin", pastLogin);

            MyApp.mixPanel.track("Past_Login", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRegister(String origin, User user) {
        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("gender", user.getGender());

            MyApp.mixPanel.track("Registered_User", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackLogin(String origin, User user) {
        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("gender", user.getGender());

            MyApp.mixPanel.track("Login_with", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackEditProfile(String changes) {
        try {
            JSONObject props = new JSONObject();
            props.put("changes", changes);

            MyApp.mixPanel.track("Edit_Profile", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackEditProfileFocus(List<ClassActivity> activities) {
        try {
            for (ClassActivity classActivity : activities) {
                JSONObject props = new JSONObject();
                props.put("ChooseFocus", classActivity.getName());

                MyApp.mixPanel.track("Edit_Profile", props);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackAddFav(int shownInScreen) {

        String origin = "";

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS) {
            origin = AppConstants.Tracker.GYM_PROFILE_TRAINERS;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH) {
            origin = AppConstants.Tracker.SEARCH;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            origin = AppConstants.Tracker.SEARCH_TRAINER;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS) {
            origin = AppConstants.Tracker.TRAINER_TAB;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
            origin = AppConstants.Tracker.USER_PROFILE;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            MyApp.mixPanel.track("Add_Favourite", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRemoveFav(int shownInScreen) {

        String origin = "";

        if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS) {
            origin = AppConstants.Tracker.GYM_PROFILE_TRAINERS;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH) {
            origin = AppConstants.Tracker.SEARCH;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            origin = AppConstants.Tracker.SEARCH_TRAINER;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS) {
            origin = AppConstants.Tracker.TRAINER_TAB;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FINISHED) {
            origin = AppConstants.Tracker.USER_PROFILE;
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            MyApp.mixPanel.track("Remove_From_Favourite", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackAddWishList(String origin, ClassModel classModel) {

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            MyApp.mixPanel.track("Add_to_wishlist", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRemoveWishList(String origin, ClassModel classModel) {

        if (origin.isEmpty()) {
            return;
        }

        float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());

        String diffHrs = "";
        if (hourDiff <= 6) {
            diffHrs = "less than 6 hrs";
        } else if (hourDiff <= 12) {
            diffHrs = "6 to 12 hrs";
        } else if (hourDiff <= 24) {
            diffHrs = "12 to 24 hrs";
        } else {
            diffHrs = "greater than 24 hrs";
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("diffHrs", diffHrs);

            MyApp.mixPanel.track("Remove_from_wishlist", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackClassDetails() {
        try {
            JSONObject props = new JSONObject();
            props.put("view", "Viewed");

            MyApp.mixPanel.track("Class_Details", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackSearch(String searchText) {
        try {
            JSONObject props = new JSONObject();
            props.put("searchText", searchText);

            MyApp.mixPanel.track("Search", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackFilters(final List<ClassesFilter> classesFilters) {

        if (classesFilters.isEmpty()) {
            return;
        }

        handlerBg.post(
                new Runnable() {
                    @Override
                    public void run() {

                        try {
                            List<String> times = new ArrayList<>();
                            List<String> activities = new ArrayList<>();
                            List<String> activityNames = new ArrayList<>();
                            List<String> genders = new ArrayList<>();
                            List<CityLocality> cityLocalities = new ArrayList<>();

                            for (ClassesFilter classesFilter : classesFilters) {
                                if (classesFilter.getObject() instanceof CityLocality) {
                                    cityLocalities.add((CityLocality) classesFilter.getObject());
                                } else if (classesFilter.getObject() instanceof Filter.Time) {
                                    times.add(((Filter.Time) classesFilter.getObject()).getId());
                                } else if (classesFilter.getObject() instanceof Filter.Gender) {
                                    genders.add(((Filter.Gender) classesFilter.getObject()).getId());
                                } else if (classesFilter.getObject() instanceof ClassActivity) {
                                    activities.add(String.valueOf(((ClassActivity) classesFilter.getObject()).getId()));
                                    activityNames.add(String.valueOf(((ClassActivity) classesFilter.getObject()).getName()));
                                }
                            }

                            JSONObject props = new JSONObject();

                            if (!activityNames.isEmpty()) {
                                props.put("using_Activity", activityNames);
                            }
                            if (!times.isEmpty()) {
                                props.put("using_Time", times);
                            }
                            if (!cityLocalities.isEmpty()) {
                                List<String> locations = new ArrayList<>();

                                for (CityLocality cityLocality : cityLocalities) {
                                    locations.add(cityLocality.getName());
                                }

                                props.put("using_Location", locations);
                            }

                            MyApp.mixPanel.track("Filter", props);
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }
                    }
                }
        );
    }

    public static void trackPurchasePlan(String searchText) {
        try {
            JSONObject props = new JSONObject();
            props.put("searchText", searchText);

            MyApp.mixPanel.track("purchase_plan", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackPackagePreferred(String name) {
        try {
            JSONObject props = new JSONObject();
            props.put("package_name", name);

            MyApp.mixPanel.track("Package_Preferred", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackGymVisit(int navigatedFrom) {

        String origin = "";

        if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_SEARCH) {
            origin = AppConstants.Tracker.SEARCH;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            origin = AppConstants.Tracker.SEARCH_GYM;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            origin = AppConstants.Tracker.CLASS_CARD;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE) {
            origin = AppConstants.Tracker.CLASS_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SHARE) {
            origin = AppConstants.Tracker.SHARED_GYM;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            MyApp.mixPanel.track("Visit_Gym_Profile", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackGymProfileEvent(String action) {
        try {
            JSONObject props = new JSONObject();
            props.put("action", action);

            MyApp.mixPanel.track("Event_On_GymProfile", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackTrainerVisit(int navigatedFrom) {

        String origin = "";

        if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_SEARCH) {
            origin = AppConstants.Tracker.SEARCH;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            origin = AppConstants.Tracker.SEARCH_GYM;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            origin = AppConstants.Tracker.CLASS_CARD;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE) {
            origin = AppConstants.Tracker.CLASS_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SHARE) {
            origin = AppConstants.Tracker.SHARED_GYM;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            MyApp.mixPanel.track("Visit_Trainer_Profile", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackTrainerProfileEvent(String action) {
        try {
            JSONObject props = new JSONObject();
            props.put("action", action);

            MyApp.mixPanel.track("Event_On_TrainerProfile", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackNotificationVisit(String origin) {
        try {
            JSONObject props = new JSONObject();
            props.put("Seen", origin);

            MyApp.mixPanel.track("Notification_View", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackMembershipPurchase(String couponCode, String packageName) {
        try {
            JSONObject props = new JSONObject();
            props.put("package_name", packageName);
            props.put("Coupon_Code", couponCode);

            MyApp.mixPanel.track("Membership_Purchase", props);
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
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            MyApp.mixPanel.track("Purchase_Plan", props);
            MyApp.mixPanel.track("Open_Checkout", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackCheckoutVisit(String packageName) {
        try {
            JSONObject props = new JSONObject();
            props.put("package_name", packageName);

            MyApp.mixPanel.track("Open_Checkout", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackSequentialUpdate(String failureReason) {
        try {
            JSONObject props = new JSONObject();
            props.put("failure_reason", failureReason);

            MyApp.mixPanel.track("Sequential tracking", props);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


}
