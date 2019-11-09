package org.feup.cmov.acmecustomer.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.interfaces.ResponseCallable;
import org.feup.cmov.acmecustomer.models.Customer;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends HttpClient implements Runnable {

    public class RegisterResponse {

        private String uuid;

        public String public_key;

        public byte[] getPublicKey() {
            System.out.println("Print public key");
            System.out.println(public_key);
            return Utils.decode(public_key);
        }

        public String getUuid() {
            return this.uuid;
        }
    }

    private Customer customer;

    private ResponseCallable<RegisterResponse> onFinish;

    public Register(Customer customer, ResponseCallable<RegisterResponse> onFinish) {
        super();
        this.customer = customer;
        this.onFinish = onFinish;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        RegisterResponse response = null;

        try {
            url = new URL("http://" + address + "/auth/register");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());

            // Write Customer as json
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            outputStream.writeBytes(gson.toJson(this.customer, Customer.class));
            outputStream.flush();
            outputStream.close();

            // Get response
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 201) {
                response = (new Gson()).fromJson(
                        readStream(urlConnection.getInputStream()),
                        RegisterResponse.class
                );
            }
        } catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        } finally {
            if (urlConnection != null)
                urlConnection.disconnect();

            this.onFinish.call(response);
        }
    }
}