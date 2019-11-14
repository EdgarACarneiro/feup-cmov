package org.feup.cmov.acmecustomer.services;

import android.content.Context;

import com.google.gson.Gson;

import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.interfaces.ResponseCallable;
import org.feup.cmov.acmecustomer.models.Customer;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class GetTransactions extends HttpClient implements Runnable {

    public class ServerTransaction {
        private String date;
        private Integer total;
        private Integer discounted;
        private boolean voucher;

        public String getDate() {
            return date;
        }

        public Integer getDiscounted() {
            return discounted;
        }

        public Integer getTotal() {
            return total;
        }

        public boolean usedVoucher() {
            return voucher;
        }
    }

    public class GetTransactionResponse {

        private ArrayList<ServerTransaction> transactions;

        public ArrayList<ServerTransaction> getTransactions() {
            return this.transactions;
        }
    }

    private byte[] content;

    private Customer customer;

    private Context context;

    private ResponseCallable<GetTransactionResponse> onFinish;

    public GetTransactions(byte[] content, Customer customer, Context context, ResponseCallable<GetTransactionResponse> onFinish) {
        super();
        this.content = content;
        this.customer = customer;
        this.context = context;
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
                byte[] content = customer.getSignedServerMsgContent(
                        readStream(urlConnection.getInputStream()).getBytes(StandardCharsets.ISO_8859_1),
                        this.context
                );

                if (content != null) {
                    response = (new Gson()).fromJson(
                            Utils.encode(Utils.fromBase64(content)),
                            GetTransactionResponse.class
                    );
                }
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
