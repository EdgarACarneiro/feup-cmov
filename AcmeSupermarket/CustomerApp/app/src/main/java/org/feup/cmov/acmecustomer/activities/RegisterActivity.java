package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.PaymentInfo;

import java.util.Hashtable;


public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button submitButton = findViewById(R.id.submit_button);
        submitButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onFinishedRegistration();
                    }
                }
        );
        //this.generateQRCode();
    }

    protected void onFinishedRegistration() {
        String name = ((EditText)findViewById(R.id.input_name)).getText().toString();
        String username = ((EditText)findViewById(R.id.input_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.input_password)).getText().toString();

        double cardNumber = Double.parseDouble(((EditText)findViewById(R.id.input_card_number)).getText().toString());
        String cardHolder = ((EditText)findViewById(R.id.input_card_holder)).getText().toString();
        int cardMonth = Integer.parseInt(((EditText)findViewById(R.id.input_card_expiration_month)).getText().toString());
        int cardYear = Integer.parseInt(((EditText)findViewById(R.id.input_card_expiration_year)).getText().toString());
        int cvv = Integer.parseInt(((EditText)findViewById(R.id.input_cvv)).getText().toString());

        Customer newCustomer = new Customer(name,
                                            username,
                                            password,
                                            new PaymentInfo(cardNumber, cardHolder, cardMonth, cardYear, cvv));

        TextView errorMessage = findViewById(R.id.error_message);
        errorMessage.setText(newCustomer.getMetadata().getKeyPair().getPublic().toString());
        errorMessage.setVisibility(View.VISIBLE);

        System.out.println(newCustomer.toString());
    }

    /*private void generateQRCode() {
        QRCodeWriter writer = new QRCodeWriter();
        final static String CHARACTER_SET = "ISO-8859-1";

        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, CHARACTER_SET);

        try {
            BitMatrix bitMatrix = writer.encode("teste", BarcodeFormat.QR_CODE, 512, 512, hints);
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
            //((ImageView) findViewById(R.id.img_result_qr)).setImageBitmap(bmp);

        } catch (WriterException e) {
            e.printStackTrace();
        }
    }*/

}
