package org.feup.cmov.acmecustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.models.TransactionRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.TransactionListViewHolder> {
    private ArrayList<TransactionRecord> transactions;
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

    public TransactionListAdapter(Context context) {
        this.context = context;
        this.transactions = new ArrayList<>();
    }

    public void setTransactions(ArrayList<TransactionRecord> newTransactions) {
        this.transactions = newTransactions;
    }

    @Override
    public TransactionListAdapter.TransactionListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_transaction_layout, parent, false);
        return new TransactionListAdapter.TransactionListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TransactionListAdapter.TransactionListViewHolder holder, int position) {
        holder.date.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.US).format(this.transactions.get(position).getDate()));
        holder.cardNumber.setText(this.transactions.get(position).getCustomer().getPaymentInfo().getMaskedCardNumber("xxxx xxxx xxxx ####"));

        if(this.transactions.get(position).usedVoucher()) {
            holder.usedCoupon.setVisibility(View.VISIBLE);
        } else {
            holder.usedCoupon.setVisibility(View.INVISIBLE);
        }

        if(this.transactions.get(position).getDiscount() > 0) {
            holder.usedDiscount.setVisibility(View.VISIBLE);
            holder.transactionValue.setText(String.format(Locale.US, "%.2f € ",
                    this.transactions.get(position).getTotal() - this.transactions.get(position).getDiscount())
            );
        } else {
            holder.usedDiscount.setVisibility(View.INVISIBLE);
            holder.transactionValue.setText(String.format(Locale.US, "%.2f €", this.transactions.get(position).getTotal()));
        }
    }

    @Override
    public int getItemCount() {
        return this.transactions.size();
    }
}
