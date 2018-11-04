package com.ridvan.rxconverter;

import retrofit2.Retrofit;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public class ServiceContext {
    public static ServiceContext instance;
    public final Retrofit retrofit;

    private ServiceContext(Retrofit retrofit) {
        this.retrofit = retrofit;
    }

    public static void initialize(Retrofit retrofit) {
        if (instance != null) throw new IllegalStateException("ServiceContext already initialized");
        ServiceContext.instance = new ServiceContext(retrofit);
    }
}
