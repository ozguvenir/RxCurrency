package com.ridvan.rxconverter.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.ridvan.rxconverter.R;
import com.ridvan.rxconverter.models.RxConverterResponse;
import com.ridvan.rxconverter.models.RxCurrency;
import com.ridvan.rxconverter.util.RxConverterConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public class RxConverterAdapter extends RecyclerView.Adapter<RxConverterAdapter.RxCurrencyViewHolder> {

    private List<RxCurrency> rxCurrencyList;
    private Double baseCurrencyValue = 1.0;

    public RxConverterAdapter() {
        rxCurrencyList = new ArrayList<>();
    }

    public void creatOrUpdateCurrencyList(RxConverterResponse response) {
        if (response != null) {
            response.getRates().put(response.getBase(), baseCurrencyValue);
            if (rxCurrencyList.size() > 0) {
                this.updateCurrencyList(response.getRates());
            } else {
                rxCurrencyList.clear();
                for (Map.Entry<String, Double> current : response.getRates().entrySet()) {
                    if (RxConverterConstants.DEFAULT_CURRENCY.equals(current.getKey()))
                        rxCurrencyList.add(0, new RxCurrency(current.getKey(), current.getValue()));
                    rxCurrencyList.add(new RxCurrency(current.getKey(), current.getValue()));
                }
            }
        }
    }

    public void updateCurrencyList(HashMap<String, Double> rates) {
        for (String name : rates.keySet()) {
            Double value = rates.get(name);

            for (RxCurrency rxCurrency : rxCurrencyList) {
                if (rxCurrency.getName().equals(name)) {
                    rxCurrency.setValue(value);
                    break;
                }
            }
        }
    }

    @NonNull
    @Override
    public RxCurrencyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View currencyView = inflater.inflate(R.layout.item_rxcurrency, parent, false);
        return new RxCurrencyViewHolder(currencyView);
    }

    @Override
    public void onBindViewHolder(@NonNull RxCurrencyViewHolder holder, int position) {
        RxCurrency currency = rxCurrencyList.get(position);
        if (currency != null) {
            holder.bindTo(currency);
            holder.itemView.setOnClickListener(v -> swapSelectedItemToTop(position));
            //holder.editValue.setOnFocusChangeListener((v, hasFocus) -> focusValue(hasFocus, holder, position));
        }
    }

    @Override
    public int getItemCount() {
        return rxCurrencyList.size();
    }

    private void swapSelectedItemToTop(int position) {
        if (rxCurrencyList != null && rxCurrencyList.size() > 0) {
            rxCurrencyList.add(0, rxCurrencyList.remove(position));
            notifyItemMoved(position, 0);
            notifyDataSetChanged();
        }
    }

    private void focusValue(boolean hasFocus, RxCurrencyViewHolder viewHolder, int position) {
        if (hasFocus) {
            swapSelectedItemToTop(position);
            viewHolder.rxEditValue = RxTextView.textChanges(viewHolder.editValue)
                    .skip(1)
                    .map(text -> text != null ? text : "")
                    .map((CharSequence s) -> Double.parseDouble(s.toString()))
                    .doOnNext(this::updateAllList)
                    .subscribe();
        } else {
            if (viewHolder.rxEditValue != null) {
                viewHolder.rxEditValue.dispose();

                InputMethodManager imm = (InputMethodManager) viewHolder.itemView.getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                if (imm != null)
                    imm.hideSoftInputFromWindow(viewHolder.editValue.getWindowToken(), 0);
            }
        }
    }

    private void updateAllList(Double newValue) {
        for (RxCurrency rxCurrency : rxCurrencyList) {
            rxCurrency.setValue(newValue * getValueByName(rxCurrency.getName()));
        }
    }

    private Double getValueByName(String name) {
        for (RxCurrency rxCurrency : rxCurrencyList) {
            if (rxCurrency.getName().equals(name))
                return rxCurrency.getValue();
        }
        return 0.00;
    }

    static class RxCurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView currencyText;
        EditText editValue;

        private Disposable rxEditValue;

        public RxCurrencyViewHolder(View itemView) {
            super(itemView);
            currencyText = itemView.findViewById(R.id.currency_text_view);
            editValue = itemView.findViewById(R.id.currency_edit_text);
        }

        public void bindTo(RxCurrency currency) {
            currencyText.setText(currency.getName());
            editValue.setText(String.valueOf(currency.getValue()));
        }
    }
}
