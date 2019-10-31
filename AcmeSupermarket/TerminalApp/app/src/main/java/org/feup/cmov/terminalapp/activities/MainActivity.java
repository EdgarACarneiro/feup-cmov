package org.feup.cmov.terminalapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import org.feup.cmov.terminalapp.R;
import org.feup.cmov.terminalapp.services.Checkout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickCheckout(View view) {
        Checkout checkout = new Checkout("127.0.0.1:5000");
        Thread thr = new Thread(checkout);
        thr.start();
    }
}
