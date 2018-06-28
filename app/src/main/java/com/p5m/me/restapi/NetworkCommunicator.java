package com.p5m.me.restapi;

import android.content.Context;
import android.graphics.Bitmap;

import com.p5m.me.data.City;
import com.p5m.me.data.FollowResponse;
import com.p5m.me.data.MediaResponse;
import com.p5m.me.data.PackageLimitModel;
import com.p5m.me.data.PromoCode;
import com.p5m.me.data.WishListResponse;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.GymDetailModel;
import com.p5m.me.data.main.NotificationModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.PaymentUrl;
import com.p5m.me.data.main.SearchResults;
import com.p5m.me.data.main.TrainerDetailModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.data.main.Transaction;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.ChangePasswordRequest;
import com.p5m.me.data.request.ChooseFocusRequest;
import com.p5m.me.data.request.ClassListRequest;
import com.p5m.me.data.request.DeviceUpdate;
import com.p5m.me.data.request.FollowRequest;
import com.p5m.me.data.request.JoinClassRequest;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.data.request.LogoutRequest;
import com.p5m.me.data.request.PaymentUrlRequest;
import com.p5m.me.data.request.PromoCodeRequest;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.data.request.UserInfoUpdate;
import com.p5m.me.data.request.UserUpdateRequest;
import com.p5m.me.data.request.WishListRequest;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.ToastUtils;

import java.io.File;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.Response;

/**
 * Created by MyU10 on 3/10/2018.
 */

public class NetworkCommunicator {

    public abstract interface RequestListener<T> {

        void onApiSuccess(T response, int requestCode);

        void onApiFailure(String errorMessage, int requestCode);
    }

    public abstract interface RequestListenerRequestDataModel<T> {

        void onApiSuccess(Object response, int requestCode, T requestDataModel);

        void onApiFailure(String errorMessage, int requestCode, T requestDataModel);
    }

    public class RequestCode {

