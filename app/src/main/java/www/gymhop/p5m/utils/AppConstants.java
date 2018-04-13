package www.gymhop.p5m.utils;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class AppConstants {

    public static String plural(String word, int number) {
        if (number == 1) {
            return word;
        } else {
            return word + "s";
        }
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
        public static final String TAB_SHOWN_IN = "tab_shown_in";
        public static final String CLASS_DATE_STRING = "class_date_string";
        public static final String TAB_ACTIVITY_ID = "tab_activity_id";
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
    }

    public class ApiParamValue {
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
    }

    public class ApiParamKey {
        public static final String MYU_AUTH_TOKEN = "Pro-Auth-Token";
        public static final String APP_VERSION = "";
        public static final String SIZE = "size";
        public static final String PAGE = "page";
        public static final String CATEGORY_ID = "categoryId";
        public static final String USER_ID = "userId";
    }

    public class AppNavigation {
        public static final int SHOWN_IN_SCHEDULE = 1;
        public static final int SHOWN_IN_WISH_LIST = 2;
        public static final int SHOWN_IN_MY_PROFILE_FINISHED = 3;
        public static final int SHOWN_IN_SEARCH = 4;
        public static final int SHOWN_IN_TRAINER = 5;
        public static final int SHOWN_IN_FIND_CLASS = 6;
    }

    public class Limit {
        public static final int PAGE_LIMIT_MAIN_CLASS_LIST = 5;
        public static final int PAGE_LIMIT_INNER_CLASS_LIST = 5;
        public static final int PAGE_LIMIT_MAIN_TRAINER_LIST = 25;
        public static final int PAGE_LIMIT_INNER_TRAINER_LIST = 25;
    }
}
