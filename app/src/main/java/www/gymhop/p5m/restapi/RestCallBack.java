package www.gymhop.p5m.restapi;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;

/**
 * Created by MyU10 on 1/21/2017.
 */

public abstract class RestCallBack<T> implements Callback<T> {

    public abstract void onFailure(Call<T> call, String message);

    public abstract void onResponse(Call<T> call, Response<T> restResponse, T response);

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        onFailure(call, t.toString());
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if (response.isSuccessful()) {
            onResponse(call, response, response.body());
        } else {
            Gson gson = new Gson();
            ResponseModel responseModel = null;
            try {
                responseModel = gson.fromJson(response.errorBody().string(), ResponseModel.class);
                onFailure(call, responseModel.errorMessage);
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.exception(e);
                onFailure(call, "");
            }
        }
    }

    public static boolean isSuccessFull(ResponseModel responseModel) {
        if (responseModel.statusCode.equals(AppConstants.ApiParamValue.SUCCESS_RESPONSE_CODE))
            return true;
        else
            return false;
    }
}
