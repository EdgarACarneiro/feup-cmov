package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.util.Base64;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.feup.cmov.acmecustomer.Constants;
import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.Product;
import org.feup.cmov.acmecustomer.models.ShoppingCart;
import org.feup.cmov.acmecustomer.services.KeyStoreHandler;
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.UUID;

import static org.feup.cmov.acmecustomer.Utils.concaByteArrays;
import static org.feup.cmov.acmecustomer.Utils.toBase64;

public class CheckoutActivity extends AppCompatActivity {

    private static final int CHECKOUT_MSG_BASE_SIZE = 4 + 36 + 1 + 4;

    private Customer currentCustomer;

    private Boolean discount;

    private Integer voucherID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        this.currentCustomer = (Customer) getIntent().getSerializableExtra("Customer");
        this.discount = (Boolean) getIntent().getSerializableExtra("Discount");
        this.voucherID = (Integer) getIntent().getSerializableExtra("Coupon");

        generateQRCode();
    }

    private Bitmap encodeAsBitmap(String str) throws WriterException {
        final String CHARACTER_SET = "ISO_8859_1";
        int DIMENSION = 1000;
        BitMatrix result;

        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION, hints);
        }
        catch (IllegalArgumentException iae) {
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary) : Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }

    private void generateQRCode() {
        ArrayList<Product> products = this.currentCustomer.getShoppingCart().getProducts();
        ByteBuffer buffer = ByteBuffer.allocate(
                CHECKOUT_MSG_BASE_SIZE + products.size() * Product.CHECKOUT_MSG_SIZE
        );

        // Loading Acme Tag and UUID
        buffer.putInt(Constants.ACME_TAG_ID);
        System.out.println("Acme Tag: " + toBase64(Arrays.copyOfRange(buffer.array(), 0, 4)).length);
        UUID uuid = UUID.fromString(LocalStorage.getCurrentUuid(this.getApplicationContext()));
        System.out.println(LocalStorage.getCurrentUuid(this.getApplicationContext()));
        // buffer.putLong(uuid.getMostSignificantBits());
        // buffer.putLong(uuid.getLeastSignificantBits());
        buffer.put(Utils.decode(
                LocalStorage.getCurrentUuid(this.getApplicationContext())
        ));

        // Loading Products
        for (Product prod: products)
            buffer.put(prod.getProductAsBytes());

        // Loading Voucher choice and discount choice
        buffer.putInt(voucherID);
        buffer.put((byte) (discount? 1: 0));

        // Signing everything
        byte[] msg = toBase64(buffer.array());
        System.out.println("Acme Tag: " + toBase64(this.currentCustomer.signMsg(msg)).length);
        byte[] content = concaByteArrays(msg, toBase64(this.currentCustomer.signMsg(msg)));

        /*byte[] uuid = Utils.decode(
                LocalStorage.getCurrentUuid(this.getApplicationContext())
        );
        byte[] items = this.currentCustomer.getShoppingCart().getAsBytes();
        byte[] msg = concaByteArrays(uuid, items);
        System.out.println(LocalStorage.getCurrentUuid(this.getApplicationContext()));
        System.out.println(Base64.encodeToString(uuid, Base64.DEFAULT));

        byte[] discountByte = new byte[1];
        discountByte[0] = (byte) (discount? 1 : 0);
        msg = concaByteArrays(msg, discountByte);

        byte[] voucherBytes = ByteBuffer.allocate(4).putInt(voucherID).array();
        msg = toBase64(concaByteArrays(msg, voucherBytes));

        // Signing everything
        Log.d("String", Base64.encodeToString(this.currentCustomer.signMsg(msg), Base64.DEFAULT));
        Log.d("Size", Integer.toString(Base64.encodeToString(this.currentCustomer.signMsg(msg), Base64.DEFAULT).length()));

        byte[] content = concaByteArrays(msg, toBase64(this.currentCustomer.signMsg(msg)));
        System.out.println(Arrays.toString(content));*/

        // As QRCode message
        String string = new String(content, StandardCharsets.ISO_8859_1);
        System.out.println(string);

        try {
            ((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(
                    encodeAsBitmap(string)
            );
        } catch (WriterException e) {
            e.printStackTrace();
        }

    }
}
