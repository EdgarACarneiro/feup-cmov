package org.feup.cmov.terminalapp.services;

import org.feup.cmov.terminalapp.interfaces.ResponseCallable;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

public class Checkout extends HttpClient implements Runnable {

    private String checkoutMsg;

    private ResponseCallable<Boolean> onFinnish;

    public Checkout(String baseAddress, String checkoutMsg, ResponseCallable<Boolean> onFinnish) {
        super(baseAddress);
        this.checkoutMsg = checkoutMsg;
        this.onFinnish = onFinnish;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        boolean result = false;

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

            byte[] msg = this.checkoutMsg.getBytes("ISO_8859_1");
            System.out.println(Arrays.toString(msg)); // TODO-Delete
            outputStream.write(msg, 0, msg.length);

            outputStream.flush();
            outputStream.close();

            // Handle response
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200)
                result = true;
        }
        catch (Exception e) {
            System.out.println(e.fillInStackTrace());
        }
        finally {
            if(urlConnection != null)
                urlConnection.disconnect();

            this.onFinnish.call(result);
        }
    }
}