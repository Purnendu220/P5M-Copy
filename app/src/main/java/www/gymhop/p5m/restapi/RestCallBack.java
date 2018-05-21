package www.gymhop.p5m.restapi;

import android.content.Context;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gymhop.p5m.eventbus.EventBroadcastHelper;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;
import www.gymhop.p5m.utils.NetworkUtil;
import www.gymhop.p5m.view.activity.Main.ForceUpdateActivity;
import www.gymhop.p5m.view.activity.base.BaseActivity;

/**
 * Created by MyU10 on 1/21/2017.
 */

public abstract class RestCallBack<T> implements Callback<T> {

    public abstract void onFailure(Call<T> call, String message);

    public abstract void onResponse(Call<T> call, Response<T> restResponse, T response);

    private Context context;

    public RestCallBack(Context context) {
        this.context = context;
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {

        String errorMessage = "Please try again";

        try {
//            switch (MyApp.apiMode) {
//                case LIVE:
//                    if (!NetworkUtil.isInternetAvailable) {
//                        errorMessage = "Internet not available";
//                    } else {
//                        errorMessage = "Please try again later";
//                    }
//                    break;
//                default:
//                    errorMessage = t.toString();
//            }

            if (!NetworkUtil.getInstance(context).isConnected()) {
                errorMessage = "No internet connection";
            } else {
                LogUtils.networkError(t.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.exception(e);
        }

        onFailure(call, errorMessage);
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
//        if (response.isSuccessful()) {
        if (isSuccessFull(response.body())) {
            onResponse(call, response, response.body());
        } else {
            Gson gson = new Gson();

            try {
                ResponseModel responseModel = null;
                String code = "";

                if (response.body() != null && response.body() instanceof ResponseModel) {
                    responseModel = (ResponseModel) response.body();
                    code = responseModel.statusCode;
                } else {
                    code = String.valueOf(response.code());
                    responseModel = gson.fromJson(response.errorBody().string(), ResponseModel.class);
                }

                // Unauthorized User..
                if (code.equals("401")) {
                    EventBroadcastHelper.logout(BaseActivity.contextRef);
                    return;

                    // While joining class..
                } else if (code.equals("498") || code.equals("402")) {
                    onFailure(call, code);

                    // Force Update..
                } else if (code.equals("426")) {
                    ForceUpdateActivity.openActivity(context, "", responseModel.updateInfoText);

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
