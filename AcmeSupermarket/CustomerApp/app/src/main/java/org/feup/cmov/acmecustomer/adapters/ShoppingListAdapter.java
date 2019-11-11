package org.feup.cmov.acmecustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.activities.MainMenuActivity;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;

import java.util.Locale;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private Customer customer;
    private Product recentlyDeletedProduct;
    private int recentlyDeletedProductIndex;
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
        holder.productPrice.setText(String.format(Locale.US, "%.2f €", this.customer.getShoppingCart().getProducts().get(position).getFullPrice()));
    }

    @Override
    public int getItemCount() {
        return this.customer.getShoppingCart().getProducts().size();
    }

    public void deleteProduct(int position) {
        this.recentlyDeletedProduct = this.customer.getShoppingCart().getProducts().get(position);
        this.recentlyDeletedProductIndex = position;
        this.customer.getShoppingCart().removeProduct(position);
        notifyItemRemoved(position);
        if(context instanceof MainMenuActivity) {
            updateButtons();
            ((MainMenuActivity) context).updateCartValue();
        }
        showUndoSnackbar();
    }

    public void showUndoSnackbar() {
        View view = ((MainMenuActivity) context).findViewById(R.id.coordinator_layout);
        CharSequence cs = this.recentlyDeletedProduct.getName() + " removed from the shopping cart";
        Snackbar snackbar = Snackbar.make(view, cs, Snackbar.LENGTH_LONG);
        snackbar.setAction("Undo", v -> undoDeleteProduct());
        snackbar.show();
    }

    public void undoDeleteProduct() {
        this.customer.getShoppingCart().getProducts().add(this.recentlyDeletedProductIndex, this.recentlyDeletedProduct);
        notifyItemInserted(this.recentlyDeletedProductIndex);
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
        FloatingActionButton addProductButton = ((MainMenuActivity) context).findViewById(R.id.add_new_item_button);
        if(checkoutButton != null) {
            if(this.getItemCount() > 0) {
                checkoutButton.show();
            } else {
                checkoutButton.hide();
            }
            checkoutButton.setOnClickListener(view -> ((MainMenuActivity) context).checkout());
        }

        if(addProductButton != null) {
            if(this.getItemCount() >= 0 && this.getItemCount() < 10) {
                addProductButton.show();
            } else {
                addProductButton.hide();
            }
            addProductButton.setOnClickListener(view -> ((MainMenuActivity) context).scanProduct());
        }
    }

}
