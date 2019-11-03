package org.feup.cmov.acmecustomer.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.feup.cmov.acmecustomer.interfaces.ResponseCallable;
import org.feup.cmov.acmecustomer.models.Customer;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends HttpClient implements Runnable {

    private Customer customer;

    private ResponseCallable onFinish;

    public Register(Customer customer, ResponseCallable onFinish) {
        super();
        this.customer = customer;
        this.onFinish = onFinish;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        String response = null;

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
            if(responseCode == 201) {
                response = readStream(urlConnection.getInputStream());
                System.out.println("CONNECTION SUCCEEDED - RESPONSE: " + response);
            }
            else
                System.out.println("FAILED RREGISTER");
        }
        catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();

            this.onFinish.call(response);
        }
    }
}