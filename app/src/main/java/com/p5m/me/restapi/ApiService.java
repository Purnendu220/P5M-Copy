package com.p5m.me.restapi;

import com.p5m.me.data.City;
import com.p5m.me.data.ClassRatingUserData;
import com.p5m.me.data.ContactRequestModel;
import com.p5m.me.data.ExploreDataModel;
import com.p5m.me.data.MediaResponse;
import com.p5m.me.data.PackageLimitModel;
import com.p5m.me.data.PaymentConfirmationResponse;
import com.p5m.me.data.PromoCode;
import com.p5m.me.data.RatingParamModel;
import com.p5m.me.data.RatingResponseModel;
import com.p5m.me.data.UnratedClassData;
import com.p5m.me.data.UserPackageDetail;
import com.p5m.me.data.WishListResponse;
import com.p5m.me.data.main.BookingCancellationResponse;
import com.p5m.me.data.main.BranchModel;
import com.p5m.me.data.main.ClassActivity;
import com.p5m.me.data.main.ClassModel;
import com.p5m.me.data.main.GymDataModel;
import com.p5m.me.data.main.GymDetailModel;
import com.p5m.me.data.main.GymModel;
import com.p5m.me.data.main.InterestedCityModel;
import com.p5m.me.data.main.InterestedCityRequestModel;
import com.p5m.me.data.main.Package;
import com.p5m.me.data.main.PaymentInitiateModel;
import com.p5m.me.data.main.ScheduleClassModel;
import com.p5m.me.data.main.StoreApiModel;
import com.p5m.me.data.main.StoreModel;
import com.p5m.me.data.main.TrainerDetailModel;
import com.p5m.me.data.main.TrainerModel;
import com.p5m.me.data.main.Transaction;
import com.p5m.me.data.main.User;
import com.p5m.me.data.request.BranchListRequest;
import com.p5m.me.data.request.ChangePasswordRequest;
import com.p5m.me.data.request.ChooseFocusRequest;
import com.p5m.me.data.request.ClassListRequest;
import com.p5m.me.data.request.ClassRatingRequest;
import com.p5m.me.data.request.DeviceUpdate;
import com.p5m.me.data.request.JoinClassRequest;
import com.p5m.me.data.request.PaymentUrlRequest;
import com.p5m.me.data.request.PromoCodeRequest;
import com.p5m.me.data.request.PublishRequest;
import com.p5m.me.data.request.RegistrationRequest;
import com.p5m.me.data.request.ScheduleRequest;
import com.p5m.me.data.request.UserInfoUpdate;
import com.p5m.me.data.request.WishListRequest;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

import com.p5m.me.data.FollowResponse;
import com.p5m.me.data.main.DefaultSettingServer;
import com.p5m.me.data.main.NotificationModel;
import com.p5m.me.data.main.PaymentUrl;
import com.p5m.me.data.main.SearchResults;
import com.p5m.me.data.request.FollowRequest;
import com.p5m.me.data.request.LoginRequest;
import com.p5m.me.data.request.LogoutRequest;
import com.p5m.me.data.request.UserUpdateRequest;
import com.p5m.me.utils.AppConstants;

