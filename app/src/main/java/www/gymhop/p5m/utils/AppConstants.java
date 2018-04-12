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
        public static final String CLASS_DATE_STRING = "class_date_string";
    }

    public class Url {
        public static final String BASE_SERVICE_BETA = " http://qa-api.profive.co/profive-midl/";
        public static final String BASE_SERVICE_ALPHA = " http://qa-api.profive.co/dev-midl/";
        public static final String BASE_SERVICE_LIVE = " http://api.p5m.me/profive-midl/";

        public static final String LOGIN = "api/v1/user/login";

        public static final String ALL_CITY = "api/v1/country/getAllCity";
        public static final String ALL_CLASS_CATEGORY = "api/v1/class/getAllClassCategory";

        public static final String CLASS_LIST = "api/v1/class/getClassList";
        public static final String TRAINER_LIST = "api/v1/class/getTrainerList";
        public static final String USER = "api/v1/user/find";
    }

    public class ApiParamValue {
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
    }

    public class ApiParamKey {
        public static final String MYU_AUTH_TOKEN = "Pro-Auth-Token";
        public static final String APP_VERSION = "";
    }

    public class Limit {
        public static final int PAGE_LIMIT_MAIN_CLASS_LIST = 10;
    }
}
