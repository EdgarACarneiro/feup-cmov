package org.feup.cmov.acmecustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.models.Product;

import java.util.ArrayList;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private ArrayList<Product> products;

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productPrice;

        public ShoppingListViewHolder(View view) {
            super(view);
            this.productName = view.findViewById(R.id.product_name);
            this.productPrice = view.findViewById(R.id.product_price);
        }
    }

    public ShoppingListAdapter(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        holder.productName.setText(this.products.get(position).getName());
        holder.productPrice.setText(Double.toString(this.products.get(position).getFullPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

}
