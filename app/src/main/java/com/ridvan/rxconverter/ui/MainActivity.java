package com.ridvan.rxconverter.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ridvan.rxconverter.R;
import com.ridvan.rxconverter.ServiceContext;
import com.ridvan.rxconverter.adapter.RxConverterAdapter;
import com.ridvan.rxconverter.models.RxCurrency;
import com.ridvan.rxconverter.network.RxConverterService;
import com.ridvan.rxconverter.util.RxConverterConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    RxConverterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        List<RxCurrency> list = new ArrayList<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Observable.interval(RxConverterConstants.TIME_PERIOD, TimeUnit.MILLISECONDS)
                .flatMap(n -> ServiceContext.instance.retrofit.create(RxConverterService.class).getLatestCurrencies(RxConverterConstants.DEFAULT_CURRENCY)
                        .flatMap(Observable::just)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(observer -> {
                        })
                        .doOnNext(currencies -> {
                            //TODO:success
                            list.clear();
                            for (Map.Entry<String, Double> current : currencies.getRates().entrySet())
                                list.add(new RxCurrency(current.getKey(), current.getValue()));

                            adapter = new RxConverterAdapter(list);
                            recyclerView.setAdapter(adapter);
                        })
                        .doOnError(throwable -> {
                            //TODO:fail
                        })
                ).subscribe();
    }
}
