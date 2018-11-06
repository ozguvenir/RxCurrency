package com.ridvan.rxconverter.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ridvan.rxconverter.R;
import com.ridvan.rxconverter.ServiceContext;
import com.ridvan.rxconverter.adapter.RxConverterAdapter;
import com.ridvan.rxconverter.network.RxConverterService;
import com.ridvan.rxconverter.util.RxConverterConstants;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.ridvan.rxconverter.util.RxConverterConstants.DEFAULT_CURRENCY;

public class MainActivity extends AppCompatActivity {

    RxConverterAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.my_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        layoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new RxConverterAdapter();

        Observable.interval(RxConverterConstants.TIME_PERIOD, TimeUnit.MILLISECONDS)
                .flatMap(n -> ServiceContext.instance.retrofit.create(RxConverterService.class).getLatestCurrencies(DEFAULT_CURRENCY)
                        .flatMap(Observable::just)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .onErrorResumeNext(observer -> {
                        })
                        .doOnNext(currencies -> {
                            adapter.createOrUpdateCurrencyList(currencies);
                            recyclerView.setAdapter(adapter);
                        })
                        .doOnError(throwable -> {
                        })
                ).subscribe();
    }

}

