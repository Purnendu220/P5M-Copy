package www.gymhop.p5m.restapi;

import android.content.Context;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.gym_class.ClassModel;
import www.gymhop.p5m.data.request_model.ClassListRequest;
import www.gymhop.p5m.data.request_model.LoginRequest;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.storage.preferences.MyPreferences;
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

        public static final int LOGIN = 100;
        public static final int ALL_CITY = 101;
        public static final int ALL_CLASS_ACTIVITY = 102;

        public static final int CLASS_LIST = 103;
        public static final int TRAINER_LIST = 104;

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

        if (useCache) {

        }

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

        if (useCache) {
//            List<ClassModel> cities = TempStorage.getCities();
//
//            if (cities != null) {
//                requestListener.onApiSuccess(cities, requestCode);
//                return null;
//            }
        }

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

}
