package com.p5m.me.restapi;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.p5m.me.BuildConfig;
import com.p5m.me.MyApp;
import com.p5m.me.storage.TempStorage;
import com.p5m.me.utils.AppConstants;
import com.p5m.me.utils.LogUtils;

import static com.p5m.me.utils.LanguageUtils.getLocalLanguage;


public class RestServiceFactory {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    private static Retrofit retrofit;

    private static ApiService apiService;

    private static <S> S createService(Class<S> serviceClass) {
        if (apiService == null) {

            if (MyApp.RETROFIT_SHOW_LOG) {
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                httpClient.addInterceptor(logging);
            }

            httpClient.readTimeout(30, TimeUnit.SECONDS);
            httpClient.connectTimeout(30, TimeUnit.SECONDS);

            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    String auth = (TempStorage.getAuthToken() != null && !TempStorage.getAuthToken().isEmpty()) ?
                            TempStorage.getAuthToken() : AppConstants.ApiParamValue.NO_TOKEN;
                    String language= getLocalLanguage();
                    LogUtils.debug("Token " + auth);

                    Request request = chain.request().newBuilder()
                            .addHeader(AppConstants.ApiParamKey.MYU_AUTH_TOKEN, auth)
                            .addHeader(AppConstants.ApiParamKey.USER_AGENT, AppConstants.ApiParamValue.USER_AGENT_ANDROID)
                            .addHeader(AppConstants.ApiParamKey.APP_VERSION, BuildConfig.VERSION_NAME)
                            .addHeader(AppConstants.ApiParamKey.APP_Language, language)
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

    public static Retrofit retrofit() {
        return retrofit;
    }

    public static ApiService createService() {
        return createService(ApiService.class);
    }
}
