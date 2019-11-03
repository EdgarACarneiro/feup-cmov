package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;
import org.feup.cmov.acmecustomer.models.ShoppingCart;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Hashtable;

public class CheckoutActivity extends AppCompatActivity {
    private Customer currentCustomer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");

        generateQRCode();
    }

    private void generateQRCode() {
        QRCodeWriter writer = new QRCodeWriter();
        final String CHARACTER_SET = "ISO-8859-1";
        final String ANDROID_KEYSTORE = "AndroidKeyStore";
        final String keyname = "AcmeKey";

        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
            keyStore.load(null);
            KeyStore.Entry entry = keyStore.getEntry(keyname, null);
            PrivateKey key;

            if(entry != null) {
                key = ((KeyStore.PrivateKeyEntry)entry).getPrivateKey();

                Hashtable<EncodeHintType, String> hints = new Hashtable<>();
                hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);
                byte[] content = this.currentCustomer.getShoppingCart().encode(key);
                String string = new String(content, StandardCharsets.ISO_8859_1);

                BitMatrix bitMatrix = writer.encode(string, BarcodeFormat.QR_CODE, 512, 512, hints);
                int width = bitMatrix.getWidth();
                int height = bitMatrix.getHeight();
                Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }
                ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
