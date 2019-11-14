package org.feup.cmov.terminalapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.feup.cmov.terminalapp.R;
import org.feup.cmov.terminalapp.services.Checkout;

public class TerminalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        setContentView(R.layout.activity_main);

        Button scanButton = findViewById(R.id.scan_new_purchase);
        scanButton.setOnClickListener(view -> scanPurchase());

        findViewById(R.id.server_response_failure).setVisibility(View.GONE);
        findViewById(R.id.server_response_success).setVisibility(View.GONE);
    }

    private void handleCheckoutResponse(Boolean status) {
        if (status) {
            runOnUiThread(() -> {
                findViewById(R.id.server_response_failure).setVisibility(View.GONE);
                findViewById(R.id.server_response_success).setVisibility(View.VISIBLE);
            });
        } else {
            runOnUiThread(() -> {
                findViewById(R.id.server_response_success).setVisibility(View.GONE);
                findViewById(R.id.server_response_failure).setVisibility(View.VISIBLE);
            });
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 0 && resultCode == RESULT_OK) {
            String contents = data.getStringExtra("SCAN_RESULT");

            // Checkout request
            if (contents != null) {
                new Thread(new Checkout(contents,
                        response -> this.handleCheckoutResponse(response))
                ).start();

            }
        }
    }

    public void scanPurchase() {
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
}
