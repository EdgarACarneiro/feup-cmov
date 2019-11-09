package org.feup.cmov.acmecustomer.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.adapters.ShoppingListAdapter;
import org.feup.cmov.acmecustomer.adapters.TransactionListAdapter;
import org.feup.cmov.acmecustomer.models.Coupon;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;
import org.feup.cmov.acmecustomer.models.ShoppingCart;
import org.feup.cmov.acmecustomer.models.Transaction;
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import static org.feup.cmov.acmecustomer.Utils.decode;
import static org.feup.cmov.acmecustomer.Utils.encode;
import static org.feup.cmov.acmecustomer.Utils.fromBase64;

public class MainMenuActivity extends AppCompatActivity {

    private static final int SIGNATURE_BASE64_SIZE = 88;

    private Customer currentCustomer;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");

        //depois tirar isto
        this.currentCustomer.setShoppingCart(createProducts());

        RecyclerView recyclerView = findViewById(R.id.product_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ShoppingListAdapter(this, currentCustomer);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);

        TextView customerName = findViewById(R.id.customer_name);
        customerName.setText("Hello, " + this.currentCustomer.getName());

        /*TextView cardNumber = findViewById(R.id.current_payment_option);
        cardNumber.setText(this.currentCustomer.getPaymentInfo().getMaskedCardNumber("####xxxxxxxxxxxx"));*/

        ImageView pastTransactions = findViewById(R.id.past_transactions);
        pastTransactions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPastTransactionsDialog(view.getContext());
            }
        });

        FloatingActionButton checkoutButton = findViewById(R.id.checkout_button);
        if(this.currentCustomer.getShoppingCart().getProducts().size() > 0) {
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

    public void addProduct(String contents) {
        byte[] message = contents.getBytes(StandardCharsets.ISO_8859_1);
        byte[] signature = Arrays.copyOfRange(
                message, message.length - SIGNATURE_BASE64_SIZE, message.length
        );
        byte[] content = fromBase64(Arrays.copyOfRange(
                message, 0, message.length - SIGNATURE_BASE64_SIZE
        ));

        // Extracting Acme signature
        String acmeSig = encode(Arrays.copyOfRange(content, 0, 4));
        content = Arrays.copyOfRange(content, 4, content.length);

        // Extracting Product Code
        ByteBuffer buffer = ByteBuffer.wrap(content);
        String productCode = (new UUID(
                buffer.getLong(),
                buffer.getLong()
        )).toString();

        // Extracting Euros Price
        Integer euros = buffer.getInt();

        // Extracting Cents Price
        Integer cents = buffer.getInt();

        // Extracting Product Name
        byte[] prodNameBytes = new byte[buffer.get()];
        buffer.get(prodNameBytes);
        String prodName = encode(prodNameBytes);

        this.adapter.addProduct(
                new Product(productCode, prodName, euros, cents)
        );
    }

    public void updateCartValue() {
        TextView cartValue = findViewById(R.id.shopping_cart_value);
        cartValue.setText(String.format(Locale.US, "%.2f €", currentCustomer.getShoppingCartValue()));
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

    private void showPastTransactionsDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_past_transactions);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        RecyclerView recyclerView = dialog.findViewById(R.id.transaction_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(dialog.getContext());
        recyclerView.setLayoutManager(layoutManager);

        TransactionListAdapter adapter = new TransactionListAdapter(this, createTransactions());
        recyclerView.setAdapter(adapter);

        ImageButton closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dialogWidth = (int)(displayMetrics.widthPixels);
        int dialogHeight = (int)(displayMetrics.heightPixels * 0.90);
        dialog.getWindow().setLayout(dialogWidth, dialogHeight);

        dialog.show();
    }

    //precisa de desaparecer depois
    private ArrayList<Transaction> createTransactions() {
        ShoppingCart cart = new ShoppingCart();
        cart.addProduct(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz", 12, 50));
        cart.addProduct(new Product("4dadae03-06c6-4a18-9eed-38c8a34db686", "Arroz1", 15, 50));

        ArrayList<Transaction> transactions = new ArrayList<>();
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon("Coupon 1"), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon("Coupon 1"), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon("Coupon 1"), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon("Coupon 1"), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon("Coupon 1"), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon("Coupon 1"), false, new Date()));
        return transactions;
    }
}