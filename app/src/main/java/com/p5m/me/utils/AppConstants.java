package com.p5m.me.utils;

import com.p5m.me.BuildConfig;
import com.p5m.me.fxn.utility.Constants;

import java.util.Locale;

import io.agora.rtc.video.BeautyOptions;
import io.agora.rtc.video.VideoEncoderConfiguration;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class AppConstants {

    public static String plural(String word, int number) {
        if(Constants.LANGUAGE==Locale.ENGLISH)
            return number == 1 ? word : word + "s";
        else
            return word;

    }

    public static String pluralES(String word, int number) {
        if(Constants.LANGUAGE==Locale.ENGLISH)
            return number == 1 ? word : word + "es";
        else
            return word;

    }

    public static class Currency {
        public static final String SAUDI_CURRENCY = "SAR";
        public static final String SAUDI_CURRENCY_SHORT ="SR";

        public static final String KUWAIT_CURRENCY = "KWD";
    }

    public static class Tab {

        public static final int REGISTRATION_STEP_NAME = 0;
        public static final int REGISTRATION_STEP_EMAIL = 1;
        public static final int REGISTRATION_STEP_GENDER = 3;
        public static final int REGISTRATION_STEP_PASSWORD = 2;

        public static final int TAB_EXPLORE_PAGE = 1;
        public static final int TAB_FIND_CLASS = 0;
        public static final int TAB_SCHEDULE = 2;
        public static final int TAB_MY_PROFILE = 4;
        public static final int TAB_MY_PROFILE_FINISHED = 5;
        public static final int TAB_MY_MEMBERSHIP = 3;



        public static final int TAB_MY_SCHEDULE_UPCOMING = 0;
        public static final int TAB_MY_SCHEDULE_WISH_LIST = 1;

        public static final int COUNT_FB_REGISTRATION = 3;
        public static final int COUNT_NORMAL_REGISTRATION = 1;
        public static final int OPEN_ABOUT_US = 100;
        public static final int OPEN_PRIVACY = 101;
        public static final int OPEN_TERMS = 102;



    }

    public static class Values {
        public static final int IMAGE_RESOLUTION_COMPRESSION = 1280;
        public static final int IMAGE_THUMBNAIL_RESOLUTION_COMPRESSION = 256;
        public static final int IMAGE_QUALITY = 90;
        public static final int SPECIAL_CLASS_ID = 1010;
        public static final int UNJOIN_BOTH_CLASS = 1000;
        public static final int UNJOIN_FRIEND_CLASS = 1001;
        public static final int CHANGE_AVAILABLE_SEATS_FOR_MY_CLASS = 1002;
    }

    public static class Tracker {
        public static final String EMAIL = "email";
        public static final String FB = "FB";

        public static final String CONTINUE_AS = "continue_As";
        public static final String SWITCH_ACCOUNT_ACTION = "switchAccountAction";
        public static final String LOGIN_BUTTON = "loginButton";
        public static final String REGISTER = "Register";

        public static final String FIRST_NAME_CHANGED = "First_Name_Changed";
        public static final String LAST_NAME_CHANGED = "Last_Name_Changed";

        public static final String EMAIL_CHANGED = "email_Changed";
        public static final String MOBILE_NUMBER_CHANGED = "mobileNumber_Changed";
        public static final String GENDER_CHANGED = "gender_changed";
        public static final String LOCATION_CHANGED = "location_changed";
        public static final String NATIONALITY_CHANGED = "nationality_changed";
        public static final String DOB_CHANGED = "DOB_changed";
        public static final String PASSWORD_CHANGED = "Password_Changed";
        public static final String PROFILE_IMAGE_CHANGED = "Profile_Image_Changed";

        public static final String GYM_PROFILE_TRAINERS = "GymProfile_Trainers";
        public static final String SEARCH = "Search";
        public static final String SEARCH_TRAINER = SEARCH;
        public static final String SEARCH_GYM = SEARCH;
        public static final String TRAINER_TAB = "Trainer_Tab";
        public static final String TRAINER_PROFILE = "Trainer_Profile";
        public static final String USER_PROFILE = "User_Profile";
        public static final String CLASS_DETAILS = "Class_Details";

        public static final String FIND_CLASS = "Find_Class";
        public static final String VIEW_ALL_RESULTS = SEARCH;
        public static final String GYM_PROFILE = "Gym_Profile";
        public static final String NOTIFICATION = "Notification";
        public static final String SHARED_CLASS = "Shared_Class";
        public static final String SHARED_GYM = "Shared_Gym";
        public static final String SHARED_TRAINER = "Shared_Trainer";
        public static final String PUSH_NOTIFICATION = "Push_Notification";

        public static final String WISH_LIST = "WishList";
        public static final String CLASS_CARD = "Class_Card";
        public static final String CLASS_PROFILE = "Class_Profile";

        public static final String LOOKING_GYM_TRAINERS_LIST = "looking_GymTrainer_List";
        public static final String LOOKING_LOCATION_USING_MAP = "looking_location_using_map";
        public static final String LOOKING_GALLERY = "looking_gallery";
        public static final String VISIT_GYM_PROFILE = "Visit_Gym_Profile";
        public static final String FROM_MY_SCHEDULE = "From_Myschedule";
        public static final String SPECIAL = "SPECIAL";
        public static final String NO_COUPON = "No_Coupon";

        public static final String JOIN_CLASS = "Join_Class";
        public static final String RECHARGE = "Recharge";
        public static final String SETTINGS = "settings";

        public static final String PURCHASE_CANCEL = "purchase_cancel";
        public static final String VIEW_LIMIT_NO_PURCHASE = "view_limit_no_purchase";
        public static final String NO_ACTION = "no_action";
        public static final String UP_COMING = "UpComing";
        public static final String PURCHASE_PLAN = "purchase_plan";
        public static final String EXPLORE = "explore";
        public static final String MAP_VIEW = "map_view";
    }

    public static class Pref {
        public static final String NAME = "p5m_pref_main";
        public static final String LOGIN = "pref_login";
        public static final String FACEBOOK_LOGIN = "fb_pref_login";
        public static final String CITIES = "pref_cities";
        public static final String ACTIVITIES = "pref_activities";
        public static final String FILTERS = "pref_filters";
        public static final String USER = "pref_user";
        public static final String DEFAULT_SETTING_SERVER = "pref_default_setting_server";
        public static final String AUTH_TOKEN = "pref_auth_token";
        public static final String OPEN_MEMBERSHIP_INFO = "open_membership_info";


        public static final String DEVICE_TOKEN = "pref_device_token";
        public static final String NOTIFICATION_COUNT = "pref_notification_count";
        public static final String JOINED_CLASSES = "joined_classes";
        public static final String FINISHED_CLASSES = "finished_classes";

        public static final String CLASS_MODEL = "class_model";
        public static final String RATING_PARAM = "rating_param";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String CALL_START_TIME = "call_start_time";
        public static final String CALL_STOP_TIME = "call_stop_time";


        public static final String PAYMENT_ERROR_RESPONSE = "payment_error_response";

        public static final int MEMBERSHIP_INFO_STATE_HAVE_PACKAGE = 0;
        public static final int MEMBERSHIP_INFO_STATE_NO_PACKAGE = 1;
        public static final int MEMBERSHIP_INFO_STATE_DONE = 2;


        public static final String CLASS_BOOKING_TIME = "Class_booking_time";
        public static final String COUNTRIES = "CountryList";
        public static final String CANCELLATION_REASONS = "CancellationReasonsList";
        public static final String IS_SHOW_MEMBERSHIP_ICON = "IsShowMembershipIcon";
    }

    public class DataKey {
        public static final String TAB_POSITION_INT = "tab_position_int";
        public static final String TAB_SHOWN_IN_INT = "tab_shown_in";
        public static final String NAVIGATED_FROM_INT = "navigated_from";
        public static final String QUERY_STRING = "query_string";
        public static final String CLASS_DATE_STRING = "class_date_string";
        public static final String TAB_ACTIVITY_ID_INT = "tab_activity_id";
        public static final String CLASS_OBJECT = "class_object";
        public static final String CLASS_SESSION_ID_INT = "class_id_int";
        public static final String NATIONALITY_OBJECT = "nationality_object";
        public static final String TRAINER_OBJECT = "trainer_object";
        public static final String PAYMENT_URL_OBJECT = "payment_url_object";
        public static final String GYM_OBJECT = "gym_object";
        public static final String GYM_ID_INT = "gym_id_int";
        public static final String TRAINER_ID_INT = "trainer_id_int";
        public static final String HOME_TAB_POSITION = "home_tab_position";
        public static final String HOME_TABS_INNER_TAB_POSITION = "home_tabs_inner_tab_position";
        public static final String HOME_TABS_PROFILE_INNER_TAB_POSITION = "home_tabs_profile_inner_tab_position";
        public static final String HOME_TABS_SCHEDULE_INNER_TAB_POSITION = "home_tabs_schedule_inner_tab_position";

        public static final String PACKAGE_NAME_STRING = "package_name";
        public static final String IS_FROM_NOTIFICATION_STACK_BUILDER_BOOLEAN = "is_from_notification_stack_builder_boolean";
        public static final String DATA_FROM_NOTIFICATION_STACK = "data_from_notification_stack";

        public static final String RATING_VALUE = "Rating_Value";
        public static final String CLASS_MODEL = "class_model";
        public static final String BOOK_WITH_FRIEND_DATA = "book_with_friend_data";
        public static final String NUMBER_OF_PACKAGES_TO_BUY = "number_of_packages_to_buy";
        public static final String PAGES_TO_OPEN = "pages_to_open";
        public static final String SHOW_DROPIN = "show_drop_in";




        public static final String REFERENCE_ID = "Reference_Id";
        public static final String PAYMENT_OPTION_ID = "payment_option_id";
        public static final String USER_ID ="user_id" ;
    }

    public class Url {
//        public static final String BASE_SERVICE_ALPHA = "http://192.168.0.39:8080/profive-midl/";
//        public static final String BASE_SERVICE_LIVE = "http://192.168.0.18:8080/profive-midl/";

        public static final String WEBSITE = "http://www.p5m.me/";

        public static final String LIVE = "http://api.p5m.me";
       // public static final String QA = "http://qa-api.profive.co";
        public static final String QA = "http://136.243.22.168:8080";

        public static final String BASE_SERVICE_ALPHA = QA + "/profive-midl/";
        public static final String BASE_SERVICE_BETA = QA + "/dev-midl/";
        public static final String BASE_SERVICE_LIVE = LIVE + "/profive-midl/";

        public static final String LOGIN = "api/v1/user/login";
        public static final String LOGOUT = "api/v1/device/logout";
        public static final String REGISTER = "api/v1/user/register";
        public static final String FORGOT_PASSWORD = "api/v1/user/forgotPassword";
        public static final String VALIDATE_EMAIL = "api/v1/user/validate";

        public static final String DEFAULT_SETTING = "api/v1/user/default";

        public static final String ALL_CITY = "api/v1/country/getAllCity";
        public static final String ALL_CLASS_CATEGORY = "api/v1/class/getAllClassCategory";
        public static final String ALL_RATING_PARAMETERS = "api/v1/rating/ratingParameters";


        public static final String USER = "api/v1/user/find";
        public static final String CLASS_DETAILS = "api/v1/class";
        public static final String USER_PACKAGE_DETAIL = "api/v1/user/packageDetail";


        public static final String CLASS_LIST = "api/v1/class/getClassList";
        public static final String BRANCH_LIST = "api/v1/gym/getBranches";
        public static final String RECOMENDED_CLASS_LIST = "api/v1/class/recommendClasses";


        public static final String TRAINER_LIST = "api/v1/user/getTrainerList";
        public static final String GYM_LIST = "api/v1/user/getGymList";
        public static final String NOTIFICATIONS = "api/v1/notification";
        public static final String WISH_LIST = "api/v1/wish/getWishList";
        public static final String SCHEDULE_LIST = "api/v1/user/upcomingClasses";
        public static final String UPCOMING_CLASSES = "api/v1/class/upcomingClasses";
        public static final String FINISHED_CLASS_LIST = "api/v1/user/finishedClasses";
        public static final String FAV_TRAINER_LIST = "api/v1/follow/getFavList";
        public static final String GET_USER_LIST = "api/v1/user/list";


        public static final String PACKAGE_LIST_NEW = "api/v2/package";

        public static final String CLASS_PACKAGE_LIST = "api/v2/package";

        public static final String PACKAGE_LIMITS = "api/v1/gym/mapping/";

        public static final String PACKAGE_PURCHASE = "api/v2/payment/package-purchase/";

        public static final String JOIN_CLASS = "api/v1/user/class/join";
        public static final String UN_JOIN_CLASS = "api/v1/user/class/unjoin";
        public static final String TRANSACTIONS = "api/v1/payment";
        public static final String USER_UPDATE = "api/v1/user/update";
        public static final String DEVICE_SAVE = "api/v1/device/save";
        public static final String UPDATE_PASS = "api/v1/user/updatepassword";
        public static final String PROMO_CODE = "api/v1/promo/apply";
        public static final String SAVE_IN_WISH_LIST = "api/v1/wish/save";
        public static final String FOLLOW = "api/v1/follow/followUser";
        public static final String UN_FOLLOW = "api/v1/follow/unFollowUser";
        public static final String WISH_DELETE = "api/v1/wish/delete";
        public static final String MEDIA_UPDATE = "api/v1/media/mediaUpdate";
        public static final String MEDIA_UPLOAD = "api/v1/media/upload";
        public static final String SEARCH = "api/v1/gym/searchCriteria?";
        public static final String SEARCH_USER_LIST = "api/v1/user/showAllUsers";
        public static final String SEARCH_CLASS_LIST = "api/v1/class/getClasses";
        public static final String CLASS_RATING = "api/v1/rating";
        public static final String CLASS_RATING_PATCH = "api/v1/rating/{id}";
        public static final String CLASS_UNRATED = "api/v1/rating/unRateClasses";
        public static final String DELETE_MEDIA = "api/v1/media/{id}";


        public static final String GET_PAYMENT_DETAIL = "api/v1/payment/package-purchase/{id}";
        public static final String SUPPORT_CONTACT = "api/v1/support/save";
        public static final String GET_EXPLORE_DETAIL = "api/v1/explore/getDetails";

        public static final String GET_STORE_DATA = "api/v1/store/getAllStore";
        public static final String USER_UPDATE_STORE = "api/v1/user/updateUserStore";
        public static final String PAYMENT_INITIATE = "api/v1/payment/initiate";
        public static final String INTERESTED_CITY = "api/v1/interested/city";
        public static final String GET_CANCELLATION_REASON = "api/v1/cancellation/getCancellationReasons";
        public static final String GET_CHANNEL_TOKEN = "api/v1/user/class/token";
        public static final String GET_CHANNEL_USER_STATUS_IN_CHANNEL = "channel/user/property";
        public static final String GET_CHANNEL_USER_COUNT = "channel/user";

    }

    public class ApiParamValue {
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
        public static final String USER_AGENT_ANDROID = "android";
        public static final String NO_TOKEN = "no-token";

        public static final String FOLLOW_TYPE_FOLLOWED = "Followed";
        public static final String EMAIL = "email";

        public static final String PACKAGE_TYPE_GENERAL = "GENERAL";
        public static final String PACKAGE_TYPE_DROP_IN = "DROPIN";
        public static final String PACKAGE_TYPE_DROP_IN_INTERCOM = "DROP IN";

        public static final String GENDER_MALE = "MALE";
        public static final String GENDER_FEMALE = "FEMALE";
        public static final String GENDER_BOTH = "MIXED";
        public static final String PACKAGE_OFFER_PERCENTAGE = "PERCENTAGE";
        public static final String PACKAGE_TYPE_EXTENSION = "EXTENSION";
        public static final String SUCCESS_RESPONSE_STATUS = "true";


    }

    public class ApiParamKey {
        public static final String MYU_AUTH_TOKEN = "Pro-Auth-Token";
        public static final String USER_AGENT = "userAgent";
        public static final String APP_VERSION = "appVersion";
        public static final String SIZE = "size";
        public static final String PAGE = "page";
        public static final String ID = "id";
        public static final String CATEGORY_ID = "categoryId";
        public static final String USER_ID = "userId";
        public static final String PACKAGE_TYPE = "packageType";
        public static final String EMAIL = "email";
        public static final String VALUE = "value";
        public static final String TYPE = "type";
        public static final String USER_CATEGORY_ID = "userCategoryId";
        public static final String GYM_ID = "gymId";
        public static final String TRAINER_ID = "trainerId";
        public static final String PACKAGE_ID = "packageId";
        public static final String FAV_TRAINER_NOTIFICATION = "favTrainerNotification";
        public static final String PROMO_ID = "promoId";
        public static final String SESSION_ID = "sessionId";
        public static final String CLASS_SESSION_ID = "classSessionId";
        public static final String DEVICE_TYPE = "Device-Type";
        public static final String DEVICE_TYPE_VALUE = "android/customer";

        public static final String OBJECT_TYPE = "objectType";
        public static final String OBJECT_DATA_ID = "objectDataId";
        public static final String MEDIA = "media";
        public static final String MEDIA_FOR = "mediaFor";
        public static final String UNIQUE_CHAR = "uniqueChar";
        public static final String MEDIA_TYPE = "mediaType";
        public static final String MEDIA_NAME = "mediaName";
        public static final String QUERY_STRING = "queryString";
        public static final String SEARCH_FOR = "searchFor";
        public static final String EMAIL_ID = "emailId";
        public static final String CLASS_ID = "classId";
        public static final String STATUS_LIST = "statusList";
        public static final String RATING_ID = "ratingId";


        public static final String NUMBER_OF_FRIENDS = "numberOfFriends" ;
        public static final String APP_Language = "language";
        public static final String DATE = "date";
        public static final String LONGITUDE = "longitude";
        public static final String LATITUDE = "latitude";
        public static final String NUMBEROFDAYS = "NUMBEROFDAYS";


        public static final String NUMBEROFCLASS = "NUMBEROFCLASS";
        public static final String PERCENTAGE = "PERCENTAGE";
        public static final String WAITLIST = "WAITLIST";
        public static final String APP_STORE_ID = "Store-Id";
        public static final String STORE_ID = "storeId";
        public static final String CANCELLATION_ID = "cancellationId";
        public static final String REMARK = "remark";
        public static final String AUTHORIZATION = "Authorization";
        public static final String APP_ID = "appid";
        public static final String U_ID = "uid";
        public static final String CHANNEL_NAME = "channelName";

    }

    public static final class Notification {
        public static final String TYPE = "type";
        public static final String MESSAGE = "message";
        public static final String BODY = "body";
        public static final String OBJECT_DATA_ID = "objectDataId";
        public static final String NOTIFICATION_TYPE = "notificationCategory";
        public static final String USER_ID_TO_NOTIFY = "userIdToNotify";
        public static final String URL = "url";
        public static final String PLATFORM = "platform";
        public static final String LINK = "link";


        public static final String CLASS_FROM_TIME = "fromTime";
        public static final String CLASS_TO_TIME = "toTime";
        public static final String CLASS_DATE = "classDate";
        public static final String CLASS_TITLE = "classTitle";



    }

    public class AppNavigation {
        public static final int SHOWN_IN_SCHEDULE_UPCOMING = 1;
        public static final int SHOWN_IN_SCHEDULE_WISH_LIST = 2;
        public static final int SHOWN_IN_MY_PROFILE_FINISHED = 3;
        public static final int SHOWN_IN_SEARCH = 4;
        public static final int SHOWN_IN_TRAINER = 5;
        public static final int SHOWN_IN_HOME_FIND_CLASSES = 6;
        public static final int SHOWN_IN_HOME_TRAINERS = 7;
        public static final int SHOWN_IN_HOME_GYM = 18;
        public static final int SHOWN_IN_MY_PROFILE_FAV_TRAINERS = 8;

        public static final int SHOWN_IN_CLASS_PROFILE = 9;
        public static final int SHOWN_IN_TRAINER_PROFILE = 10;
        public static final int SHOWN_IN_GYM_PROFILE = 11;
        public static final int SHOWN_IN_GYM_PROFILE_TRAINERS = 12;
        public static final int SHOWN_IN_SEARCH_RESULTS = 13;
        public static final int SHOWN_IN_RATING_LIST = 15;
        public static final int SHOWN_IN_MAP_VIEW = 16;
        public static final int SHOWN_IN_HOME_MAP_CLASSES = 17;
        public static final int SHOWN_IN_EXPLORE_PAGE = 18;
        public static final int SHOWN_IN_GYM = 19;

        public static final int NAVIGATION_FROM_RESERVE_CLASS = 51;
        public static final int NAVIGATION_FROM_MY_PROFILE = 52;
        public static final int NAVIGATION_FROM_SETTING = 53;
        public static final int NAVIGATION_FROM_CONTINUE_USER = 54;
        public static final int NAVIGATION_FROM_NOTIFICATION = 55;

        public static final int NAVIGATION_FROM_FIND_CLASS = 56;
        public static final int NAVIGATION_FROM_TRAINERS = 57;
        public static final int NAVIGATION_FROM_SCHEDULE = 58;
        public static final int NAVIGATION_FROM_FB_LOGIN = 59;
        public static final int NAVIGATION_FROM_NOTIFICATION_SCREEN = 60;
        public static final int NAVIGATION_FROM_SHARE = 61;
        public static final int NAVIGATION_FROM_DEEPLINK_ACTIVITY = 62;
        public static final int NAVIGATION_FROM_MEMBERSHIP = 63;
        public static final int NAVIGATION_FROM_SHOW_SCHEDULER = 64;
        public static final int NAVIGATION_FROM_EXPLORE = 65;


        public static final int NAVIGATION_FROM_OTHER_USER = 66;
        public static final int SHOWN_IN_GET_STARTED = 67;
        public static final int NAVIGATION_FROM_FACEBOOK_SIGNUP = 68;
        public static final int NAVIGATION_FROM_FACEBOOK_LOGIN = 69;
    }

    public class Limit {
        public static final int PAGE_LIMIT_MAIN_CLASS_LIST = 15;
        public static final int PAGE_LIMIT_INNER_CLASS_LIST = 20;
        public static final int PAGE_LIMIT_MAIN_TRAINER_LIST = 25;
        public static final int PAGE_LIMIT_INNER_TRAINER_LIST = 25;
        public static final int PAGE_LIMIT_NOTIFICATIONS = 25;
        public static final int PAGE_LIMIT_EXPLORE_PAGE = 4;
        public static final int PAGE_LIMIT_UNLIMITED = 10000;


        public static final int PACKAGE_LIMIT_SCREEN_TABS = 6;
    }

    public class ResultCode {
        public static final int PAYMENT_SUCCESS = 1001;
        public static final int CHOOSE_NATIONALITY = 1002;
        public static final int CHOOSE_LOCATION = 1003;
        public static final int IMAGE_PICKER = 1003;
        public static final int PAYMENT_OPTIONS = 1004;



    }

  public class FitnessLevel {
        public static final String CLASS_LEVEL_BASIC = "BASIC";
        public static final String CLASS_LEVEL_INTERMEDIATE = "INTERMEDIATE";
        public static final String CLASS_LEVEL_ADVANCED = "ADVANCED";
      public static final String CLASS_LEVEL_BASIC_TITLE = "Basic";
      public static final String CLASS_LEVEL_INTERMEDIATE_TITLE = "Intermediate";
      public static final String CLASS_LEVEL_ADVANCED_TITLE = "Advanced";

  }

  public class ImageViewHolderType{
      public static final String CLASS_IMAGE_HOLDER = "CLASS_IMAGE_HOLDER";
      public static final String PROFILE_IMAGE_HOLDER = "PROFILE_IMAGE_HOLDER";
      public static final String GALLERY_IMAGE_HOLDER = "GALLERY_IMAGE_HOLDER";
      public static final String RATING_IMAGE_HOLDER = "RATING_IMAGE_HOLDER";
      public static final String COVER_IMAGE_HOLDER = "COVER_IMAGE_HOLDER";
  }

  public class ExploreViewType{
      public static final String TRAINER_VIEW = "TRAINER_VIEW";
      public static final String GYM_VIEW = "GYM_VIEW";
      public static final String CATEGORY_CAROUSEL_VIEW = "CATEGORY_CAROUSEL_VIEW";

      public static final String BANNER_CAROUSAL_VIEW = "BANNER_CAROUSEL_VIEW";
      public static final String TEXT_WITH_BUTTONS = "P5M_BUTTONS";
      public static final String TEXT_CAROUSAL_VIEW = "TEXT_CAROUSAL_VIEW";
      public static final String PRICE_MODEL_CAROUSEL_VIEW = "PRICE_MODEL_CAROUSEL_VIEW";
      public static final String TRAINER_CAROUSAL_VIEW = "TRAINER_VIEW";
      public static final String TOP_RATED_CLASSES = "TOP_RATED_CLASSES";
      public static final String TEXT_WITH_BUTTONS_2 = "CONFUSED_BUTTONS";


  }

  public class MixPanelValue{
      public static final String MEMBERSHIP = "membership";

      public static final String INTERCOM = "intercom";
      public static final String LIST = "list";
  }

    private static final int BEAUTY_EFFECT_DEFAULT_CONTRAST = BeautyOptions.LIGHTENING_CONTRAST_NORMAL;
    private static final float BEAUTY_EFFECT_DEFAULT_LIGHTNESS = 0.7f;
    private static final float BEAUTY_EFFECT_DEFAULT_SMOOTHNESS = 0.5f;
    private static final float BEAUTY_EFFECT_DEFAULT_REDNESS = 0.1f;

    public static final BeautyOptions DEFAULT_BEAUTY_OPTIONS = new BeautyOptions(
            BEAUTY_EFFECT_DEFAULT_CONTRAST,
            BEAUTY_EFFECT_DEFAULT_LIGHTNESS,
            BEAUTY_EFFECT_DEFAULT_SMOOTHNESS,
            BEAUTY_EFFECT_DEFAULT_REDNESS);

    public static VideoEncoderConfiguration.VideoDimensions[] VIDEO_DIMENSIONS = new VideoEncoderConfiguration.VideoDimensions[]{
            VideoEncoderConfiguration.VD_320x240,
            VideoEncoderConfiguration.VD_480x360,
            VideoEncoderConfiguration.VD_640x360,
            VideoEncoderConfiguration.VD_640x480,
            new VideoEncoderConfiguration.VideoDimensions(960, 540),
            VideoEncoderConfiguration.VD_1280x720
    };

    public static int[] VIDEO_MIRROR_MODES = new int[]{
            io.agora.rtc.Constants.VIDEO_MIRROR_MODE_AUTO,
            io.agora.rtc.Constants.VIDEO_MIRROR_MODE_ENABLED,
            io.agora.rtc.Constants.VIDEO_MIRROR_MODE_DISABLED,
    };

    public static final String PREF_NAME = "io.agora.openlive";
    public static final int DEFAULT_PROFILE_IDX = 2;
    public static final String PREF_RESOLUTION_IDX = "pref_profile_index";
    public static final String PREF_ENABLE_STATS = "pref_enable_stats";
    public static final String PREF_MIRROR_LOCAL = "pref_mirror_local";
    public static final String PREF_MIRROR_REMOTE = "pref_mirror_remote";
    public static final String PREF_MIRROR_ENCODE = "pref_mirror_encode";

    public static final String KEY_CLIENT_ROLE = "key_client_role";
    public static final String KEY_CHANNEL_NAME = "key_channel_name";
    public static final String KEY_CHANNEL_TOKEN = "key_channel_token";
    public static final int TIME_START_DURATION = 10;
    public static final String plainCredentials = BuildConfig.AGORA_APP_CUSTOMER_ID+":"+ BuildConfig.AGORA_APP_CUSTOMER_CERTIFICATE;

    public interface channelType{
        String CHANNEL_YOUTUBE = "YOUTUBE";
        String CHANNEL_FACEBOOK = "FACEBOOK";
        String CHANNEL_INSTAGRAM = "INSTAGRAM";
        String CHANNEL_JITSI ="JITSI";
        String CHANNEL_ZOOM = "ZOOM";
        String CHANNEL_SKYPE = "SKYPE";
        String CHANNEL_GOOGLE = "HANGOUT";
        String CHANNEL_INAPP = "INAPP";

    }

}
