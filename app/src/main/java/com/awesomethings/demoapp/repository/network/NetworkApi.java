package com.awesomethings.demoapp.repository.network;

import com.awesomethings.demoapp.BuildConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.awesomethings.demoapp.repository.network.RequestApi._BASE_URL;

/**
 * Created by Master on 6/6/17.
 */

public interface NetworkApi {


    @GET(RequestApi._MAIN_DATA)
    Call<ResponseBody> startMarkersRetrievingRequest();


    class Factory {
        private Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(_BASE_URL)
                .client(httpClient.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        private static NetworkApi.Factory serverApi;
        private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        public static NetworkApi getApi() {
            if (BuildConfig.DEBUG){
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request.Builder builder = chain.request().newBuilder()
                                .addHeader("Content-Type", "application/json");
                        return chain.proceed(builder.build());
                    }
                });

                httpClient.interceptors().add(interceptor);
            }
            if (serverApi == null){
                serverApi = new NetworkApi.Factory();
            }
            return serverApi.retrofit.create(NetworkApi.class);
        }
    }
}
