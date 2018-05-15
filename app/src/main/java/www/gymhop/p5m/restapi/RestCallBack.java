package www.gymhop.p5m.restapi;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gymhop.p5m.MyApp;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.view.activity.Main.ForceUpdateActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

/**
 * Created by MyU10 on 1/21/2017.
 */

public abstract class RestCallBack<T> implements Callback<T> {

    public abstract void onFailure(Call<T> call, String message);

    public abstract void onResponse(Call<T> call, Response<T> restResponse, T response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {

//        String errorMessage = "Please try again later";
//
//        try {
////            switch (MyApp.apiMode) {
////                case LIVE:
////                    if (!NetworkUtil.isInternetAvailable) {
////                        errorMessage = "Internet not available";
////                    } else {
////                        errorMessage = "Please try again later";
////                    }
////                    break;
////                default:
////                    errorMessage = t.toString();
////            }
//
//            if (!NetworkUtil.isInternetAvailable) {
//                errorMessage = "Internet not available";
//            } else {
//                errorMessage = "Please try again later";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            LogUtils.exception(e);
//        }
//
//        onFailure(call, errorMessage);
        onFailure(call, t.toString());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        if (response.isSuccessful()) {
        if (isSuccessFull(response.body())) {
            onResponse(call, response, response.body());
        } else {
            Gson gson = new Gson();

            try {
                ResponseModel responseModel = gson.fromJson(response.errorBody().string(), ResponseModel.class);

                // Unauthorized User..
                if (responseModel.statusCode.equals("401")) {
                    EventBroadcastHelper.logout(BaseActivity.contextRef);
                    return;

                    // While joining class..
                } else if (responseModel.statusCode.equals("498") || responseModel.statusCode.equals("402")) {
                    onFailure(call, responseModel.statusCode);

                    // Force Update..
                } else if (responseModel.statusCode.equals("426")) {
                    ForceUpdateActivity.openActivity(MyApp.context, "", responseModel.updateInfoText);

                } else {
                    onFailure(call, responseModel.errorMessage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
                onFailure(call, "");
            }
        }
    }

//        else {
//            try {
//                onFailure(call, response.errorBody().string());
//            } catch (IOException e) {
//                e.printStackTrace();
//                LogUtils.exception(e);
//                onFailure(call, "Unknown");
//            }
//        }
//}

    private boolean isSuccessFull(T t) {
        if (t instanceof ResponseModel) {
            ResponseModel responseModel = (ResponseModel) t;
            if (responseModel.statusCode.equals(AppConstants.ApiParamValue.SUCCESS_RESPONSE_CODE))
                return true;
        }
        return false;
    }
}
