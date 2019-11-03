package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.adapters.ShoppingListAdapter;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.PaymentInfo;
import org.feup.cmov.acmecustomer.models.Product;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    private Customer currentCustomer;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");
        this.currentCustomer.setShoppingCart(this.createProducts());

        this.recyclerView = findViewById(R.id.product_list);

        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ShoppingListAdapter(currentCustomer.getShoppingCart());
        recyclerView.setAdapter(mAdapter);

        TextView customerName = findViewById(R.id.customer_name);
        customerName.setText("Hello, " + this.currentCustomer.getName());

        TextView cardNumber = findViewById(R.id.current_payment_option);
        cardNumber.setText(this.currentCustomer.getPaymentInfo().getMaskedCardNumber("####xxxxxxxxxxxx"));

        TextView cartValue = findViewById(R.id.shopping_cart_value);
        cartValue.setText(this.currentCustomer.getShoppingCartValue() + "€");


        FloatingActionButton checkoutButton = findViewById(R.id.checkout_button);
        if(recyclerView.getAdapter().getItemCount() > 0) {
            checkoutButton.show();
        } else {
            checkoutButton.hide();
        }
    }

    public ArrayList<Product> createProducts() {
        ArrayList<Product> products = new ArrayList<>();
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz", 12, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz1", 15, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz2", 12, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz3", 13, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz", 12, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz1", 15, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz2", 12, 50));
        products.add(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz3", 13, 50));
        return products;
    }
}
