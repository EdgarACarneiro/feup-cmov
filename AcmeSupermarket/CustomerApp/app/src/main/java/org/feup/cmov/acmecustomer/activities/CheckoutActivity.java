package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;
import org.feup.cmov.acmecustomer.models.ShoppingCart;
import org.feup.cmov.acmecustomer.services.KeyStoreHandler;
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;

public class CheckoutActivity extends AppCompatActivity {

    private Customer currentCustomer;

    private Boolean discount;

    private Integer voucherID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");
        this.discount = (Boolean) getIntent().getSerializableExtra("Discount");
        // this.voucherID = (Integer) getIntent().getSerializableExtra("Coupon");

        generateQRCode();
    }

    private byte[] concaByteArrays(byte[] a, byte[] b) {
        byte[] res = new byte[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);

        return res;
    }

    private void generateQRCode() {
        QRCodeWriter writer = new QRCodeWriter();
        // final String CHARACTER_SET = "UTF8";
        final String CHARACTER_SET = "ISO_8859_1";

        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<>();
            hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);

            // Constructing QRCode message
            byte[] uuid = Utils.decode(
                    LocalStorage.getCurrentUuid(this.getApplicationContext())
            );
            System.out.println(uuid.length);
            byte[] items = this.currentCustomer.getShoppingCart().getAsBytes();
            byte[] msg = concaByteArrays(uuid, items);

            byte[] discountByte = new byte[1];
            discountByte[0] = (byte) (discount? 1 : 0);
            msg = concaByteArrays(msg, discountByte);

            byte[] voucherBytes = ByteBuffer.allocate(4).putInt(voucherID).array();
            msg = concaByteArrays(msg, voucherBytes);

            // Signing everything
            byte[] content = concaByteArrays(msg, this.currentCustomer.signMsg(msg));
            System.out.println(Arrays.toString(content));

            // As QRCode message
            String string = new String(content, StandardCharsets.ISO_8859_1);
            // String string = Utils.encode(content);
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
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
