package org.feup.cmov.acmecustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.activities.MainMenuActivity;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private Customer customer;
    private Product recentlyDeletedProduct;
    private Context context;

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public TextView productPrice;

        public ShoppingListViewHolder(View view) {
            super(view);
            this.productName = view.findViewById(R.id.product_name);
            this.productPrice = view.findViewById(R.id.product_price);
        }
    }

    public ShoppingListAdapter(Context context, Customer customer) {
        this.customer = customer;
        this.context = context;
    }

    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout, parent, false);
        return new ShoppingListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        holder.productName.setText(this.customer.getShoppingCart().getProducts().get(position).getName());
        holder.productPrice.setText(this.customer.getShoppingCart().getProducts().get(position).getFullPrice() + "€");
    }

    @Override
    public int getItemCount() {
        return this.customer.getShoppingCart().getProducts().size();
    }

    public void deleteProduct(int position) {
        this.recentlyDeletedProduct = this.customer.getShoppingCart().getProducts().get(position);
        this.customer.getShoppingCart().removeProduct(position);
        notifyItemRemoved(position);
        if(context instanceof MainMenuActivity) {
            updateButtons();
            ((MainMenuActivity) context).updateCartValue();
        }
    }

    public void addProduct(Product p) {
        this.customer.getShoppingCart().addProduct(p);
        notifyDataSetChanged();
        if(context instanceof MainMenuActivity) {
            updateButtons();
            ((MainMenuActivity) context).updateCartValue();
        }
    }

    public void updateButtons() {
        FloatingActionButton checkoutButton = ((MainMenuActivity) context).findViewById(R.id.checkout_button);
        if(checkoutButton != null) {
            if(this.getItemCount() > 0) {
                checkoutButton.show();
            } else {
                checkoutButton.hide();
            }
            checkoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((MainMenuActivity) context).checkout();
                }
            });
        }
    }

}
