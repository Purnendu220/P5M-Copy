package www.gymhop.p5m.utils;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class AppConstants {

    public static String plural(String word, int number) {
        return number == 1 ? word : word + "s";
    }

    public static class FragmentPosition {

        public static final int REGISTRATION_STEP_NAME = 0;
        public static final int REGISTRATION_STEP_EMAIL = 1;
        public static final int REGISTRATION_STEP_GENDER = 3;
        public static final int REGISTRATION_STEP_PASSWORD = 2;

        public static final int TAB_FIND_CLASS = 0;
        public static final int TAB_TRAINER = 1;
        public static final int TAB_SCHEDULE = 2;
        public static final int TAB_MY_PROFILE = 3;
    }

    public static class Pref {
        public static final String NAME = "p5m_pref_main";
        public static final String LOGIN = "pref_login";
        public static final String CITIES = "pref_cities";
        public static final String ACTIVITIES = "pref_activities";
        public static final String FILTERS = "pref_filters";
        public static final String USER = "pref_user";
        public static final String AUTH_TOKEN = "pref_auth_token";
    }

    public class DataKey {
        public static final String TAB_POSITION_INT = "tab_position_int";
        public static final String TAB_SHOWN_IN_INT = "tab_shown_in";
        public static final String NAVIGATED_FROM_INT = "navigated_from";
        public static final String CLASS_DATE_STRING = "class_date_string";
        public static final String TAB_ACTIVITY_ID_INT = "tab_activity_id";
        public static final String CLASS_OBJECT = "class_object";
        public static final String NATIONALITY_OBJECT = "nationality_object";
        public static final String TRAINER_OBJECT = "trainer_object";
        public static final String PAYMENT_URL_OBJECT = "payment_url_object";
        public static final String GYM_OBJECT = "gym_object";
        public static final String GYM_ID_INT = "gym_id_int";
        public static final String HOME_TAB_POSITION = "home_tab_position";
    }

    public class Url {
//        public static final String BASE_SERVICE_BETA = "http://192.168.0.39:8080/profive-midl/";
        public static final String WEBSITE = "http://www.p5m.me/";
        public static final String BASE_SERVICE_BETA = "http://qa-api.profive.co/profive-midl/";
        public static final String BASE_SERVICE_ALPHA = "http://qa-api.profive.co/dev-midl/";
        public static final String BASE_SERVICE_LIVE = "http://api.p5m.me/profive-midl/";

        public static final String LOGIN = "api/v1/user/login";
        public static final String LOGOUT = "api/v1/device/logout";
        public static final String REGISTER = "api/v1/user/register";
        public static final String VALIDATE_EMAIL = "api/v1/user/validate";

        public static final String ALL_CITY = "api/v1/country/getAllCity";
        public static final String ALL_CLASS_CATEGORY = "api/v1/class/getAllClassCategory";

        public static final String USER = "api/v1/user/find";

        public static final String CLASS_LIST = "api/v1/class/getClassList";
        public static final String TRAINER_LIST = "api/v1/user/getTrainerList";
        public static final String WISH_LIST = "api/v1/wish/getWishList";
        public static final String SCHEDULE_LIST = "api/v1/user/upcomingClasses";
        public static final String UPCOMING_CLASSES = "api/v1/class/upcomingClasses";
        public static final String FINISHED_CLASS_LIST = "api/v1/user/finishedClasses";
        public static final String FAV_TRAINER_LIST = "api/v1/follow/getFavList";

        public static final String PACKAGE_LIST = "api/v1/package";
        public static final String CLASS_PACKAGE_LIST = "api/v1/package";

        public static final String PACKAGE_LIMITS = "api/v1/gym/mapping/";

        public static final String PACKAGE_PURCHASE = "api/v1/payment/package-purchase/";

        public static final String JOIN_CLASS = "api/v1/user/class/join";
        public static final String TRANSACTIONS = "api/v1/payment";
        public static final String USER_UPDATE = "api/v1/user/update";
        public static final String DEVICE_SAVE = "api/v1/device/save";
        public static final String UPDATE_PASS = "api/v1/user/updatepassword";
    }

    public class ApiParamValue {
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
        public static final String USER_AGENT_ANDROID = "android";
        public static final String NO_TOKEN = "no-token";

        public static final String FOLLOW_TYPE_FOLLOWED = "Followed";
        public static final String EMAIL = "email";

        public static final String PACKAGE_TYPE_GENERAL = "GENERAL";
        public static final String PACKAGE_TYPE_DROP_IN = "DROPIN";

        public static final String GENDER_MALE = "MALE";
        public static final String GENDER_FEMALE = "FEMALE";
        public static final String GENDER_BOTH = "MIXED";
    }

    public class ApiParamKey {
        public static final String MYU_AUTH_TOKEN = "Pro-Auth-Token";
        public static final String USER_AGENT = "userAgent";
        public static final String APP_VERSION = "";
        public static final String SIZE = "size";
        public static final String PAGE = "page";
        public static final String ID = "id";
        public static final String CATEGORY_ID = "categoryId";
        public static final String USER_ID = "userId";
        public static final String PACKAGE_TYPE = "packageType";
        public static final String EMAIL = "email";
        public static final String VALUE = "value";
        public static final String TYPE = "type";
        public static final String GYM_ID = "gymId";
        public static final String TRAINER_ID = "trainerId";
        public static final String PACKAGE_ID = "packageId";
        public static final String FAV_TRAINER_NOTIFICATION = "favTrainerNotification";
        public static final String PROMO_ID = "promoId";
        public static final String SESSION_ID = "sessionId";
    }

    public class AppNavigation {
        public static final int SHOWN_IN_SCHEDULE_UPCOMING = 1;
        public static final int SHOWN_IN_SCHEDULE_WISH_LIST = 2;
        public static final int SHOWN_IN_MY_PROFILE_FINISHED = 3;
        public static final int SHOWN_IN_SEARCH = 4;
        public static final int SHOWN_IN_TRAINER = 5;
        public static final int SHOWN_IN_HOME_FIND_CLASSES = 6;
        public static final int SHOWN_IN_HOME_TRAINERS = 7;
        public static final int SHOWN_IN_MY_PROFILE_FAV_TRAINERS = 8;

        public static final int SHOWN_IN_CLASS_PROFILE = 9;
        public static final int SHOWN_IN_TRAINER_PROFILE = 10;
        public static final int SHOWN_IN_GYM_PROFILE = 11;

        public static final int NAVIGATION_FROM_RESERVE_CLASS = 51;
        public static final int NAVIGATION_FROM_MY_PROFILE = 52;
        public static final int NAVIGATION_FROM_SETTING = 53;
        public static final int NAVIGATION_FROM_CONTINUE_USER = 54;
    }

    public class Limit {
        public static final int PAGE_LIMIT_MAIN_CLASS_LIST = 2;
        public static final int PAGE_LIMIT_INNER_CLASS_LIST = 3;
        public static final int PAGE_LIMIT_MAIN_TRAINER_LIST = 8;
        public static final int PAGE_LIMIT_INNER_TRAINER_LIST = 8;

        public static final int PACKAGE_LIMIT_SCREEN_TABS = 5;
    }

    public class ResultCode {
        public static final int PAYMENT_SUCCESS = 1001;
        public static final int CHOOSE_NATIONALITY = 1002;
        public static final int CHOOSE_LOCATION = 1003;

    }
}
