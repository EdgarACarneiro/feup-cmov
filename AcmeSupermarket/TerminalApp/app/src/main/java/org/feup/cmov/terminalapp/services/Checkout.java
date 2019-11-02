package org.feup.cmov.terminalapp.services;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Checkout extends HttpClient implements Runnable {

    /* Hardcoded for now */
    private String groceries = "0001;prod1-099,prod2-199,prod2-199;1;True";

    public Checkout(String baseAddress) {
        super(baseAddress);
    }

    @Override
    public void run() {
        System.out.println("STARTING THREAD");
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL("http://" + address + "/checkout");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            System.out.println(urlConnection.getOutputStream());
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            // First write content
            outputStream.writeBytes(this.groceries);

            // Write content signed
            // outputStream.write
            outputStream.flush();
            outputStream.close();

            // get response
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200) {
                System.out.println("MAN SERISLY WTF");
                String response = readStream(urlConnection.getInputStream());
                System.out.println("CONNECTION SUCCEEDED - RESPONSE: " + response);
            }
            else
                System.out.println("FAILED CONNECTION");
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