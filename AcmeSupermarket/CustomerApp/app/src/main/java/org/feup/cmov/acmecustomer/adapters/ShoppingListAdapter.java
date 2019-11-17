package org.feup.cmov.acmecustomer.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.activities.MainMenuActivity;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.ShoppingCart.Pair;
import org.feup.cmov.acmecustomer.models.Product;

import java.util.Locale;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ShoppingListViewHolder> {
    private Customer customer;
    private Pair<Product, Integer> recentlyDeletedProduct;
    private int recentlyDeletedProductIndex;
    private Context context;
    private Snackbar currentSnackbar;

    public ShoppingListAdapter(Context context, Customer customer) {
        this.customer = customer;
        this.context = context;
    }

    @Override
    public ShoppingListAdapter.ShoppingListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_product_layout, parent, false);
        return new ShoppingListViewHolder(view, new ShoppingListViewHolder.ItemClickListener() {
            @Override
            public void addQuantity(int position) {
                customer.getShoppingCart().addProduct(customer.getShoppingCart().getProducts().get(position).getFirst());
                notifyDataSetChanged();
                if(context instanceof MainMenuActivity) {
                    updateButtons();
                    ((MainMenuActivity) context).updateCartValue();
                }
                if(currentSnackbar != null) {
                    currentSnackbar.dismiss();
                }
            }

            @Override
            public void removeQuantity(int position) {
                customer.getShoppingCart().removeProductQuantity(position);
                notifyDataSetChanged();
                if(context instanceof MainMenuActivity) {
                    updateButtons();
                    ((MainMenuActivity) context).updateCartValue();
                }
                if(currentSnackbar != null) {
                    currentSnackbar.dismiss();
                }
            }
        });
    }

    @Override
    public void onBindViewHolder(ShoppingListViewHolder holder, int position) {
        holder.productName.setText(this.customer.getShoppingCart().getProducts().get(position).getFirst().getName());
        holder.productPrice.setText(String.format(Locale.US, "%.2f €", this.customer.getShoppingCart().getProducts().get(position).getFirst().getFullPrice() * this.customer.getShoppingCart().getProducts().get(position).getSecond()));
        holder.productQuantity.setText(this.customer.getShoppingCart().getProducts().get(position).getSecond().toString());

        if(context instanceof MainMenuActivity) {
            if(this.customer.getShoppingCart().getProducts().get(position).getSecond() <= 1) {
                holder.removeQuantity.setVisibility(View.INVISIBLE);
            } else {
                holder.removeQuantity.setVisibility(View.VISIBLE);
            }

            if(this.customer.getShoppingCart().isFull()) {
                holder.addQuantity.setVisibility(View.INVISIBLE);
            } else {
                holder.addQuantity.setVisibility(View.VISIBLE);
            }
        } else {
            holder.removeQuantity.setVisibility(View.INVISIBLE);
            holder.addQuantity.setVisibility(View.INVISIBLE);
        }
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
        notifyDataSetChanged();
        if(context instanceof MainMenuActivity) {
            updateButtons();
            ((MainMenuActivity) context).updateCartValue();
        }
        showUndoSnackbar();
    }

    public void showUndoSnackbar() {
        View view = ((MainMenuActivity) context).findViewById(R.id.coordinator_layout);
        CharSequence cs = this.recentlyDeletedProduct.getFirst().getName() + " removed from the shopping cart";
        this.currentSnackbar = Snackbar.make(view, cs, Snackbar.LENGTH_LONG);
        currentSnackbar.getView().setBackgroundColor(this.context.getColor(R.color.darkGrey));
        ((TextView) currentSnackbar.getView().findViewById(com.google.android.material.R.id.snackbar_action))
                .setTextColor(this.context.getColor(R.color.red));
        currentSnackbar.setAction("Undo", v -> undoDeleteProduct());
        currentSnackbar.show();
    }

    public void undoDeleteProduct() {
        this.customer.getShoppingCart().getProducts().add(this.recentlyDeletedProductIndex, this.recentlyDeletedProduct);
        notifyItemInserted(this.recentlyDeletedProductIndex);
        notifyDataSetChanged();
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
            if(this.customer.getShoppingCart().getProducts().size() >= 0 && !this.customer.getShoppingCart().isFull()) {
                addProductButton.show();
            } else {
                addProductButton.hide();
            }
            addProductButton.setOnClickListener(view -> ((MainMenuActivity) context).scanProduct());
        }
    }

    public static class ShoppingListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ItemClickListener listener;

        public TextView productName;
        public TextView productPrice;
        public TextView productQuantity;
        public ImageView removeQuantity;
        public ImageView addQuantity;

        public ShoppingListViewHolder(View view, ItemClickListener listener) {
            super(view);
            this.productName = view.findViewById(R.id.product_name);
            this.productPrice = view.findViewById(R.id.product_price);
            this.productQuantity = view.findViewById(R.id.product_quantity);
            this.removeQuantity = view.findViewById(R.id.remove_quantity);
            this.addQuantity = view.findViewById(R.id.add_quantity);
            this.listener = listener;
            
            removeQuantity.setOnClickListener(this);
            addQuantity.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.add_quantity:
                    listener.addQuantity(this.getLayoutPosition());
                    break;
                case R.id.remove_quantity:
                    listener.removeQuantity(this.getLayoutPosition());
                    break;
                default:
                    break;
            }
        }

        public interface ItemClickListener {
            void addQuantity(int position);
            void removeQuantity(int position);
        }
    }

}
