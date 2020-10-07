package com.p5m.me.analytics;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import com.mixpanel.android.mpmetrics.MixpanelAPI;
import com.p5m.me.BuildConfig;
import com.p5m.me.MyApp;
import com.p5m.me.data.CityLocality;
import com.p5m.me.data.ClassesFilter;
import com.p5m.me.data.Filter;
import com.p5m.me.data.PriceModel;
import com.p5m.me.data.PushDetailModel;
import com.p5m.me.data.UserPackageInfo;
import com.p5m.me.data.main.BranchModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.main.User;
import com.p5m.me.data.main.UserPackage;
import com.p5m.me.helper.Helper;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.DateUtils;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Varun John on 6/18/2018.
 */

public class MixPanel {

    private static HandlerThread handlerThread;
    private static Handler handlerBg;

    private static boolean isSetupDone;
    public static final String MIX_PANEL_TOKEN = BuildConfig.MIXPANEL_KEY;

    public static final String PROJECT_ID = BuildConfig.SENDER_ID;


    public static MixpanelAPI mixPanel;

    public static void setup(Context context) {

        if (handlerThread == null) {
            handlerThread = new HandlerThread("HandlerThreadMixPanel");
            handlerThread.start();
        }

        if (handlerBg == null) {
            handlerBg = new Handler(handlerThread.getLooper());
        }

        if (isSetupDone) {
            return;
        }
        if(MyPreferences.getInstance().isLogin()){
            mixPanel = MixpanelAPI.getInstance(context, MIX_PANEL_TOKEN, false);
            try {
                mixPanel.identify(String.valueOf(TempStorage.getUser().getId()));
                mixPanel.getPeople().identify(String.valueOf(TempStorage.getUser().getId()));

                JSONObject props = new JSONObject();
                props.put("Source", "Android");
                props.put("Location", TempStorage.getCountryName());
                mixPanel.registerSuperProperties(props);
                isSetupDone = true;
                LogUtils.debug("MixPanel setup done");
            } catch (Exception e) {
                isSetupDone = false;
                e.printStackTrace();
                LogUtils.exception(e);
                LogUtils.debug("MixPanel setup error");
            }
        }else{
            mixPanel = MixpanelAPI.getInstance(context, MIX_PANEL_TOKEN, false);

        }

    }

