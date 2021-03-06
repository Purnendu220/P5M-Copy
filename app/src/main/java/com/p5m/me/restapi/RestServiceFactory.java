package com.p5m.me.restapi;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.p5m.me.BuildConfig;
import com.p5m.me.MyApp;
import com.p5m.me.helper.Helper;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

import static com.p5m.me.utils.LanguageUtils.getLocalLanguage;


public class RestServiceFactory {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
    private static OkHttpClient.Builder httpClient2 = new OkHttpClient.Builder();
    private static OkHttpClient.Builder httpClientYoutube = new OkHttpClient.Builder();


    private static Retrofit retrofit,retrofit2,retrofitYoutube;

    private static ApiService apiService;
    private static ApiService apiService2;
    private static ApiService apiServiceYoutube;



    private static <S> S createService(Class<S> serviceClass) {
        if (apiService == null) {

            if (MyApp.RETROFIT_SHOW_LOG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                httpClient.addInterceptor(logging);
            }
            httpClient.connectTimeout(1, TimeUnit.MINUTES);
            httpClient.readTimeout(2, TimeUnit.MINUTES);
            httpClient.connectTimeout(2, TimeUnit.MINUTES);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String auth = (TempStorage.getAuthToken() != null && !TempStorage.getAuthToken().isEmpty()) ?
                            TempStorage.getAuthToken() : AppConstants.ApiParamValue.NO_TOKEN;
                    String language= getLocalLanguage();
                    LogUtils.debug("Token " + auth);

                    HttpUrl url = chain.request().url().newBuilder()
                            .addQueryParameter(AppConstants.ApiParamKey.DEBUG, BuildConfig.DEBUG+"")
                            .build();


                    Request request = chain.request().newBuilder()
                            .addHeader(AppConstants.ApiParamKey.MYU_AUTH_TOKEN, auth)
                            .addHeader(AppConstants.ApiParamKey.DEVICE_TYPE, AppConstants.ApiParamKey.DEVICE_TYPE_VALUE)

                            .addHeader(AppConstants.ApiParamKey.APP_VERSION, BuildConfig.VERSION_NAME_API)
                            .addHeader(AppConstants.ApiParamKey.APP_Language, language)
                            .addHeader(AppConstants.ApiParamKey.APP_STORE_ID, String.valueOf(TempStorage.getCountryId()))
                            .url(url)
                            .build();
                    return chain.proceed(request);
                }
            });

            String baseUrl = BuildConfig.BASE_URL+BuildConfig.BASE_URL_MIDL;



            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create());
            retrofit = builder.client(httpClient.build()).build();
            apiService = (ApiService) retrofit.create(serviceClass);
        }
        return (S) apiService;
    }

    private static <S> S createServiceBaseUrl2(Class<S> serviceClass) {
        if (apiService2 == null) {

            if (MyApp.RETROFIT_SHOW_LOG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                httpClient2.addInterceptor(logging);
            }

            httpClient2.readTimeout(30, TimeUnit.SECONDS);
            httpClient2.connectTimeout(30, TimeUnit.SECONDS);

            httpClient2.addInterceptor(chain -> {
                Request request = chain.request().newBuilder()
                        .addHeader(AppConstants.ApiParamKey.AUTHORIZATION, "Basic "+ Helper.getbase64EncodedString(AppConstants.plainCredentials).trim())

                        .build();
                return chain.proceed(request);
            });

            String baseUrl = "https://api.agora.io/dev/v1/";



            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create());
            retrofit2 = builder.client(httpClient2.build()).build();
            apiService2 = (ApiService) retrofit2.create(serviceClass);
        }
        return (S) apiService2;
    }


    private static <S> S createServiceBaseUrlYoutube(Class<S> serviceClass) {
        if (apiServiceYoutube == null) {

            if (MyApp.RETROFIT_SHOW_LOG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
                httpClientYoutube.addInterceptor(logging);
            }

            httpClientYoutube.readTimeout(30, TimeUnit.SECONDS);
            httpClientYoutube.connectTimeout(30, TimeUnit.SECONDS);



            String baseUrl = "https://www.googleapis.com/youtube/v3/";



            Retrofit.Builder builder =
                    new Retrofit.Builder()
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create());
            retrofitYoutube = builder.client(httpClientYoutube.build()).build();
            apiServiceYoutube = (ApiService) retrofitYoutube.create(serviceClass);
        }
        return (S) apiServiceYoutube;
    }

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static ApiService createService() {
        return createService(ApiService.class);
    }
    public static ApiService createService2() {
        return createServiceBaseUrl2(ApiService.class);
    }
    public static ApiService createServiceYoutube() {
        return createServiceBaseUrlYoutube(ApiService.class);
    }
}
