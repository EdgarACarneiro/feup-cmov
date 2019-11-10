package org.feup.cmov.acmecustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.models.Transaction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionListViewHolder> {
    private ArrayList<Transaction> transactions;
    private Context context;

    public static class TransactionListViewHolder extends RecyclerView.ViewHolder {
        public TextView date;
        public TextView cardNumber;
        public TextView transactionValue;
        public ImageView usedCoupon;
        public ImageView usedDiscount;

        public TransactionListViewHolder(View view) {
            super(view);
            this.date = view.findViewById(R.id.date);
            this.cardNumber = view.findViewById(R.id.card_number);
            this.transactionValue = view.findViewById(R.id.value);
            this.usedCoupon = view.findViewById(R.id.used_coupon);
            this.usedDiscount = view.findViewById(R.id.used_discount);
        }
    }

    public TransactionListAdapter(Context context, ArrayList<Transaction> transactions) {
        this.context = context;
        this.transactions = transactions;
    }

    @Override
    public TransactionListAdapter.TransactionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_layout, parent, false);
        return new TransactionListAdapter.TransactionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionListAdapter.TransactionListViewHolder holder, int position) {
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(this.transactions.get(position).getDate()));
        holder.cardNumber.setText(this.transactions.get(position).getCustomer().getPaymentInfo().getMaskedCardNumber("xxxxxxxxxxxx####"));

        if(this.transactions.get(position).getCoupon() != null) {
            holder.usedCoupon.setVisibility(View.VISIBLE);
        } else {
            holder.usedCoupon.setVisibility(View.INVISIBLE);
        }

        if(this.transactions.get(position).isDiscounted()) {
            holder.usedDiscount.setVisibility(View.VISIBLE);
            holder.transactionValue.setText(String.format(Locale.US, "%.2f €", this.transactions.get(position).getCart().getValue() * 0.85));
        } else {
            holder.usedDiscount.setVisibility(View.INVISIBLE);
            holder.transactionValue.setText(String.format(Locale.US, "%.2f €", this.transactions.get(position).getCart().getValue()));
        }
    }

    @Override
    public int getItemCount() {
        return this.transactions.size();
    }
}
