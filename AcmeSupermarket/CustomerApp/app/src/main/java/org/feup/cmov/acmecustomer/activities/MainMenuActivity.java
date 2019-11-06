package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.adapters.ShoppingListAdapter;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");
        this.currentCustomer.setShoppingCart(this.createProducts());

        RecyclerView recyclerView = findViewById(R.id.product_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ShoppingListAdapter mAdapter = new ShoppingListAdapter(currentCustomer.getShoppingCart().getProducts());
        recyclerView.setAdapter(mAdapter);

        TextView customerName = findViewById(R.id.customer_name);
        customerName.setText("Hello, " + this.currentCustomer.getName());

        TextView cardNumber = findViewById(R.id.current_payment_option);
        cardNumber.setText(this.currentCustomer.getPaymentInfo().getMaskedCardNumber("####xxxxxxxxxxxx"));

        TextView cartValue = findViewById(R.id.shopping_cart_value);
        cartValue.setText(this.currentCustomer.getShoppingCartValue() + "€");

        FloatingActionButton addProductButton = findViewById(R.id.add_new_item_button);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProduct();
            }
        });

        FloatingActionButton checkoutButton = findViewById(R.id.checkout_button);
        if(recyclerView.getAdapter().getItemCount() > 0) {
            checkoutButton.show();
        } else {
            checkoutButton.hide();
        }
        checkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                if (contents != null){
                    Toast.makeText(this, contents, Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    //only for testing
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

    public void checkout() {
        Intent intent = new Intent(this, TransactionConfirmationActivity.class);
        intent.putExtra("Customer", this.currentCustomer);
        startActivity(intent);
    }

    public void addProduct() {
        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE",  "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException anfe) {
            Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
        //add product to products array logic here
    }
}
