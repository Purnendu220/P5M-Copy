package www.gymhop.p5m.restapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.TrainerDetail;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.gym_class.ClassModel;
import www.gymhop.p5m.data.request_model.ClassListRequest;
import www.gymhop.p5m.data.request_model.LoginRequest;
import www.gymhop.p5m.utils.AppConstants;

public interface ApiService {

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGIN)
    Call<ResponseModel<User>> login(@Body LoginRequest classListRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ALL_CITY)
    Call<ResponseModel<List<City>>> getCityList();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ALL_CLASS_CATEGORY)
    Call<ResponseModel<List<ClassActivity>>> getClassCategoryList();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CLASS_LIST)
    Call<ResponseModel<List<ClassModel>>> getClassList(@Body ClassListRequest classListRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.TRAINER_LIST)
    Call<ResponseModel<List<TrainerDetail>>> getTrainerList();

}
