package www.gymhop.p5m.utils;

/**
 * Created by MyU10 on 3/9/2018.
 */

public class AppConstants {
    public static class Pref {
        public static final String NAME = "p5m_pref";
    }

    public class DataKey {
        public static final String TAB_POSITION_INT = "tab_position_int";
        public static final String CLASS_DATE_STRING = "class_date_string";
    }

    public class Url {
        public static final String BASE_SERVICE_BETA = " http://qa-api.profive.co/";
        public static final String BASE_SERVICE_ALPHA = " http://qa-api.profive.co/";
        public static final String BASE_SERVICE_LIVE = " http://qa-api.profive.co/";

        public static final String ALL_CITY = "dev-midl/api/v1/country/getAllCity";
        public static final String ALL_CLASS_CATEGORY = "dev-midl/api/v1/class/getAllClassCategory";

        public static final String CLASS_LIST = "profive-midl/api/v1/class/getClassList";
        public static final String TRAINER_LIST = "profive-midl/api/v1/class/getTrainerList";
        public static final String USER = "profive-midl/api/v1/user/find";
    }

    public class ApiParamValue {
        public static final String SUCCESS_RESPONSE_CODE = "2XX";
    }

    public class ApiParamKey {
        public static final String MYU_AUTH_TOKEN = "Pro-Auth-Token";
        public static final String APP_VERSION = "";
    }
}
