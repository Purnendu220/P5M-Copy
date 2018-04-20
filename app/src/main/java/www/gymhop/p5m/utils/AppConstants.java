package www.gymhop.p5m.utils;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class AppConstants {

    public static String plural(String word, int number) {
        return number == 1 ? word : word + "s";
    }

    public static class Pref {
        public static final String NAME = "p5m_pref_main";
        public static final String LOGIN = "pref_login";
        public static final String CITIES = "pref_cities";
        public static final String ACTIVITIES = "pref_activities";
        public static final String FILTERS = "pref_filters";
        public static final String USER = "pref_user";
    }

    public class DataKey {
        public static final String TAB_POSITION_INT = "tab_position_int";
        public static final String TAB_SHOWN_IN_INT = "tab_shown_in";
        public static final String NAVIGATED_FROM_INT = "navigated_from";
        public static final String CLASS_DATE_STRING = "class_date_string";
        public static final String TAB_ACTIVITY_ID_INT = "tab_activity_id";
        public static final String CLASS_OBJECT = "class_object";
        public static final String TRAINER_OBJECT = "trainer_object";
    }

    public class Url {
        public static final String BASE_SERVICE_BETA = " http://qa-api.profive.co/profive-midl/";
        public static final String BASE_SERVICE_ALPHA = " http://qa-api.profive.co/dev-midl/";
        public static final String BASE_SERVICE_LIVE = " http://api.p5m.me/profive-midl/";

        public static final String LOGIN = "api/v1/user/login";

        public static final String ALL_CITY = "api/v1/country/getAllCity";
        public static final String ALL_CLASS_CATEGORY = "api/v1/class/getAllClassCategory";

        public static final String USER = "api/v1/user/find";

        public static final String CLASS_LIST = "api/v1/class/getClassList";
        public static final String TRAINER_LIST = "api/v1/user/getTrainerList";
        public static final String WISH_LIST = "api/v1/wish/getWishList";
        public static final String SCHEDULE_LIST = "api/v1/user/upcomingClasses";
        public static final String FINISHED_CLASS_LIST = "api/v1/user/finishedClasses";
        public static final String FAV_TRAINER_LIST = "api/v1/follow/getFavList";

        public static final String PACKAGE_LIST = "api/v1/package";
        public static final String CLASS_PACKAGE_LIST = "api/v1/package";

        public static final String PACKAGE_LIMITS = "api/v1/gym/mapping/";

        public static final String PACKAGE_PURCHASE = "api/v1/payment/package-purchase/";
    }

    public class ApiParamValue {
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
        public static final String FOLLOW_TYPE_FOLLOWED = "Followed";

        public static final String PACKAGE_TYPE_GENERAL = "GENERAL";
        public static final String PACKAGE_TYPE_DROP_IN = "DROPIN";
    }

    public class ApiParamKey {
        public static final String MYU_AUTH_TOKEN = "Pro-Auth-Token";
        public static final String APP_VERSION = "";
        public static final String SIZE = "size";
        public static final String PAGE = "page";
        public static final String ID = "id";
        public static final String CATEGORY_ID = "categoryId";
        public static final String USER_ID = "userId";
        public static final String PACKAGE_TYPE = "packageType";
        public static final String TYPE = "type";
        public static final String GYM_ID = "gymId";
        public static final String TRAINER_ID = "trainerId";
        public static final String PACKAGE_ID = "packageId";
        public static final String PROMO_ID = "promoId";
        public static final String SESSION_ID = "sessionId";
    }

    public class AppNavigation {
        public static final int SHOWN_IN_SCHEDULE_UPCOMING = 1;
        public static final int SHOWN_IN_WISH_LIST = 2;
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
    }

    public class Limit {
        public static final int PAGE_LIMIT_MAIN_CLASS_LIST = 5;
        public static final int PAGE_LIMIT_INNER_CLASS_LIST = 15;
        public static final int PAGE_LIMIT_MAIN_TRAINER_LIST = 25;
        public static final int PAGE_LIMIT_INNER_TRAINER_LIST = 25;

        public static final int PACKAGE_LIMIT_SCREEN_TABS = 5;
    }
}
