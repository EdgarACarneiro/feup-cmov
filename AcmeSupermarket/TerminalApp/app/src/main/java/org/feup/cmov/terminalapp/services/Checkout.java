package org.feup.cmov.terminalapp.services;

import org.feup.cmov.terminalapp.interfaces.ResponseCallable;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class Checkout extends HttpClient implements Runnable {

    private String checkoutMsg;

    private ResponseCallable<Integer> onFinnish;

    public Checkout(String checkoutMsg, ResponseCallable<Integer> onFinnish) {
        super();
        this.checkoutMsg = checkoutMsg;
        this.onFinnish = onFinnish;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        int result = -1;

        try {
            url = new URL("http://" + address + "/checkout");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);

            byte[] msg = this.checkoutMsg.getBytes("ISO_8859_1");
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.write(msg, 0, msg.length);

            outputStream.flush();
            outputStream.close();

            // Handle response
            int responseCode = urlConnection.getResponseCode();
            if(responseCode == 200)
                result = Integer.parseInt(readStream(urlConnection.getInputStream()));
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