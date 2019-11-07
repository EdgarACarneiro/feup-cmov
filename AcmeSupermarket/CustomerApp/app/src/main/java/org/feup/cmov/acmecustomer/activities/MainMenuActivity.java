package org.feup.cmov.acmecustomer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
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
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.UUID;

public class MainMenuActivity extends AppCompatActivity {
    private Customer currentCustomer;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");

        RecyclerView recyclerView = findViewById(R.id.product_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShoppingListAdapter(this, currentCustomer);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        TextView customerName = findViewById(R.id.customer_name);
        customerName.setText("Hello, " + this.currentCustomer.getName());

        TextView cardNumber = findViewById(R.id.current_payment_option);
        cardNumber.setText(this.currentCustomer.getPaymentInfo().getMaskedCardNumber("####xxxxxxxxxxxx"));

        FloatingActionButton addProductButton = findViewById(R.id.add_new_item_button);
        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanProduct();
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
                    addProduct(contents);
                }
            }
        }
    }

    public void addProduct(String contents) {
        byte[] result = contents.getBytes(StandardCharsets.ISO_8859_1);
        ByteBuffer buffer = ByteBuffer.wrap(result);
        int tagID = buffer.getInt();
        UUID uuid = new UUID(buffer.getLong(), buffer.getLong());
        int euros = buffer.getInt();
        int cents = buffer.getInt();
        byte[] pName = new byte[buffer.get()];
        buffer.get(pName);
        String productName = new String(pName, StandardCharsets.ISO_8859_1);

        Product p = new Product(uuid.toString(), productName, euros, cents);

        this.adapter.addProduct(p);
    }

    public void updateCartValue() {
        TextView cartValue = findViewById(R.id.shopping_cart_value);
        cartValue.setText(this.currentCustomer.getShoppingCartValue() + "€");
    }

    public void checkout() {
        Intent intent = new Intent(this, TransactionConfirmationActivity.class);
        intent.putExtra("Customer", this.currentCustomer);
        startActivity(intent);
    }

    public void scanProduct() {
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
    }

    public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        private ShoppingListAdapter adapter;

        public SwipeToDeleteCallback(ShoppingListAdapter adapter) {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
            this.adapter = adapter;
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            this.adapter.deleteProduct(position);
        }

    }
}