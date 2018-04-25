package www.gymhop.p5m.restapi;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.Package;
import www.gymhop.p5m.data.PackageLimitModel;
import www.gymhop.p5m.data.PaymentUrl;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.main.ClassModel;
import www.gymhop.p5m.data.main.TrainerDetailModel;
import www.gymhop.p5m.data.main.TrainerModel;
import www.gymhop.p5m.data.request.ClassListRequest;
import www.gymhop.p5m.data.request.JoinClassRequest;
import www.gymhop.p5m.data.request.LoginRequest;
import www.gymhop.p5m.data.request.PaymentUrlRequest;
import www.gymhop.p5m.data.request.RegistrationRequest;
import www.gymhop.p5m.data.temp.GymDetailModel;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class NetworkCommunicator {


    public abstract interface RequestListener<T> {

        void onApiSuccess(T response, int requestCode);

        void onApiFailure(String errorMessage, int requestCode);
    }

    public class RequestCode {

        public static final int VALIDATE_EMAIL = 98;
        public static final int REGISTER = 99;
        public static final int LOGIN = 100;
        public static final int ALL_CITY = 101;
        public static final int ALL_CLASS_ACTIVITY = 102;

        public static final int CLASS_LIST = 103;
        public static final int TRAINER_LIST = 104;

        public static final int WISH_LIST = 105;
        public static final int SCHEDULE_LIST = 106;
        public static final int FAV_TRAINER_LIST = 107;
        public static final int FINISHED_CLASS_LIST = 108;

        public static final int PACKAGES_FOR_USER = 109;
        public static final int PACKAGES_LIMIT = 110;

        public static final int BUY_PACKAGE = 111;
        public static final int UPCOMING_CLASSES = 112;
        public static final int TRAINER = 113;
        public static final int GYM = 114;
        public static final int JOIN_CLASS = 115;
    }

    private Context context;
    private ApiService apiService;
    private static NetworkCommunicator networkCommunicator;
    private MyPreferences myPreferences;

    private NetworkCommunicator(Context context) {
        this.context = context;
        this.apiService = RestServiceFactory.createService();
        this.myPreferences = MyPreferences.initialize(context);
    }

    public static NetworkCommunicator getInstance(Context context) {
        if (networkCommunicator == null) {
            networkCommunicator = new NetworkCommunicator(context);
        }
        return networkCommunicator;
    }

    public Call login(LoginRequest loginRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.LOGIN;
        Call<ResponseModel<User>> call = apiService.login(loginRequest);
        LogUtils.debug("NetworkCommunicator hitting login");

        call.enqueue(new RestCallBack<ResponseModel<User>>() {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator login onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator login onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);

                TempStorage.setAuthToken(restResponse.headers().get(AppConstants.ApiParamKey.MYU_AUTH_TOKEN));
            }
        });
        return call;
    }

    public Call register(RegistrationRequest registrationRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.REGISTER;
        Call<ResponseModel<User>> call = apiService.register(registrationRequest);
        LogUtils.debug("NetworkCommunicator hitting register");

        call.enqueue(new RestCallBack<ResponseModel<User>>() {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator register onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator validateEmail onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call validateEmail(String email, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.VALIDATE_EMAIL;
        Call<ResponseModel> call = apiService.validateEmail(AppConstants.ApiParamValue.EMAIL, email);
        LogUtils.debug("NetworkCommunicator hitting validateEmail");

        call.enqueue(new RestCallBack<ResponseModel>() {
            @Override
            public void onFailure(Call<ResponseModel> call, String message) {
                LogUtils.networkError("NetworkCommunicator validateEmail onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> restResponse, ResponseModel response) {
                LogUtils.networkSuccess("NetworkCommunicator validateEmail onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getCities(final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.ALL_CITY;
        Call<ResponseModel<List<City>>> call = apiService.getCityList();
        LogUtils.debug("NetworkCommunicator hitting getCities");

        if (useCache) {
            List<City> cities = TempStorage.getCities();

            if (cities != null) {
                ResponseModel<List<City>> responseModel = new ResponseModel<>();
                responseModel.data = cities;
                requestListener.onApiSuccess(responseModel, requestCode);
                return null;
            }
        }

        call.enqueue(new RestCallBack<ResponseModel<List<City>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<City>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getCities onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<City>>> call, Response<ResponseModel<List<City>>> restResponse, ResponseModel<List<City>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getCities onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
                TempStorage.setCities(response.data);
            }
        });
        return call;
    }

    public Call getActivities(final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.ALL_CLASS_ACTIVITY;
        Call<ResponseModel<List<ClassActivity>>> call = apiService.getClassCategoryList();
        LogUtils.debug("NetworkCommunicator hitting getActivities");

        if (useCache) {
            List<ClassActivity> activities = TempStorage.getActivities();

            if (activities != null) {
                ResponseModel<List<ClassActivity>> responseModel = new ResponseModel<>();
                responseModel.data = activities;
                requestListener.onApiSuccess(responseModel, requestCode);
                return null;
            }
        }

        call.enqueue(new RestCallBack<ResponseModel<List<ClassActivity>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassActivity>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getActivities onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassActivity>>> call, Response<ResponseModel<List<ClassActivity>>> restResponse, ResponseModel<List<ClassActivity>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getActivities onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
                TempStorage.setActivities(response.data);
            }
        });
        return call;
    }

    public Call getClassList(ClassListRequest classListRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getClassList(classListRequest);
        LogUtils.debug("NetworkCommunicator hitting getClassList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getClassList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getClassList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getTrainerList(int categoryId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRAINER_LIST;
        Call<ResponseModel<List<TrainerModel>>> call = apiService.getTrainerList(categoryId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<TrainerModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<TrainerModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getTrainerList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<TrainerModel>>> call, Response<ResponseModel<List<TrainerModel>>> restResponse, ResponseModel<List<TrainerModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getTrainerList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getWishList(int userId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getWishList(userId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getTrainerList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getTrainerList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getScheduleList(int userId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getScheduleList(userId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getScheduleList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getScheduleList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getScheduleList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getFinishedClassList(int userId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getFinishedClassList(userId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getFinishedClassList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getFinishedClassList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getFinishedClassList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getFavTrainerList(String followType, int userId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRAINER_LIST;
        Call<ResponseModel<List<TrainerModel>>> call = apiService.getFavTrainerList(followType, userId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getFavTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<TrainerModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<TrainerModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getFavTrainerList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<TrainerModel>>> call, Response<ResponseModel<List<TrainerModel>>> restResponse, ResponseModel<List<TrainerModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getFavTrainerList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getPackages(int userId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.PACKAGES_FOR_USER;
        Call<ResponseModel<List<Package>>> call = apiService.getPackageList(userId);
        LogUtils.debug("NetworkCommunicator hitting getPackages");

        call.enqueue(new RestCallBack<ResponseModel<List<Package>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<Package>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getPackages onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<Package>>> call, Response<ResponseModel<List<Package>>> restResponse, ResponseModel<List<Package>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getPackages onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getPackagesForClass(int userId, int gymId, int sessionId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.PACKAGES_FOR_USER;
        Call<ResponseModel<List<Package>>> call = apiService.getClassPackageList(userId, gymId, sessionId);
        LogUtils.debug("NetworkCommunicator hitting getPackagesForClass");

        call.enqueue(new RestCallBack<ResponseModel<List<Package>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<Package>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getPackagesForClass onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<Package>>> call, Response<ResponseModel<List<Package>>> restResponse, ResponseModel<List<Package>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getPackagesForClass onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getPackagesLimit(String packageType, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.PACKAGES_LIMIT;
        Call<ResponseModel<List<PackageLimitModel>>> call = apiService.getPackageLimitList(packageType);
        LogUtils.debug("NetworkCommunicator hitting getPackagesLimit");

        call.enqueue(new RestCallBack<ResponseModel<List<PackageLimitModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<PackageLimitModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getPackagesLimit onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<PackageLimitModel>>> call, Response<ResponseModel<List<PackageLimitModel>>> restResponse, ResponseModel<List<PackageLimitModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getPackagesLimit onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call purchasePackageForClass(PaymentUrlRequest paymentUrlRequest, final RequestListener requestListener, boolean useCache) {

        final int requestCode = RequestCode.BUY_PACKAGE;
        Call<ResponseModel<PaymentUrl>> call = apiService.purchasePackageForClass(paymentUrlRequest);
        LogUtils.debug("NetworkCommunicator hitting getPackagesLimit");

        call.enqueue(new RestCallBack<ResponseModel<PaymentUrl>>() {
            @Override
            public void onFailure(Call<ResponseModel<PaymentUrl>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getPackagesLimit onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<PaymentUrl>> call, Response<ResponseModel<PaymentUrl>> restResponse, ResponseModel<PaymentUrl> response) {
                LogUtils.networkSuccess("NetworkCommunicator getPackagesLimit onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getUpcomingClasses(int userId, int gymId, int trainerId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.UPCOMING_CLASSES;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getUpcomingClasses(userId, gymId, trainerId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getUpcomingClasses");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>() {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getUpcomingClasses onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getUpcomingClasses onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getTrainer(int trainerId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRAINER;
        Call<ResponseModel<TrainerDetailModel>> call = apiService.getTrainer(trainerId);
        LogUtils.debug("NetworkCommunicator hitting getTrainer");

        call.enqueue(new RestCallBack<ResponseModel<TrainerDetailModel>>() {
            @Override
            public void onFailure(Call<ResponseModel<TrainerDetailModel>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getTrainer onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<TrainerDetailModel>> call, Response<ResponseModel<TrainerDetailModel>> restResponse, ResponseModel<TrainerDetailModel> response) {
                LogUtils.networkSuccess("NetworkCommunicator getTrainer onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getGym(int gymId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.GYM;
        Call<ResponseModel<GymDetailModel>> call = apiService.getGym(gymId);
        LogUtils.debug("NetworkCommunicator hitting getGym");

        call.enqueue(new RestCallBack<ResponseModel<GymDetailModel>>() {
            @Override
            public void onFailure(Call<ResponseModel<GymDetailModel>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getGym onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<GymDetailModel>> call, Response<ResponseModel<GymDetailModel>> restResponse, ResponseModel<GymDetailModel> response) {
                LogUtils.networkSuccess("NetworkCommunicator getGym onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call joinClass(JoinClassRequest joinClassRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.JOIN_CLASS;
        Call<ResponseModel<User>> call = apiService.joinClass(joinClassRequest);
        LogUtils.debug("NetworkCommunicator hitting joinClass");

        call.enqueue(new RestCallBack<ResponseModel<User>>() {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator joinClass onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator joinClass onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

}
