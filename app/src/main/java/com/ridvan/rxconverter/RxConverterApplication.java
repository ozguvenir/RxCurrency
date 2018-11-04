package com.ridvan.rxconverter;

import android.app.Application;

import com.ridvan.rxconverter.util.RxConverterConstants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public class RxConverterApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // HttpLoggingInterceptor
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        // OkHttp
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .writeTimeout(RxConverterConstants.TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(RxConverterConstants.TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(RxConverterConstants.TIMEOUT, TimeUnit.SECONDS)
                .build();

        // initializing service and retrofit
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RxConverterConstants.REVOLUT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        ServiceContext.initialize(retrofit);
    }
}
