package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.adapters.ShoppingListAdapter;
import org.feup.cmov.acmecustomer.models.Coupon;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.services.GetVouchers;
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.util.ArrayList;
import java.util.Locale;

import static org.feup.cmov.acmecustomer.Utils.concaByteArrays;
import static org.feup.cmov.acmecustomer.Utils.toBase64;

public class TransactionConfirmationActivity extends AppCompatActivity {

    public static final String NO_COUPON_AVAILABLE = "No vouchers available";
    public static final String NO_COUPON_SELECTED = "No voucher selected";

    private Customer currentCustomer;

    private ArrayList<Coupon> couponsList;

    private int availableDiscont;

    int selectedCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_confirmation);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");

        // Initializing the coupons as empty for now & make request
        couponsList = new ArrayList<>();
        initializeCouponsDropdown();
        initializeDiscont();
        requestCoupons();

        ((TextView) findViewById(R.id.subtotal_value)).setText(String.format(Locale.US, "%.2f €", currentCustomer.getShoppingCartValue()));

        RecyclerView recyclerView = findViewById(R.id.product_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ShoppingListAdapter mAdapter = new ShoppingListAdapter(this, currentCustomer);
        recyclerView.setAdapter(mAdapter);

        ImageView cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(view -> goBack());

        ImageView confirmButton = findViewById(R.id.confirm);
        confirmButton.setOnClickListener(view -> checkout());
    }

    private void initializeCouponsDropdown() {
        Spinner coupons = findViewById(R.id.coupon_dropdown);
        ArrayAdapter<String> couponAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                getCouponDescriptions()
        );
        couponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coupons.setAdapter(couponAdapter);
        coupons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedCoupon = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                selectedCoupon = 0;
            }
        });

        initializeDiscont();
    }

    private void initializeDiscont() {
        CheckBox customerWantsDiscount = findViewById(R.id.discount);
        customerWantsDiscount.setOnCheckedChangeListener((CompoundButton buttonView, boolean isChecked) -> {
            if (isChecked) {
                double subtotal = currentCustomer.getShoppingCartValue();
                double discount = this.availableDiscont;
                if (selectedCoupon > 0)
                    discount += subtotal * 0.15;
                discount *= 0.01;

                if (discount > 0) {
                    ((TextView) findViewById(R.id.subtotal_value)).setText(String.format(Locale.US, "%.2f €", subtotal));
                    ((TextView) findViewById(R.id.discount_value)).setText(String.format(Locale.US, "%.2f €", -discount));
                    ((TextView) findViewById(R.id.total_value)).setText(String.format(Locale.US, "%.2f €", subtotal - discount));
                    findViewById(R.id.coupon_discount_info).setVisibility(View.VISIBLE);
                }
            } else {
                ((TextView) findViewById(R.id.total_value)).setText(String.format(Locale.US, "%.2f €", currentCustomer.getShoppingCartValue()));
                findViewById(R.id.coupon_discount_info).setVisibility(View.GONE);
            }
        });

    }

    private void requestCoupons() {
        // Building up message
        byte[] uuid = toBase64(Utils.decode(
                LocalStorage.getCurrentUuid(this.getApplicationContext())
        ));
        System.out.println(toBase64(this.currentCustomer.signMsg(uuid)).length);
        byte[] content = concaByteArrays(uuid, toBase64(this.currentCustomer.signMsg(uuid)));

        // Triggering vouchers request to server
        new Thread(new GetVouchers(content, (response) -> {
            if (response != null) {
                this.couponsList = new ArrayList<>();

                for (int voucherId: response.getVouchers()) {
                    couponsList.add(new Coupon(voucherId));
                }
                couponsList.add(new Coupon(0)); // TODO DELETE
                availableDiscont = response.getDiscounted();
                availableDiscont = 372; // TODO DELETE
                runOnUiThread(() -> initializeCouponsDropdown());

            } else {
                // Failed Registration - TODO - Detailed Error from Server
                System.out.println(":::::: FAILED GETTING COUPONS :::::::");
            }
        })).start();
    }


    public String[] getCouponDescriptions() {
        String[] descriptions = new String[couponsList.size() + 1];
        if(couponsList.size() == 0) {
            descriptions[0] = NO_COUPON_AVAILABLE;
        } else {
            descriptions[0] = NO_COUPON_SELECTED;
        }
        for(int i = 0; i < couponsList.size(); i++) {
            descriptions[i+1] = couponsList.get(i).getDescription();
        }
        return descriptions;
    }

    public void goBack() {
        super.finish();
    }

    public void checkout() {
        CheckBox customerWantsDiscount = findViewById(R.id.discount);

        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("Customer", this.currentCustomer);
        intent.putExtra("Discount", customerWantsDiscount.isChecked());
        intent.putExtra("Coupon",
                (selectedCoupon == 0? -1: couponsList.get(selectedCoupon - 1).getId())
        );

        startActivity(intent);
    }
}
