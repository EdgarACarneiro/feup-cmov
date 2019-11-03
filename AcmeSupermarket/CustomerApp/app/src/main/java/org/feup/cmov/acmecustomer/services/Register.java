package org.feup.cmov.acmecustomer.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.feup.cmov.acmecustomer.models.Customer;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Register extends HttpClient implements Runnable {

    private Customer customer;

    public Register(Customer customer) {
        super();
        this.customer = customer;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;

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
            if(responseCode == 200) {
                String response = readStream(urlConnection.getInputStream());
                System.out.println("CONNECTION SUCCEEDED - RESPONSE: " + response);
            }
            else
                System.out.println("FAILED REQUEST");
        }
        catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();
        }
    }
}