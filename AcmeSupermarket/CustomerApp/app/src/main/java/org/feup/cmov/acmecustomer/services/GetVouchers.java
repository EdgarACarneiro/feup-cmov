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

public class GetVouchers extends HttpClient implements Runnable {

    public class GetVouchersResponse {

        private ArrayList<Integer> vouchers;

        private int discounted;

        public ArrayList<Integer> getVouchers() {
            return this.vouchers;
        }

        public int getDiscounted() {
            return discounted;
        }
    }

    private byte[] content;

    private Customer customer;

    private Context context;

    private ResponseCallable<GetVouchersResponse> onFinish;

    public GetVouchers(byte[] content, Customer customer, Context context, ResponseCallable<GetVouchersResponse> onFinish) {
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
        GetVouchersResponse response = null;

        try {
            url = new URL("http://" + address + "/get-vouchers");
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
                            GetVouchersResponse.class
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
