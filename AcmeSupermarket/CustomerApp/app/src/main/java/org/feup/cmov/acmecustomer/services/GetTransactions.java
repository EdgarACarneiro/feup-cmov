package org.feup.cmov.acmecustomer.services;

import com.google.gson.Gson;

import org.feup.cmov.acmecustomer.interfaces.ResponseCallable;
import org.feup.cmov.acmecustomer.models.Transaction;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetTransactions extends HttpClient implements Runnable {

    public class GetTransactionResponse {

        private ArrayList<Transaction> transactions;

        public ArrayList<Transaction> getTransactions() {
            return this.transactions;
        }
    }

    private byte[] content;

    private ResponseCallable<GetTransactionResponse> onFinish;

    public GetTransactions(byte[] content, ResponseCallable<GetTransactionResponse> onFinish) {
        super();
        this.content = content;
        this.onFinish = onFinish;
    }

    @Override
    public void run() {
        URL url;
        HttpURLConnection urlConnection = null;
        GetTransactionResponse response = null;

        try {
            url = new URL("http://" + address + "/get-transactions");
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setUseCaches(false);
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());

            // Write vouchers request message
            outputStream.write(content, 0, content.length);
            outputStream.flush();
            outputStream.close();

            // Get response
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == 200) {
                response = (new Gson()).fromJson(
                        readStream(urlConnection.getInputStream()),
                        GetTransactionResponse.class
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
