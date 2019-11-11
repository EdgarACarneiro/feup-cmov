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
import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.adapters.ShoppingListAdapter;
import org.feup.cmov.acmecustomer.adapters.TransactionListAdapter;
import org.feup.cmov.acmecustomer.models.Coupon;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;
import org.feup.cmov.acmecustomer.models.ShoppingCart;
import org.feup.cmov.acmecustomer.models.Transaction;
import org.feup.cmov.acmecustomer.services.KeyStoreHandler;
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.nio.charset.StandardCharsets;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import static org.feup.cmov.acmecustomer.Utils.concaByteArrays;
import static org.feup.cmov.acmecustomer.Utils.fromBase64;
import static org.feup.cmov.acmecustomer.Utils.toBase64;

public class MainMenuActivity extends AppCompatActivity {

    private static final int SIGNATURE_BASE64_SIZE = 88;

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

        TextView couponMessage = findViewById(R.id.new_coupon);
        couponMessage.setVisibility(View.GONE);

        ImageView pastTransactions = findViewById(R.id.past_transactions);
        pastTransactions.setOnClickListener(view -> showPastTransactionsDialog(view.getContext()));

        FloatingActionButton checkoutButton = findViewById(R.id.checkout_button);
        if(this.currentCustomer.getShoppingCart().getProducts().size() > 0) {
            checkoutButton.show();
        } else {
            checkoutButton.hide();
        }
        checkoutButton.setOnClickListener(view -> checkout());

        FloatingActionButton addProductButton = findViewById(R.id.add_new_item_button);
        addProductButton.setOnClickListener(view -> scanProduct());

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                int productAmount = currentCustomer.getShoppingCart().getProducts().size();
                if(newState == RecyclerView.SCROLL_STATE_IDLE) {
                    if(productAmount > 0) checkoutButton.show();
                    if(productAmount >= 0 && productAmount < 10) addProductButton.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if(dy != 0 || recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    checkoutButton.hide();
                    addProductButton.hide();
                }
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
        byte[] message = contents.getBytes(StandardCharsets.ISO_8859_1);
        byte[] signature = Arrays.copyOfRange(
                message, message.length - SIGNATURE_BASE64_SIZE, message.length
        );
        byte[] content = Arrays.copyOfRange(
                message, 0, message.length - SIGNATURE_BASE64_SIZE
        );

        // Todo - Refactor
        // Signature Validation
        boolean verified = false;
        try {
                Signature sign = Signature.getInstance("SHA256withRSA");
                sign.initVerify(
                        KeyStoreHandler.getKeyFromBytes(
                                LocalStorage.getAcmePublicKey(
                                        this.getApplicationContext())));
                sign.update(content);
                verified = sign.verify(signature);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        if (!verified) {
            System.out.println("NOT working");
            // Todo - Handle error
            // return;
        }

        this.adapter.addProduct(
                new Product(fromBase64(content))
        );
    }

    public void updateCartValue() {
        TextView cartValue = findViewById(R.id.shopping_cart_value);
        TextView couponMessage = findViewById(R.id.new_coupon);
        cartValue.setText(String.format(Locale.US, "%.2f €", currentCustomer.getShoppingCartValue()));
        if(currentCustomer.getShoppingCartValue() >= 100.0) {
            couponMessage.setVisibility(View.VISIBLE);
        } else {
            couponMessage.setVisibility(View.GONE);
        }
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
        closeButton.setOnClickListener(view -> dialog.cancel());

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
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon(1), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon(2), true, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon(2), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon(3), true, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon(3), false, new Date()));
        transactions.add(new Transaction(this.currentCustomer, cart, new Coupon(4), false, new Date()));
        return transactions;
    }

    private void requestCoupons() {
        // Building up message
        byte[] uuid = toBase64(Utils.decode(
                LocalStorage.getCurrentUuid(this.getApplicationContext())
        ));
        System.out.println(toBase64(this.currentCustomer.signMsg(uuid)).length);
        byte[] content = concaByteArrays(uuid, toBase64(this.currentCustomer.signMsg(uuid)));

        // Triggering vouchers request to server
        new Thread(new GetTransactions(content, (response) -> {
            if (response != null) {
                this.couponsList = new ArrayList<>();

                for (int voucherId: response.getVouchers()) {
                    couponsList.add(new Coupon(voucherId));
                }
                runOnUiThread(() -> initializeCouponsDropdown());

            } else {
                // Failed Registration - TODO - Detailed Error from Server
                System.out.println(":::::: FAILED GETTING COUPONS :::::::");
            }
        })).start();
    }

}