public interface ApiService {

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGIN)
    Call<ResponseModel<User>> login(@Body LoginRequest loginRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGIN)
    Call<ResponseModel<User>> loginFB(@Body LoginRequest classListRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.FORGOT_PASSWORD)
    Call<ResponseModel<String>> forgotPassword(@Query(AppConstants.ApiParamKey.EMAIL_ID) String email);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.REGISTER)
    Call<ResponseModel<User>> register(@Body RegistrationRequest registrationRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.LOGOUT)
    Call<ResponseModel> logout(@Body LogoutRequest logoutRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.VALIDATE_EMAIL)
    Call<ResponseModel> validateEmail(@Query(AppConstants.ApiParamKey.TYPE) String type,
                                      @Query(AppConstants.ApiParamKey.VALUE) String value);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ALL_CITY)
    Call<ResponseModel<List<City>>> getCityList();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.DEFAULT_SETTING)
    Call<ResponseModel<DefaultSettingServer>> getDefault();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ALL_CLASS_CATEGORY)
    Call<ResponseModel<List<ClassActivity>>> getClassCategoryList();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.ALL_RATING_PARAMETERS)
    Call<ResponseModel<List<RatingParamModel>>> getClassRatingPArameters();

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GET_USER_LIST + "/{" + AppConstants.ApiParamKey.USER_CATEGORY_ID + "}")
    Call<ResponseModel<List<GymDataModel>>> getGymList(@Path(AppConstants.ApiParamKey.USER_CATEGORY_ID) int userCategoryId);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CLASS_LIST)
    Call<ResponseModel<List<ClassModel>>> getClassList(@Body ClassListRequest classListRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.BRANCH_LIST)
    Call<ResponseModel<List<BranchModel>>> getBranchList(@Body BranchListRequest branchListRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.RECOMENDED_CLASS_LIST)
    Call<ResponseModel<List<ClassModel>>> getRecomendedClassList(@Query(AppConstants.ApiParamKey.DATE) String data,
                                                                 @Query(AppConstants.ApiParamKey.LONGITUDE) Double longitude,
                                                                 @Query(AppConstants.ApiParamKey.LATITUDE) Double latitude,
                                                                 @Query(AppConstants.ApiParamKey.USER_ID) long userId);


    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.TRAINER_LIST)
    Call<ResponseModel<List<TrainerModel>>> getTrainerList(@Query(AppConstants.ApiParamKey.CATEGORY_ID) int categoryId,
                                                           @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                           @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SEARCH_USER_LIST)
    Call<ResponseModel<List<TrainerModel>>> getSearchedTrainerList(@Query(AppConstants.ApiParamKey.ID) int userId,
                                                                   @Query(AppConstants.ApiParamKey.QUERY_STRING) String queryString,
                                                                   @Query(AppConstants.ApiParamKey.SEARCH_FOR) String searchFor,
                                                                   @Query(AppConstants.ApiParamKey.TYPE) String type,
                                                                   @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                                   @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SEARCH_USER_LIST)
    Call<ResponseModel<List<GymDetailModel>>> getSearchedGymList(@Query(AppConstants.ApiParamKey.ID) int userId,
                                                                 @Query(AppConstants.ApiParamKey.QUERY_STRING) String queryString,
                                                                 @Query(AppConstants.ApiParamKey.SEARCH_FOR) String searchFor,
                                                                 @Query(AppConstants.ApiParamKey.TYPE) String type,
                                                                 @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                                 @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SEARCH_CLASS_LIST)
    Call<ResponseModel<List<ClassModel>>> getSearchedClassesList(@Query(AppConstants.ApiParamKey.ID) int userId,
                                                                 @Query(AppConstants.ApiParamKey.QUERY_STRING) String queryString,
                                                                 @Query(AppConstants.ApiParamKey.USER_CATEGORY_ID) String userCategoryId,
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
    @GET(AppConstants.Url.UPCOMING_CLASSES)
    Call<ResponseModel<List<ClassModel>>> getUpcomingClasses(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                             @Query(AppConstants.ApiParamKey.GYM_ID) int gymId,
                                                             @Query(AppConstants.ApiParamKey.TRAINER_ID) int trainerId,
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
                                                              @Query(AppConstants.ApiParamKey.ID) int userId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PACKAGE_LIST_NEW)
    Call<ResponseModel<List<Package>>> getPackageList(@Query(AppConstants.ApiParamKey.USER_ID) int userId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CLASS_PACKAGE_LIST)
    Call<ResponseModel<List<Package>>> getClassPackageList(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                           @Query(AppConstants.ApiParamKey.GYM_ID) int gymId,
                                                           @Query(AppConstants.ApiParamKey.SESSION_ID) int sessionId,
                                                           @Query(AppConstants.ApiParamKey.NUMBER_OF_FRIENDS) int numberOfFriends);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PACKAGE_LIMITS)
    Call<ResponseModel<List<PackageLimitModel>>> getPackageLimitList(
            @Query(AppConstants.ApiParamKey.PACKAGE_TYPE) String packageType);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.TRANSACTIONS)
    Call<ResponseModel<List<Transaction>>> getTransactions(
            @Query(AppConstants.ApiParamKey.USER_ID) int userId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PACKAGE_PURCHASE)
    Call<ResponseModel<List<PackageLimitModel>>> purchasePackageForSelf(
            @Query(AppConstants.ApiParamKey.USER_ID) int userId,
            @Query(AppConstants.ApiParamKey.PACKAGE_ID) int packageId);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.PACKAGE_PURCHASE)
    Call<ResponseModel<PaymentUrl>> purchasePackageForClass(@Body PaymentUrlRequest paymentUrlRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.JOIN_CLASS)
    Call<ResponseModel<User>> joinClass(@Body JoinClassRequest joinClassRequest);

    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.USER_UPDATE + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<User>> userUpdate(@Path(AppConstants.ApiParamKey.USER_ID) int userId,
                                         @Body UserUpdateRequest userUpdateRequest);

    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.USER_UPDATE + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<User>> userInfoUpdate(@Path(AppConstants.ApiParamKey.USER_ID) int userId,
                                             @Body UserInfoUpdate userInfoUpdate);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.USER + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<TrainerDetailModel>> getTrainer(@Path(AppConstants.ApiParamKey.USER_ID) int trainerId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CLASS_DETAILS)
    Call<ResponseModel<ClassModel>> getClassDetails(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                    @Query(AppConstants.ApiParamKey.CLASS_SESSION_ID) int sessionId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.USER + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<User>> getUser(@Path(AppConstants.ApiParamKey.USER_ID) int userId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.USER + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<GymDetailModel>> getGym(@Path(AppConstants.ApiParamKey.USER_ID) int gymId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.USER_PACKAGE_DETAIL)
    Call<ResponseModel<List<UserPackageDetail>>> getUserPackageDetail(@Query(AppConstants.ApiParamKey.USER_ID) int userId, @Query(AppConstants.ApiParamKey.ID) long id);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.DEVICE_SAVE)
    Call<ResponseModel<Boolean>> saveDevice(@Body DeviceUpdate deviceUpdate);

    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.USER_UPDATE + "/{" + AppConstants.ApiParamKey.USER_ID + "}")
    Call<ResponseModel<User>> updateFocus(@Path(AppConstants.ApiParamKey.USER_ID) int userId,
                                          @Body ChooseFocusRequest chooseFocusRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.UPDATE_PASS)
    Call<ResponseModel<String>> changePass(@Body ChangePasswordRequest changePasswordRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CLASS_RATING)
    Call<ResponseModel<RatingResponseModel>> submitClassRating(@Body ClassRatingRequest classRatingRequest);

    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.CLASS_RATING + "/{" + AppConstants.ApiParamKey.RATING_ID + "}")
    Call<ResponseModel<RatingResponseModel>> updateClassRating(@Path(AppConstants.ApiParamKey.RATING_ID) long ratingId, @Body ClassRatingRequest classRatingRequest);


    @Headers("Content-type: application/json")
    @PATCH(AppConstants.Url.CLASS_RATING_PATCH)
    Call<ResponseModel<RatingResponseModel>> publishClassRating(@Path("id") long id, @Body PublishRequest classRatingRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CLASS_UNRATED)
    Call<ResponseModel<UnratedClassData>> unRatedClassList(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                           @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                           @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.CLASS_RATING)
    Call<ResponseModel<ClassRatingUserData>> getRatingList(@Query(AppConstants.ApiParamKey.CLASS_ID) int classId,
                                                           @Query(AppConstants.ApiParamKey.STATUS_LIST) int statusList,
                                                           @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                           @Query(AppConstants.ApiParamKey.SIZE) int size);


    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.PROMO_CODE)
    Call<ResponseModel<PromoCode>> applyPromoCode(@Body PromoCodeRequest promoCodeRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.TRAINER_LIST)
    Call<ResponseModel<List<TrainerModel>>> getTrainers(@Query(AppConstants.ApiParamKey.GYM_ID) int gymId,
                                                        @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                        @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GYM_LIST)
    Call<ResponseModel<List<GymModel>>> getGyms(@Query(AppConstants.ApiParamKey.CATEGORY_ID) int categoryId,
                                                @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                @Query(AppConstants.ApiParamKey.SIZE) int size);


    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.NOTIFICATIONS)
    Call<ResponseModel<List<NotificationModel>>> getNotifications(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                                  @Query(AppConstants.ApiParamKey.PAGE) int page,
                                                                  @Query(AppConstants.ApiParamKey.SIZE) int size);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SAVE_IN_WISH_LIST)
    Call<ResponseModel<WishListResponse>> addToWishList(@Body WishListRequest wishListRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.FOLLOW)
    Call<ResponseModel<FollowResponse>> follow(@Body FollowRequest followRequest);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.UN_FOLLOW)
    Call<ResponseModel<FollowResponse>> unFollow(@Body FollowRequest followRequest);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.WISH_DELETE + "/{" + AppConstants.ApiParamKey.ID + "}")
    Call<ResponseModel<String>> removeFromWishList(@Path(AppConstants.ApiParamKey.ID) int wishId);

    @Headers("Content-type: application/json")
    @DELETE(AppConstants.Url.DELETE_MEDIA)
    Call<ResponseModel<User>> deleteMedia(@Path(AppConstants.ApiParamKey.ID) long mediaId);

    @Headers("Content-type: application/json")
    @DELETE(AppConstants.Url.UN_JOIN_CLASS + "/{" + AppConstants.ApiParamKey.ID + "}")
    Call<ResponseModel<User>> unJoinClass(@Path(AppConstants.ApiParamKey.ID) int classSessionId,
                                          @Query(AppConstants.ApiParamKey.CANCELLATION_ID) Integer cancellationId,
                                          @Query(AppConstants.ApiParamKey.REMARK) String remark);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.SEARCH)
    Call<ResponseModel<SearchResults>> search(@Query(AppConstants.ApiParamKey.ID) int userId,
                                              @Query(AppConstants.ApiParamKey.QUERY_STRING) String queryString,
                                              @Query(AppConstants.ApiParamKey.SEARCH_FOR) String searchFor);

    @Multipart
    @POST(AppConstants.Url.MEDIA_UPDATE + "/{" + AppConstants.ApiParamKey.ID + "}")
    Call<ResponseModel<MediaResponse>> updateMediaImage(@Part MultipartBody.Part media,
                                                        @Path(AppConstants.ApiParamKey.ID) int id,
                                                        @Query(AppConstants.ApiParamKey.OBJECT_TYPE) String objectType,
                                                        @Query(AppConstants.ApiParamKey.OBJECT_DATA_ID) int objectDataId,
                                                        @Query(AppConstants.ApiParamKey.MEDIA_FOR) String mediaFor,
                                                        @Query(AppConstants.ApiParamKey.MEDIA_TYPE) String mediaType,
                                                        @Query(AppConstants.ApiParamKey.MEDIA_NAME) String mediaName,
                                                        @Query(AppConstants.ApiParamKey.UNIQUE_CHAR) String uniqueChar);

    @Multipart
    @POST(AppConstants.Url.MEDIA_UPLOAD)
    Call<ResponseModel<MediaResponse>> uploadMediaImage(@Part MultipartBody.Part media,
                                                        @Query(AppConstants.ApiParamKey.OBJECT_TYPE) String objectType,
                                                        @Query(AppConstants.ApiParamKey.OBJECT_DATA_ID) int objectDataId,
                                                        @Query(AppConstants.ApiParamKey.MEDIA_FOR) String mediaFor,
                                                        @Query(AppConstants.ApiParamKey.MEDIA_TYPE) String mediaType,
                                                        @Query(AppConstants.ApiParamKey.MEDIA_NAME) String mediaName,
                                                        @Query(AppConstants.ApiParamKey.UNIQUE_CHAR) String uniqueChar);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GET_PAYMENT_DETAIL)
    Call<ResponseModel<PaymentConfirmationResponse>> getPaymentDetail(@Path("id") long id);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.SUPPORT_CONTACT)
    Call<ResponseModel<Object>> getContactResponse(@Body ContactRequestModel contactRequestModel);

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.CLASS_LIST)
    Call<ResponseModel<List<ScheduleClassModel>>> getScheduleClass(@Body ScheduleRequest scheduleRequest);


    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GET_EXPLORE_DETAIL)
    Call<ResponseModel<List<ExploreDataModel>>> getExploreData(@Query(AppConstants.ApiParamKey.USER_ID) long userId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GET_STORE_DATA)
    Call<ResponseModel<List<StoreApiModel>>> getStoreData();

    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.USER_UPDATE_STORE)
    Call<ResponseModel<StoreModel>> updateStoreId(@Query(AppConstants.ApiParamKey.USER_ID) int userId,
                                                  @Query(AppConstants.ApiParamKey.STORE_ID) int storeId);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.PAYMENT_INITIATE)
    Call<ResponseModel<String>> getPaymentInitiate();

    @Headers("Content-type: application/json")
    @POST(AppConstants.Url.INTERESTED_CITY)
    Call<ResponseModel<InterestedCityModel>> uploadInterestedCity(@Body InterestedCityRequestModel interestedCityModel);

    @Headers("Content-type: application/json")
    @PUT(AppConstants.Url.INTERESTED_CITY)
    Call<ResponseModel<InterestedCityModel>> uploadInterestedCity(@Query(AppConstants.ApiParamKey.ID) int id,
                                                                  @Body InterestedCityRequestModel interestedCityModel);

    @Headers("Content-type: application/json")
    @GET(AppConstants.Url.GET_CANCELLATION_REASON)
    Call<ResponseModel<List<BookingCancellationResponse>>> getCancellationReason();
}
