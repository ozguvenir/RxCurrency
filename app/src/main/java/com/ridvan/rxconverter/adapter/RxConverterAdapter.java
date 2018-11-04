package com.ridvan.rxconverter.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ridvan.rxconverter.R;
import com.ridvan.rxconverter.models.RxCurrency;

import java.util.List;

/**
 * Created by ridvanozguvenir on 4.11.2018.
 */
public class RxConverterAdapter extends RecyclerView.Adapter<RxConverterAdapter.RxCurrencyViewHolder> {

    private List<RxCurrency> rxCurrencyList;

    public RxConverterAdapter(List<RxCurrency> currencyList) {
        rxCurrencyList = currencyList;
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
            holder.editValue.setOnFocusChangeListener((v, hasFocus) -> focusValue());
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

    private void focusValue() {

    }

    static class RxCurrencyViewHolder extends RecyclerView.ViewHolder {
        TextView currencyText;
        EditText editValue;

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
