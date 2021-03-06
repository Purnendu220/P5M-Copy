package com.p5m.me.restapi;

import android.content.Context;

import com.google.gson.Gson;
import com.p5m.me.eventbus.EventBroadcastHelper;
import com.p5m.me.storage.preferences.MyPreferences;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;
import com.p5m.me.utils.NetworkUtil;
import com.p5m.me.view.activity.Main.ForceUpdateActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    EventBroadcastHelper.logout(context);
                    return;

                    // While joining class..
                } else if (code.equals("498") || code.equals("402") || code.equals("405")) {
                    MyPreferences.getInstance().savePaymentErrorResponse(responseModel);
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



    private boolean isSuccessFull(T t) {
        if (t instanceof ResponseModel) {
            ResponseModel responseModel = (ResponseModel) t;
            boolean isSuccessFull = (responseModel.statusCode!=null&&responseModel.statusCode.equals(AppConstants.ApiParamValue.SUCCESS_RESPONSE_CODE))
                    ||(responseModel.success!=null&&responseModel.success.equals(AppConstants.ApiParamValue.SUCCESS_RESPONSE_STATUS));
            return isSuccessFull;
        }
        return false;
    }
}
