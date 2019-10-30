package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.PaymentInfo;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView login = findViewById(R.id.have_an_account);
        login.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        changeToLoginScreen();
                    }
                }
        );

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

        double cardNumber = parseDouble(((EditText)findViewById(R.id.input_card_number)).getText().toString());
        String cardHolder = ((EditText)findViewById(R.id.input_card_holder)).getText().toString();
        int cardMonth = parseInt(((EditText)findViewById(R.id.input_card_expiration_month)).getText().toString());
        int cardYear = parseInt(((EditText)findViewById(R.id.input_card_expiration_year)).getText().toString());
        int cvv = parseInt(((EditText)findViewById(R.id.input_cvv)).getText().toString());

        if(noErrorsOnRegistration()) {
            TextView errorMessage = findViewById(R.id.error_message);
            errorMessage.setText("");
            errorMessage.setVisibility(View.GONE);

            //missing REST call to register
            Customer newCustomer = new Customer(name,
                    username,
                    password,
                    new PaymentInfo(cardNumber, cardHolder, cardMonth, cardYear, cvv));

            /*Intent intent = new Intent(this, MainScreen.class);
            startActivity(intent);*/
        } else {
            TextView errorMessage = findViewById(R.id.error_message);
            errorMessage.setText("Please verify the data before submit!");
            errorMessage.setVisibility(View.VISIBLE);
        }
    }

    protected void changeToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    protected boolean noErrorsOnRegistration() {
        String name = ((EditText)findViewById(R.id.input_name)).getText().toString();
        String username = ((EditText)findViewById(R.id.input_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.input_password)).getText().toString();
        String cardNumber = ((EditText)findViewById(R.id.input_card_number)).getText().toString();
        String cardHolder = ((EditText)findViewById(R.id.input_card_holder)).getText().toString();
        int cardMonth = parseInt(((EditText)findViewById(R.id.input_card_expiration_month)).getText().toString());
        //int cardYear = Integer.parseInt(((EditText)findViewById(R.id.input_card_expiration_year)).getText().toString());
        //int cvv = Integer.parseInt(((EditText)findViewById(R.id.input_cvv)).getText().toString());

        return name.length() > 3 && username.length() > 3 && password.length() >= 5
                && cardNumber.length() == 16 && cardHolder.length() > 3
                && cardMonth > 0 && cardMonth <= 12;
    }

    private double parseDouble(String s){
        if(s == null || s.isEmpty())
            return 0.0;
        else
            return Double.parseDouble(s);
    }

    private int parseInt(String s){
        if(s == null || s.isEmpty())
            return 0;
        else
            return Integer.parseInt(s);
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
