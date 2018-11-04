package com.ridvan.rxconverter.network;

import com.ridvan.rxconverter.models.RxConverterResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public interface RxConverterService {
    @GET("/latest")
    Observable<RxConverterResponse> getLatestCurrencies(@Query("base") String base);
}