        public static final int FORGOT_PASSWORD = 96;
        public static final int LOGIN_FB = 97;
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
        public static final int LOGOUT = 116;
        public static final int TRANSACTION = 117;
        public static final int UPDATE_USER = 118;
        public static final int DEVICE = 119;
        public static final int ME_USER = 120;
        public static final int CHANGE_PASS = 121;
        public static final int PROMO_CODE = 122;
        public static final int ADD_TO_WISH_LIST = 123;
        public static final int FOLLOW = 124;
        public static final int UN_FOLLOW = 125;
        public static final int NOTIFICATIONS = 126;
        public static final int SEARCH_ALL = 127;
        public static final int PHOTO_UPLOAD = 128;
        public static final int GYM_LIST = 129;
        public static final int CLASS_DETAIL = 130;
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

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator login onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator login onResponse data " + response);
                TempStorage.setAuthToken(restResponse.headers().get(AppConstants.ApiParamKey.MYU_AUTH_TOKEN));
                requestListener.onApiSuccess(response, requestCode);

//                EventBroadcastHelper.sendDeviceUpdate(context);
            }
        });
        return call;
    }

    public Call loginFb(LoginRequest loginRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.LOGIN_FB;
        Call<ResponseModel<User>> call = apiService.loginFB(loginRequest);
        LogUtils.debug("NetworkCommunicator hitting loginFB");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator loginFB onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator loginFB onResponse data " + response);
                TempStorage.setAuthToken(restResponse.headers().get(AppConstants.ApiParamKey.MYU_AUTH_TOKEN));
                requestListener.onApiSuccess(response, requestCode);

//                EventBroadcastHelper.sendDeviceUpdate(context);
            }
        });
        return call;
    }

    public Call register(RegistrationRequest registrationRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.REGISTER;
        Call<ResponseModel<User>> call = apiService.register(registrationRequest);
        LogUtils.debug("NetworkCommunicator hitting register");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator register onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator validateEmail onResponse data " + response);
                TempStorage.setAuthToken(restResponse.headers().get(AppConstants.ApiParamKey.MYU_AUTH_TOKEN));
                requestListener.onApiSuccess(response, requestCode);

//                EventBroadcastHelper.sendDeviceUpdate(context);
            }
        });
        return call;
    }

    public Call forgotPassword(String email, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.FORGOT_PASSWORD;
        Call<ResponseModel<String>> call = apiService.forgotPassword(email);
        LogUtils.debug("NetworkCommunicator hitting forgotPassword");

        call.enqueue(new RestCallBack<ResponseModel<String>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<String>> call, String message) {
                LogUtils.networkError("NetworkCommunicator forgotPassword onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> restResponse, ResponseModel<String> response) {
                LogUtils.networkSuccess("NetworkCommunicator forgotPassword onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call validateEmail(String email, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.VALIDATE_EMAIL;
        Call<ResponseModel> call = apiService.validateEmail(AppConstants.ApiParamValue.EMAIL, email);
        LogUtils.debug("NetworkCommunicator hitting validateEmail");

        call.enqueue(new RestCallBack<ResponseModel>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<List<City>>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<List<ClassActivity>>>(context) {
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

    public Call getDefault() {
//        final int requestCode = RequestCode.ALL_CLASS_ACTIVITY;
        Call<ResponseModel<DefaultSettingServer>> call = apiService.getDefault();
        LogUtils.debug("NetworkCommunicator hitting getDefault");

        call.enqueue(new RestCallBack<ResponseModel<DefaultSettingServer>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<DefaultSettingServer>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getDefault onFailure " + message);
//                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<DefaultSettingServer>> call, Response<ResponseModel<DefaultSettingServer>> restResponse, ResponseModel<DefaultSettingServer> response) {
                LogUtils.networkSuccess("NetworkCommunicator getDefault onResponse data " + response);
//                requestListener.onApiSuccess(response, requestCode);
                TempStorage.setDefault(response.data);
            }
        });
        return call;
    }

    public Call getClassList(ClassListRequest classListRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getClassList(classListRequest);
        LogUtils.debug("NetworkCommunicator hitting getClassList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>(context) {
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

    public Call deviceUpdate(DeviceUpdate deviceUpdate, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.DEVICE;
        Call<ResponseModel<Boolean>> call = apiService.saveDevice(deviceUpdate);
        LogUtils.debug("NetworkCommunicator hitting sendDeviceUpdate");

        call.enqueue(new RestCallBack<ResponseModel<Boolean>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<Boolean>> call, String message) {
                LogUtils.networkError("NetworkCommunicator sendDeviceUpdate onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<Boolean>> call, Response<ResponseModel<Boolean>> restResponse, ResponseModel<Boolean> response) {
                LogUtils.networkSuccess("NetworkCommunicator sendDeviceUpdate onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getTrainerList(int categoryId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRAINER_LIST;
        Call<ResponseModel<List<TrainerModel>>> call = apiService.getTrainerList(categoryId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<TrainerModel>>>(context) {
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

    public Call getSearchTrainerList(String queryString, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRAINER_LIST;
        Call<ResponseModel<List<TrainerModel>>> call = apiService.getSearchedTrainerList(TempStorage.getUser().getId(), queryString, "user", "trainer", page, size);
        LogUtils.debug("NetworkCommunicator hitting getSearchTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<TrainerModel>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<TrainerModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getSearchTrainerList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<TrainerModel>>> call, Response<ResponseModel<List<TrainerModel>>> restResponse, ResponseModel<List<TrainerModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getSearchTrainerList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getSearchGymList(String queryString, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.GYM_LIST;
        Call<ResponseModel<List<GymDetailModel>>> call = apiService.getSearchedGymList(TempStorage.getUser().getId(), queryString, "user", "gym", page, size);
        LogUtils.debug("NetworkCommunicator hitting getSearchTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<GymDetailModel>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<GymDetailModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getSearchTrainerList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<GymDetailModel>>> call, Response<ResponseModel<List<GymDetailModel>>> restResponse, ResponseModel<List<GymDetailModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getSearchTrainerList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getSearchClassList(String queryString, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getSearchedClassesList(TempStorage.getUser().getId(), queryString, "1", page, size);
        LogUtils.debug("NetworkCommunicator hitting getSearchClassList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getSearchClassList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getSearchClassList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getWishList(int userId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getWishList(userId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getWishList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<ClassModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getWishList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<ClassModel>>> call, Response<ResponseModel<List<ClassModel>>> restResponse, ResponseModel<List<ClassModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getWishList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getScheduleList(int userId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_LIST;
        Call<ResponseModel<List<ClassModel>>> call = apiService.getScheduleList(userId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getScheduleList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>(context) {
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
        Call<ResponseModel<List<ClassModel>>> call = apiService.getFinishedClassList(userId);
        LogUtils.debug("NetworkCommunicator hitting getFinishedClassList");

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>(context) {
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
        Call<ResponseModel<List<TrainerModel>>> call = apiService.getFavTrainerList(followType, userId);
        LogUtils.debug("NetworkCommunicator hitting getFavTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<TrainerModel>>>(context) {
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

    public Call getGymTrainerList(int gymId, int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRAINER_LIST;
        Call<ResponseModel<List<TrainerModel>>> call = apiService.getTrainers(gymId, page, size);
        LogUtils.debug("NetworkCommunicator hitting getGymTrainerList");

        call.enqueue(new RestCallBack<ResponseModel<List<TrainerModel>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<TrainerModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getGymTrainerList onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<TrainerModel>>> call, Response<ResponseModel<List<TrainerModel>>> restResponse, ResponseModel<List<TrainerModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getGymTrainerList onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getNotifications(int page, int size, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.NOTIFICATIONS;
        Call<ResponseModel<List<NotificationModel>>> call = apiService.getNotifications(TempStorage.getUser().getId(), page, size);
        LogUtils.debug("NetworkCommunicator hitting getNotifications");

        call.enqueue(new RestCallBack<ResponseModel<List<NotificationModel>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<NotificationModel>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getNotifications onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<NotificationModel>>> call, Response<ResponseModel<List<NotificationModel>>> restResponse, ResponseModel<List<NotificationModel>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getNotifications onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getPackages(int userId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.PACKAGES_FOR_USER;
        Call<ResponseModel<List<Package>>> call = apiService.getPackageList(userId);
        LogUtils.debug("NetworkCommunicator hitting getPackages");

        call.enqueue(new RestCallBack<ResponseModel<List<Package>>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<List<Package>>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<List<PackageLimitModel>>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<PaymentUrl>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<List<ClassModel>>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<TrainerDetailModel>>(context) {
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

    public Call getClassDetail(int sessionId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CLASS_DETAIL;
        Call<ResponseModel<ClassModel>> call = apiService.getClassDetails(TempStorage.getUser().getId(), sessionId);
        LogUtils.debug("NetworkCommunicator hitting getClassDetail");

        call.enqueue(new RestCallBack<ResponseModel<ClassModel>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<ClassModel>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getClassDetail onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<ClassModel>> call, Response<ResponseModel<ClassModel>> restResponse, ResponseModel<ClassModel> response) {
                LogUtils.networkSuccess("NetworkCommunicator getClassDetail onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getMyUser(final RequestListener requestListener, final boolean useCache) {
        final int requestCode = RequestCode.ME_USER;
        Call<ResponseModel<User>> call = apiService.getUser(TempStorage.getUser().getId());
        LogUtils.debug("NetworkCommunicator hitting User");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator User onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator User onResponse data " + response);
                if (response.data != null) {
                    EventBroadcastHelper.sendUserUpdate(context, response.data);
                }
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getGym(int gymId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.GYM;
        Call<ResponseModel<GymDetailModel>> call = apiService.getGym(gymId);
        LogUtils.debug("NetworkCommunicator hitting getGym");

        call.enqueue(new RestCallBack<ResponseModel<GymDetailModel>>(context) {
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

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
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

    public Call logout(LogoutRequest logoutRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.LOGOUT;
        Call<ResponseModel> call = apiService.logout(logoutRequest);
        LogUtils.debug("NetworkCommunicator hitting joinClass");

        call.enqueue(new RestCallBack<ResponseModel>(context) {
            @Override
            public void onFailure(Call<ResponseModel> call, String message) {
                LogUtils.networkError("NetworkCommunicator joinClass onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel> call, Response<ResponseModel> restResponse, ResponseModel response) {
                LogUtils.networkSuccess("NetworkCommunicator joinClass onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call getTransactions(int userId, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.TRANSACTION;
        Call<ResponseModel<List<Transaction>>> call = apiService.getTransactions(userId);
        LogUtils.debug("NetworkCommunicator hitting getTransactions");

        call.enqueue(new RestCallBack<ResponseModel<List<Transaction>>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<List<Transaction>>> call, String message) {
                LogUtils.networkError("NetworkCommunicator getTransactions onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<List<Transaction>>> call, Response<ResponseModel<List<Transaction>>> restResponse, ResponseModel<List<Transaction>> response) {
                LogUtils.networkSuccess("NetworkCommunicator getTransactions onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call updateUser(int userId, UserUpdateRequest userUpdateRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.UPDATE_USER;
        Call<ResponseModel<User>> call = apiService.userUpdate(userId, userUpdateRequest);
        LogUtils.debug("NetworkCommunicator hitting updateUser");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator updateUser onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator updateUser onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call userInfoUpdate(int userId, UserInfoUpdate userInfoUpdate, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.UPDATE_USER;
        Call<ResponseModel<User>> call = apiService.userInfoUpdate(userId, userInfoUpdate);
        LogUtils.debug("NetworkCommunicator hitting updateUser");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator updateUser onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator updateUser onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call updateUserFocus(int userId, ChooseFocusRequest chooseFocusRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.UPDATE_USER;
        Call<ResponseModel<User>> call = apiService.updateFocus(userId, chooseFocusRequest);
        LogUtils.debug("NetworkCommunicator hitting updateUser");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator updateUser onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator updateUser onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call changePass(ChangePasswordRequest changePasswordRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.CHANGE_PASS;
        Call<ResponseModel<String>> call = apiService.changePass(changePasswordRequest);
        LogUtils.debug("NetworkCommunicator hitting changePass");

        call.enqueue(new RestCallBack<ResponseModel<String>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<String>> call, String message) {
                LogUtils.networkError("NetworkCommunicator changePass onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> restResponse, ResponseModel<String> response) {
                LogUtils.networkSuccess("NetworkCommunicator changePass onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call applyPromoCode(PromoCodeRequest promoCodeRequest, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.PROMO_CODE;
        Call<ResponseModel<PromoCode>> call = apiService.applyPromoCode(promoCodeRequest);
        LogUtils.debug("NetworkCommunicator hitting applyPromoCode");

        call.enqueue(new RestCallBack<ResponseModel<PromoCode>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<PromoCode>> call, String message) {
                LogUtils.networkError("NetworkCommunicator applyPromoCode onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<PromoCode>> call, Response<ResponseModel<PromoCode>> restResponse, ResponseModel<PromoCode> response) {
                LogUtils.networkSuccess("NetworkCommunicator applyPromoCode onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call uploadUserImage(Context context, File file, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.PHOTO_UPLOAD;
        LogUtils.debug("NetworkCommunicator hitting uploadImage");

        int mediaId = TempStorage.getUser().getUserProfileImageId();

        try {
            file = new Compressor.Builder(context)
                    .setMaxWidth(AppConstants.Values.IMAGE_RESOLUTION_COMPRESSION)
                    .setMaxHeight(AppConstants.Values.IMAGE_RESOLUTION_COMPRESSION)
                    .setQuality(AppConstants.Values.IMAGE_QUALITY)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(context.getCacheDir().getPath())
                    .build()
                    .compressToFile(file);

        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }


        ProgressRequestBody fileBody = new ProgressRequestBody(file, new ProgressRequestBody.UploadCallbacks() {
            @Override
            public void onProgressUpdate(int percentage) {
            }

            @Override
            public void onError() {
            }

            @Override
            public void onFinish() {
            }
        });

        MultipartBody.Part filePart = MultipartBody.Part.createFormData(AppConstants.ApiParamKey.MEDIA, file.getName(), fileBody);

        Call<ResponseModel<MediaResponse>> call = RestServiceFactory.createService().updateMediaImage(
                filePart,
                mediaId,
                "user",
                TempStorage.getUser().getId(),
                "UserProfile",
                "Image",
                file.getName());

        if (mediaId == 0) {
            call = RestServiceFactory.createService().uploadMediaImage(
                    filePart,
                    "user",
                    TempStorage.getUser().getId(),
                    "UserProfile",
                    "Image",
                    file.getName());

        }

        final File finalFile = file;
        call.enqueue(new RestCallBack<ResponseModel<MediaResponse>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<MediaResponse>> call, String message) {
                LogUtils.networkError("NetworkCommunicator uploadImage onFailure " + message);
                requestListener.onApiFailure(message, requestCode);

                finalFile.delete();
            }

            @Override
            public void onResponse(Call<ResponseModel<MediaResponse>> call, Response<ResponseModel<MediaResponse>> restResponse, ResponseModel<MediaResponse> response) {
                LogUtils.networkSuccess("NetworkCommunicator uploadImage onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);

                finalFile.delete();
            }
        });
        return call;
    }

    public Call followUnFollow(boolean follow, final TrainerModel trainerModel, final RequestListenerRequestDataModel<TrainerModel> requestListener, boolean useCache) {
        int requestCode = 0;
        Call<ResponseModel<FollowResponse>> call = null;

        if (follow) {
            requestCode = RequestCode.FOLLOW;
            call = apiService.follow(new FollowRequest(TempStorage.getUser().getId(), trainerModel.getId()));
        } else {
            requestCode = RequestCode.UN_FOLLOW;
            call = apiService.unFollow(new FollowRequest(TempStorage.getUser().getId(), trainerModel.getId()));
        }

        LogUtils.debug("NetworkCommunicator hitting follow");

        final int finalRequestCode = requestCode;
        call.enqueue(new RestCallBack<ResponseModel<FollowResponse>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<FollowResponse>> call, String message) {
                LogUtils.networkError("NetworkCommunicator follow onFailure " + message);
                requestListener.onApiFailure(message, finalRequestCode, trainerModel);
            }

            @Override
            public void onResponse(Call<ResponseModel<FollowResponse>> call, Response<ResponseModel<FollowResponse>> restResponse, ResponseModel<FollowResponse> response) {
                LogUtils.networkSuccess("NetworkCommunicator follow onResponse data " + response);
                requestListener.onApiSuccess(response, finalRequestCode, trainerModel);
            }
        });
        return call;
    }

    public Call addToWishList(final ClassModel classModel, final int classSessionId) {
        final int requestCode = RequestCode.ADD_TO_WISH_LIST;
        Call<ResponseModel<WishListResponse>> call = apiService.addToWishList(new WishListRequest(TempStorage.getUser().getId(), classSessionId));
        LogUtils.debug("NetworkCommunicator hitting addToWishList");

        call.enqueue(new RestCallBack<ResponseModel<WishListResponse>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<WishListResponse>> call, String message) {
                LogUtils.networkError("NetworkCommunicator addToWishList onFailure " + message);
//                requestListener.onApiFailure(message, requestCode);
                ToastUtils.showLong(context, message);
            }

            @Override
            public void onResponse(Call<ResponseModel<WishListResponse>> call, Response<ResponseModel<WishListResponse>> restResponse, ResponseModel<WishListResponse> response) {
                LogUtils.networkSuccess("NetworkCommunicator addToWishList onResponse data " + response);
//                requestListener.onApiSuccess(response, requestCode);

                try {
                    ToastUtils.show(context, classModel.getTitle() + " successfully added to your wishlist");
                    classModel.setWishListId(((ResponseModel<WishListResponse>) response).data.getId());
                    EventBroadcastHelper.sendWishAdded(classModel);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                }
            }
        });
        return call;
    }

    public Call unJoinClass(final ClassModel classModel, int joinClassId, final RequestListener requestListener) {

        final int requestCode = RequestCode.ADD_TO_WISH_LIST;
        Call<ResponseModel<User>> call = apiService.unJoinClass(joinClassId);
        LogUtils.debug("NetworkCommunicator hitting unJoinClass");

        call.enqueue(new RestCallBack<ResponseModel<User>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<User>> call, String message) {
                LogUtils.networkError("NetworkCommunicator unJoinClass onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<User>> call, Response<ResponseModel<User>> restResponse, ResponseModel<User> response) {
                LogUtils.networkSuccess("NetworkCommunicator unJoinClass onResponse data " + response);
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }

    public Call removeFromWishList(final ClassModel classModel) {

        EventBroadcastHelper.sendWishRemoved(classModel);

        final int requestCode = RequestCode.ADD_TO_WISH_LIST;
        Call<ResponseModel<String>> call = apiService.removeFromWishList(classModel.getWishListId());
        LogUtils.debug("NetworkCommunicator hitting addToWishList");

        call.enqueue(new RestCallBack<ResponseModel<String>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<String>> call, String message) {
                LogUtils.networkError("NetworkCommunicator addToWishList onFailure " + message);
//                requestListener.onApiFailure(message, requestCode);
                ToastUtils.showLong(context, message);
            }

            @Override
            public void onResponse(Call<ResponseModel<String>> call, Response<ResponseModel<String>> restResponse, ResponseModel<String> response) {
                LogUtils.networkSuccess("NetworkCommunicator addToWishList onResponse data " + response);
//                requestListener.onApiSuccess(response, requestCode);
                try {
//                    ToastUtils.show(context, ((ResponseModel<String>) response).data);
                    EventBroadcastHelper.sendWishRemoved(classModel);
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.exception(e);
                }
            }
        });
        return call;
    }

    public Call search(final String queryString, String searchFor, final RequestListener requestListener, boolean useCache) {
        final int requestCode = RequestCode.SEARCH_ALL;
        Call<ResponseModel<SearchResults>> call = apiService.search(TempStorage.getUser().getId(), queryString, searchFor);
        LogUtils.debug("NetworkCommunicator hitting search");

        call.enqueue(new RestCallBack<ResponseModel<SearchResults>>(context) {
            @Override
            public void onFailure(Call<ResponseModel<SearchResults>> call, String message) {
                LogUtils.networkError("NetworkCommunicator search onFailure " + message);
                requestListener.onApiFailure(message, requestCode);
            }

            @Override
            public void onResponse(Call<ResponseModel<SearchResults>> call, Response<ResponseModel<SearchResults>> restResponse, ResponseModel<SearchResults> response) {
                LogUtils.networkSuccess("NetworkCommunicator search onResponse data " + response);
                if (response.data != null) {
                    response.data.searchText = queryString;
                }
                requestListener.onApiSuccess(response, requestCode);
            }
        });
        return call;
    }
}
