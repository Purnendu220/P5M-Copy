package www.gymhop.p5m.restapi;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import www.gymhop.p5m.data.City;
import www.gymhop.p5m.data.ClassActivity;
import www.gymhop.p5m.data.Package;
import www.gymhop.p5m.data.PackageLimitModel;
import www.gymhop.p5m.data.User;
import www.gymhop.p5m.data.UserPackage;
import www.gymhop.p5m.data.gym_class.ClassModel;
import www.gymhop.p5m.data.gym_class.TrainerModel;
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
    @GET(AppConstants.Url.TRAINER_LIST)
    Call<ResponseModel<List<TrainerModel>>> getTrainerList(@Query(AppConstants.ApiParamKey.CATEGORY_ID) int categoryId,
                                                           @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                           @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.WISH_LIST + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<List<ClassModel>>> getWishList(@Path(AppConstants.ApiParamKey.USER_ID) int userId,
                                                      @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                      @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SCHEDULE_LIST)
    Call<ResponseModel<List<ClassModel>>> getScheduleList(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                          @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                          @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.FINISHED_CLASS_LIST + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<List<ClassModel>>> getFinishedClassList(@Path(AppConstants.ApiParamKey.USER_ID) int userId,
                                                               @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                               @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.FAV_TRAINER_LIST)
    Call<ResponseModel<List<TrainerModel>>> getFavTrainerList(@Query(AppConstants.ApiParamKey.TYPE) String followType,
                                                              @Query(AppConstants.ApiParamKey.ID) int userId,
                                                              @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                              @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PACKAGE_LIST)
    Call<ResponseModel<List<UserPackage>>> getPackageList(@Query(AppConstants.ApiParamKey.USER_ID) int userId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CLASS_PACKAGE_LIST)
    Call<ResponseModel<List<Package>>> getClassPackageList(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                           @Query(AppConstants.ApiParamKey.GYM_ID) int gymId);


    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PACKAGE_LIMITS)
    Call<ResponseModel<List<PackageLimitModel>>> getPackageLimitList(@Query(AppConstants.ApiParamKey.PACKAGE_TYPE) String packageType);

}