    public static void optInTracking(){
        if(mixPanel!=null){
            mixPanel.optInTracking();
            mixPanel.identify(String.valueOf(TempStorage.getUser().getId()));
            mixPanel.getPeople().identify(String.valueOf(TempStorage.getUser().getId()));
            try{
                JSONObject props = new JSONObject();
                props.put("Source", "Android");
                props.put("Location", TempStorage.getCountryName());
                mixPanel.registerSuperProperties(props);
                isSetupDone = true;
            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    private static void trackEvent(JSONObject props, String eventName) {
        try {
            if (MyApp.USE_MIX_PANEL) {
                mixPanel.track(eventName, props);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }


    public static void login(Context context) {
        mixPanel.alias(String.valueOf(TempStorage.getUser().getId()),null);
    }

    public static void logout() {
        mixPanel.reset();
    }

    public static void flush() {
        try {
            if (MyApp.USE_MIX_PANEL) {
                mixPanel.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private static void trackUser(String id, JSONObject props) {
        try {
            if (MyApp.USE_MIX_PANEL) {
                mixPanel.getPeople().identify(id);
                mixPanel.getPeople().set(props);
                mixPanel.getPeople().initPushHandling(MixPanel.PROJECT_ID);
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

    }

    public static void trackPastLogin(String pastLogin) {
        try {
            JSONObject props = new JSONObject();
            props.put("pastLogin", pastLogin);

            trackEvent(props, "Past_Login");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRegister(String origin, User user) {
        optInTracking();
        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("gender", user.getGender());

            trackEvent(props, "Registered_User");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackLogin(String origin, User user) {
        optInTracking();

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("gender", user.getGender());

            trackEvent(props, "Login_with");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackEditProfile(String changes) {
        try {
            JSONObject props = new JSONObject();
            props.put("changes", changes);

            trackEvent(props, "Edit_Profile");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackBottomSheetChoosePackageButton() {
        try {
            JSONObject props = new JSONObject();
            trackEvent(props, "Select_Option");
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

                trackEvent(props, "Edit_Profile");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRatingImageView() {
        try {
            JSONObject props = new JSONObject();
            props.put("action", "looking_rating_images");
            trackEvent(props, "Event_On_Rating");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackAddFav(int shownInScreen, String name) {

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
            props.put("Trainer_Name", name);

            trackEvent(props, "Add_Favourite");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRemoveFav(int shownInScreen, String name) {

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
        } else if (shownInScreen == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS) {
            origin = AppConstants.Tracker.USER_PROFILE;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("Trainer_Name", name);

            trackEvent(props, "Remove_From_Favourite");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackAddWishList(int shownIn, ClassModel classModel) {

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

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("className", classModel.getTitle());
            props.put("  type", classModel.getAvailableSeat() == 0 ? "Waitlist" : "Wishlist");

            trackEvent(props, "Add_to_wishlist");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackRemoveWishList(String origin, ClassModel classModel) {

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            props.put("diffHrs", DateUtils.getHourDiff(hourDiff));

            trackEvent(props, "Remove_from_wishlist");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }



    public static void trackClassDetailsVisit(int navigationFrom,ClassModel classModel) {
        try {
            User user = TempStorage.getUser();
            String origin = trackOrigin(navigationFrom);
            JSONObject props = new JSONObject();
            props.put("className", classModel.getTitle());
            props.put("classTiming", DateUtils.getDayTiming(classModel.getClassDate() + " " + classModel.getFromTime()));
            props.put("origin", origin);
            props.put("trainerName", classModel.getTrainerDetail() == null ? "No Trainer" : classModel.getTrainerDetail().getFirstName());
            props.put("gymName", classModel.getGymBranchDetail() == null ? "No Gym" : classModel.getGymBranchDetail().getGymName());
            props.put("Classgender", Helper.getClassGenderTextForTracker(classModel.getClassType()));
            props.put("start_time", DateUtils.getHours(classModel.getFromTime()));
            props.put("start_time", DateUtils.getHours(classModel.getFromTime()));
            props.put("booking_time", DateUtils.getCurrentDateandTime());
            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            props.put("diffHrs", DateUtils.getHourDiff(hourDiff));
            props.put("gender", user.getGender());
            props.put("ActivityPrefered", classModel.getClassCategory());
            props.put("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());

            props.put("view", "Viewed");
            props.put("origin", origin);

            trackEvent(props, "Class_Details");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackSearch(String searchText) {
        try {
            JSONObject props = new JSONObject();
            props.put("searchText", searchText);

            trackEvent(props, "Search");
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
                            List<String> gymList = new ArrayList<>();
                            List<String> gymNames = new ArrayList<>();
                            List<String> priceModel = new ArrayList<>();
                            List<String> level = new ArrayList<>();


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
                                } else if (classesFilter.getObject() instanceof GymDataModel) {
                                    gymList.add(String.valueOf(((GymDataModel) classesFilter.getObject()).getId()));
                                    gymNames.add(String.valueOf(((GymDataModel) classesFilter.getObject()).getStudioName()));
                                } else if (classesFilter.getObject() instanceof PriceModel) {
                                    priceModel.add(((PriceModel) classesFilter.getObject()).getName());
                                } else if (classesFilter.getObject() instanceof Filter.FitnessLevel) {
                                    level.add(((Filter.FitnessLevel) classesFilter.getObject()).getName());
                                }
                            }

                            JSONObject props = new JSONObject();

                            if (!activityNames.isEmpty()) {
                                props.put("using_Activity", activityNames);
                            }
                            if (!gymList.isEmpty()) {
                                props.put("using_Gym", gymNames);
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
                            if (!level.isEmpty()) {
                                props.put("using_FitnessLevel", level);
                            }
                            if (!priceModel.isEmpty()) {
                                props.put("using_PriceModel", priceModel);
                            }
                            trackEvent(props, "Filter");
                        } catch (Exception e) {
                            e.printStackTrace();
                            LogUtils.exception(e);
                        }
                    }
                }
        );
    }

    public static void trackPackagePreferred(String name) {
        try {
            JSONObject props = new JSONObject();
            props.put("package_name", name);

            trackEvent(props, "Package_Preferred");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    private static String trackOrigin(int navigatedFrom){
        String origin = "";

        if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_SEARCH) {
            origin = AppConstants.Tracker.SEARCH;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_SEARCH_RESULTS) {
            origin = AppConstants.Tracker.SEARCH_GYM;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            origin = AppConstants.Tracker.CLASS_CARD;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE) {
            origin = AppConstants.Tracker.CLASS_DETAILS;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SHARE) {
            origin = AppConstants.Tracker.SHARED_GYM;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_MAP_VIEW) {
            origin = AppConstants.Tracker.MAP_VIEW;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE) {
            origin = AppConstants.Tracker.EXPLORE;
        }

        if (origin.isEmpty()) {
            return "";
        }
        else return origin;
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
            origin = AppConstants.Tracker.CLASS_DETAILS;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SHARE) {
            origin = AppConstants.Tracker.SHARED_GYM;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_MAP_VIEW) {
            origin = AppConstants.Tracker.MAP_VIEW;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE) {
            origin = AppConstants.Tracker.EXPLORE;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            trackEvent(props, "Visit_Gym_Profile");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackGymProfileEvent(String action) {
        try {
            JSONObject props = new JSONObject();
            props.put("action", action);

            trackEvent(props, "Event_On_GymProfile");
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
            origin = AppConstants.Tracker.SEARCH_TRAINER;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_HOME_FIND_CLASSES) {
            origin = AppConstants.Tracker.CLASS_CARD;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_CLASS_PROFILE) {
            origin = AppConstants.Tracker.CLASS_DETAILS;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_TRAINER_PROFILE) {
            origin = AppConstants.Tracker.TRAINER_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION_SCREEN) {
            origin = AppConstants.Tracker.NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_NOTIFICATION) {
            origin = AppConstants.Tracker.PUSH_NOTIFICATION;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SHARE) {
            origin = AppConstants.Tracker.SHARED_GYM;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_GYM_PROFILE_TRAINERS) {
            origin = AppConstants.Tracker.GYM_PROFILE_TRAINERS;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_HOME_TRAINERS) {
            origin = AppConstants.Tracker.TRAINER_TAB;
        } else if (navigatedFrom == AppConstants.AppNavigation.SHOWN_IN_MY_PROFILE_FAV_TRAINERS) {
            origin = AppConstants.Tracker.USER_PROFILE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE) {
            origin = AppConstants.Tracker.EXPLORE;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            trackEvent(props, "Visit_Trainer_Profile");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackTrainerProfileEvent(String action) {
        try {
            JSONObject props = new JSONObject();
            props.put("action", action);

            trackEvent(props, "Event_On_TrainerProfile");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackNotificationVisit(String origin) {
        try {
            JSONObject props = new JSONObject();
            props.put("Seen", origin);

            trackEvent(props, "Notification_View");
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

            trackEvent(props, "Membership_Purchase");
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
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE) {
            origin = AppConstants.Tracker.EXPLORE;
        }

        if (origin.isEmpty()) {
            return;
        }

        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);

            trackEvent(props, "Purchase_Plan");
            trackEvent(props, "Open_Membership");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackCheckoutVisit(String packageName) {
        try {
            JSONObject props = new JSONObject();
            props.put("package_name", packageName);

            trackEvent(props, "Open_Checkout");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackFindClass(int navigatedFrom, Boolean isList) {
        String origin = "";

        if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE) {
            origin = AppConstants.Tracker.EXPLORE;
        } else if (navigatedFrom == AppConstants.AppNavigation.NAVIGATION_FROM_SCHEDULE) {
            origin = AppConstants.Tracker.FROM_MY_SCHEDULE;
        }

        String viewType = "";

        if (isList)
            viewType = "map";
        else
            viewType = "list";
        try {
            JSONObject props = new JSONObject();
            props.put("origin", origin);
            props.put("view_type", viewType);
            if (!TextUtils.isEmpty(origin))
                trackEvent(props, "OpenFindClass");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackPayButtonClick(String packageName) {
        try {
            JSONObject props = new JSONObject();
            props.put("package_name", packageName);

            trackEvent(props, "Pay_Clicked");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackMapViewClick(String action, BranchModel model) {
        try {
            JSONObject props = new JSONObject();
            props.put("action", action);
            props.put("gym", model.getGymName());

            props.put("gym_locality", model.getLocalityName());

            trackEvent(props, "MapView");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackSequentialUpdate(String failureReason) {
        try {
            JSONObject props = new JSONObject();
            props.put("failure_reason", failureReason);

            trackEvent(props, "Sequential tracking");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackJoinClass(String origin, ClassModel classModel) {
        try {
            User user = TempStorage.getUser();
            JSONObject props = new JSONObject();
            props.put("className", classModel.getTitle());
            props.put("classTiming", DateUtils.getDayTiming(classModel.getClassDate() + " " + classModel.getFromTime()));
            props.put("origin", origin);
            props.put("trainerName", classModel.getTrainerDetail() == null ? "No Trainer" : classModel.getTrainerDetail().getFirstName());
            props.put("gymName", classModel.getGymBranchDetail() == null ? "No Trainer" : classModel.getGymBranchDetail().getGymName());
            props.put("Classgender", Helper.getClassGenderTextForTracker(classModel.getClassType()));
            props.put("start_time", DateUtils.getHours(classModel.getFromTime()));
            props.put("start_time", DateUtils.getHours(classModel.getFromTime()));
            props.put("booking_time", DateUtils.getCurrentDateandTime());

            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            props.put("diffHrs", DateUtils.getHourDiff(hourDiff));
            props.put("userGender", user.getGender());
            if (user.getUserPackageDetailDtoList() != null) {
                String packageUsedForJoinClass = "";
                List<UserPackage> packageList = user.getUserPackageDetailDtoList();
                for (int i = 0; i < packageList.size(); i++) {
                    UserPackage userPackage = packageList.get(0);
                    if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_GENERAL) && userPackage.getBalance() > 0) {
                        packageUsedForJoinClass = userPackage.getPackageName();
                    }
                    if (userPackage.getPackageType().equals(AppConstants.ApiParamValue.PACKAGE_TYPE_DROP_IN) && classModel.getGymBranchDetail().getGymId() == userPackage.getGymId()) {
                        packageUsedForJoinClass = userPackage.getPackageName();
                        break;
                    }
                }
                props.put("packageUsedForJoinClass", packageUsedForJoinClass);
            }
            props.put("ActivityPrefered", classModel.getClassCategory());
            props.put("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());

            trackEvent(props, "Join_Class");
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
        } else if (shownIn == AppConstants.AppNavigation.NAVIGATION_FROM_EXPLORE) {
            origin = AppConstants.Tracker.EXPLORE;
        }

        //  trackJoinClass(origin, classModel);
    }

    public static void trackUnJoinClass(String origin, ClassModel classModel) {
        try {

            JSONObject props = new JSONObject();
            props.put("className", classModel.getTitle());
            props.put("classTiming", DateUtils.getDayTiming(classModel.getClassDate() + " " + classModel.getFromTime()));
            props.put("origin", origin);
            props.put("trainerName", classModel.getTrainerDetail() == null ? "No Trainer" : classModel.getTrainerDetail().getFirstName());
            props.put("gymName", classModel.getGymBranchDetail() == null ? "No Trainer" : classModel.getGymBranchDetail().getGymName());
            props.put("Classgender", Helper.getClassGenderTextForTracker(classModel.getClassType()));
            props.put("cancellation_time", DateUtils.getCurrentDateandTime());

            float hourDiff = DateUtils.hoursLeft(classModel.getClassDate() + " " + classModel.getFromTime());
            props.put("diffHrs", DateUtils.getHourDiff(hourDiff));

            props.put("ActivityPrefered", classModel.getClassCategory());
            props.put("locality_preferred", classModel.getGymBranchDetail() == null ? "" : classModel.getGymBranchDetail().getLocalityName());

            trackEvent(props, "Unjoin");
            trackEvent(props, "Booking Cancel");

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackUserUpdate(User user) {

        try {
            UserPackageInfo userPackageInfo = new UserPackageInfo(user);

            JSONObject props = new JSONObject();
            props.put("id", user.getId() + "");
            props.put("$name", user.getFirstName());
            props.put("$first_name", user.getFirstName());
            props.put("$email", user.getEmail());
            props.put("User Category", user.getUserCategory());
            props.put("Has Active Package", userPackageInfo.havePackages);

            props.put("Gender", user.getGender());

            props.put("dob", user.getDob() == null ?
                    "" : DateUtils.getDateFormatter(new Date(user.getDob())) + "");

            props.put("$phone", user.getMobile());

            props.put("Date of Joining", user.getDateOfJoining() == 0 ?
                    "" : DateUtils.getDateFormatter(new Date(user.getDateOfJoining())) + "");

            props.put("Last Active Date", user.getLastActiveDate() == 0 ?
                    "" : DateUtils.getDateFormatter(new Date(user.getLastActiveDate())) + "");

            props.put("Location", user.getLocation());
            props.put("Nationality", user.getNationality());
            props.put("FacebookId", user.getFacebookId());
            props.put("GoogleId", user.getGoogleId());
            props.put("Category List", MixPanel.getCategoryList(user.getClassCategoryList()));
            props.put("Number of Transactions", user.getNumberOfTransactions() + "");
            props.put("General Package", userPackageInfo.haveGeneralPackage ?
                    userPackageInfo.userPackageGeneral.getPackageName() : "");

            trackUser(String.valueOf(user.getId()), props);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getCategoryList(List<ClassActivity> list) {
        StringBuffer categoryList = new StringBuffer();
        try {
            if (list != null && list.size() > 0) {
                for (ClassActivity object : list) {
                    categoryList.append(object.getClassCategoryName() + ",");
                }
                return categoryList.toString().substring(0, categoryList.toString().length() - 1);
            }

        } catch (Exception e) {

        }
        return categoryList.toString();
    }

    public static void trackPushNotificationClick(PushDetailModel pushDetailModel) {
        try {
            if (pushDetailModel != null) {
                JSONObject props = new JSONObject();
                if (!pushDetailModel.getType().isEmpty())
                    props.put("Type", pushDetailModel.getType());
                if (!pushDetailModel.getMessage().isEmpty())
                    props.put("Message", pushDetailModel.getMessage());
                props.put("Source", pushDetailModel.getSource());
                if (!pushDetailModel.getUrl().isEmpty())
                    props.put("Url", pushDetailModel.getUrl());
                trackEvent(props, "Push_Click");
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackGymVisitLimitView(String packageName) {
        try {
            JSONObject props = new JSONObject();
            props.put("view_limit", packageName);

            trackEvent(props, "Open_Membership");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackExplore(String mixPannelSection, String mixPannelValue) {
        try {
            JSONObject props = new JSONObject();
            props.put("section", mixPannelSection);
            props.put("values", mixPannelValue);

            trackEvent(props, "Explore");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public static void trackExploreVisit() {
        try {
            JSONObject props = new JSONObject();

            trackEvent(props, "Explore");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public static void trackScheduleVisit() {
        try {
            JSONObject props = new JSONObject();

            trackEvent(props, "Schedule");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public static void trackProfileVisit() {
        try {
            JSONObject props = new JSONObject();

            trackEvent(props, "Profile");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }

    public static void trackLearnAboutCredits() {
        try {
            JSONObject props = new JSONObject();

            trackEvent(props, "LearnAboutCredits");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public static void trackMembershipInfoVisit() {
        try {
            JSONObject props = new JSONObject();

            trackEvent(props, "MembershipInfoVisit");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
    public static void trackFindClassVisit() {
        try {
            JSONObject props = new JSONObject();

            trackEvent(props, "OpenFindClass");
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }
    }
}

