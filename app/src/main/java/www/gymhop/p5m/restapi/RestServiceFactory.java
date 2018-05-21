package www.gymhop.p5m.restapi;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.gymhop.p5m.MyApp;
import www.gymhop.p5m.storage.TempStorage;
import www.gymhop.p5m.utils.AppConstants;
import www.gymhop.p5m.utils.LogUtils;


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
                    LogUtils.debug("Token " + auth);

                    Request request = chain.request().newBuilder()
                            .addHeader(AppConstants.ApiParamKey.MYU_AUTH_TOKEN, auth)
                            .addHeader(AppConstants.ApiParamKey.USER_AGENT, AppConstants.ApiParamValue.USER_AGENT_ANDROID)
//                            .addHeader(AppConstants.ApiParamKey.APP_VERSION, TempStorage.version)
                            .build();
                    return chain.proceed(request);
                }
            });

            String baseUrl = "";

            switch (MyApp.apiMode) {
                case TESTING_BETA:
                    baseUrl = AppConstants.Url.BASE_SERVICE_BETA;
                    break;
                case TESTING_ALPHA:
                    baseUrl = AppConstants.Url.BASE_SERVICE_ALPHA;
                    break;
                case LIVE:
                    baseUrl = AppConstants.Url.BASE_SERVICE_LIVE;
                    break;
                default:
            }

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
