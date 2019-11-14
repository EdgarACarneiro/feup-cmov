package org.feup.cmov.acmecustomer.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import org.feup.cmov.acmecustomer.R;
import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.models.Customer;
import org.feup.cmov.acmecustomer.models.PaymentInfo;
import org.feup.cmov.acmecustomer.services.LocalStorage;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        TextView login = findViewById(R.id.dont_have_account);
        login.setOnClickListener(view -> changeToRegisterScreen());

        Button loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(view -> onLogin());
    }

    protected void changeToRegisterScreen() {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void showError(String errorMsg) {
        TextView errorMessage = findViewById(R.id.error_message);
        errorMessage.setText(errorMsg);
        errorMessage.setVisibility(View.VISIBLE);
    }

    private void clearError() {
        TextView errorMessage = findViewById(R.id.error_message);
        errorMessage.setText("");
        errorMessage.setVisibility(View.GONE);
    }

    protected void onLogin() {
        /*
        // Validating input errors
        if (!noErrorsOnLogin()) {
            showError("Please verify the data before submit!");
            return;
        }

        String username = ((EditText) findViewById(R.id.input_username)).getText().toString();
        String password = ((EditText) findViewById(R.id.input_password)).getText().toString();
        String customerUuid = LocalStorage.read(this.getApplicationContext(), username + "_uuid");

        // Validating if customer exists
        if (customerUuid == null) {
            showError("The given User did not register on this phone.");
            return;
        }

        // Validating password
        Customer customer = (new Gson()).fromJson(
                LocalStorage.read(this.getApplicationContext(), customerUuid),
                Customer.class
        );
        if (!customer.verifyPassword(password))
            showError("Wrong password!");
        else {
            clearError();

            // Validated User
            LocalStorage.setCurrentUuid(this.getApplicationContext, customerUuid);
            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("Customer", customer);
            startActivity(intent);
        }
        */

        // PREVIOUS CODE
        if (noErrorsOnLogin()) {
            clearError();

            //TODO: Missing load of local info
            Customer newCustomer = new Customer("Teste",
                    "teste",
                    "teste",
                    new PaymentInfo("1111222233334444", "teste", 12 , 21, 111));

            newCustomer.getMetadata().generateKeyPair(this);

            Intent intent = new Intent(this, MainMenuActivity.class);
            intent.putExtra("Customer", newCustomer);
            startActivity(intent);
        } else {
            //showError("Please verify the data before submit!");
            Utils.showErrorUI(findViewById(R.id.login_root), "Please verify the data before submit!");
        }
    }

    protected boolean noErrorsOnLogin() {
        String username = ((EditText)findViewById(R.id.input_username)).getText().toString();
        String password = ((EditText)findViewById(R.id.input_password)).getText().toString();

        return username.length() > 3 && password.length() >= 5;
    }
}
