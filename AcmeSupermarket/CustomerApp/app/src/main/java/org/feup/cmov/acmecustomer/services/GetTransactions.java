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
        private String d;
        private Integer t;
        private Integer di;
        private boolean v;

        public String getDate() {
            return d;
        }

        public Integer getDiscounted() {
            return di;
        }

        public Integer getTotal() {
            return t;
        }

        public boolean usedVoucher() {
            return v;
        }
    }

    public class GetTransactionResponse {

        private ArrayList<ServerTransaction> transactions;

        public GetTransactionResponse() {
            transactions = new ArrayList<>();
        }

        public ArrayList<ServerTransaction> getTransactions() {
            return this.transactions;
        }

        public void addTransaction(ServerTransaction transaction) {
            this.transactions.add(transaction);
        }
    }

    private class EncodedResponse {

        private ArrayList<String> transactions;

        public ArrayList<String> getTransactions() {
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
                byte[] encodedContent = customer.getSignedServerMsgContent(
                        readStream(urlConnection.getInputStream()).getBytes(StandardCharsets.ISO_8859_1),
                        this.context
                );

                // Signature Verified
                if (encodedContent != null) {
                    EncodedResponse encodedResponse = new Gson().fromJson(
                            Utils.encode(Utils.fromBase64(encodedContent)),
                            EncodedResponse.class
                    );
                    response = new GetTransactionResponse();

                    // Handling array of encoded past transactions
                    for (String transaction: encodedResponse.getTransactions()) {
                        byte[] decryptedTransaction = customer.decryptMsg(
                                Utils.fromBase64(Utils.decode(transaction))
                        );

                        if (decryptedTransaction != null)
                            response.addTransaction(new Gson().fromJson(
                                    Utils.encode(decryptedTransaction),
                                    ServerTransaction.class
                            ));
                    }
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
