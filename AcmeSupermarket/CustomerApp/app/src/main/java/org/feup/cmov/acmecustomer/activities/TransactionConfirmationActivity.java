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
import android.widget.ImageView;
import android.widget.Spinner;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.adapters.ShoppingListAdapter;
import org.feup.cmov.acmecustomer.models.Coupon;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;

import java.util.ArrayList;

public class TransactionConfirmationActivity extends AppCompatActivity {
    private Customer currentCustomer;
    int selectedCoupon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_confirmation);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");
        this.currentCustomer.setShoppingCart(this.createProducts());

        Spinner coupons = findViewById(R.id.coupon_dropdown);
        ArrayAdapter<String> couponAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                getCouponDescriptions(createCoupons())
        );
        couponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        coupons.setAdapter(couponAdapter);

        RecyclerView recyclerView = findViewById(R.id.product_list);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        ShoppingListAdapter mAdapter = new ShoppingListAdapter(currentCustomer.getShoppingCart().getProducts());
        recyclerView.setAdapter(mAdapter);

        ImageView cancelButton = findViewById(R.id.cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        ImageView confirmButton = findViewById(R.id.confirm);
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout();
            }
        });
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

    public ArrayList<Coupon> createCoupons() {
        ArrayList<Coupon> coupons = new ArrayList<>();
        coupons.add(new Coupon("Coupon 1"));
        coupons.add(new Coupon("Coupon 2"));
        coupons.add(new Coupon("Coupon 3"));
        return coupons;
    }

    public String[] getCouponDescriptions(ArrayList<Coupon> coupons) {
        String[] descriptions = new String[coupons.size() + 1];
        if(coupons.size() == 0) {
            descriptions[0] = "No coupons available";
        } else {
            descriptions[0] = "No coupon selected";
        }
        for(int i = 0; i < coupons.size(); i++) {
            descriptions[i+1] = coupons.get(i).getDescription();
        }
        return descriptions;
    }

    public void goBack() {
        super.finish();
    }

    public void checkout() {
        CheckBox customerWantsDiscount = findViewById(R.id.discount);
        Spinner coupons = findViewById(R.id.coupon_dropdown);

        Intent intent = new Intent(this, CheckoutActivity.class);
        intent.putExtra("Customer", this.currentCustomer);
        intent.putExtra("Discount", customerWantsDiscount.isChecked());
        intent.putExtra("Coupon", getCouponDescriptions(createCoupons())[coupons.getSelectedItemPosition()]);

        startActivity(intent);
    }
}
