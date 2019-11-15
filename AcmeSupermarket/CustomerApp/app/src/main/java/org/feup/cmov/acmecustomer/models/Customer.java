package org.feup.cmov.acmecustomer.models;

import android.content.Context;

import com.google.gson.annotations.Expose;

import org.feup.cmov.acmecustomer.Constants;
import org.feup.cmov.acmecustomer.Utils;
import org.feup.cmov.acmecustomer.services.KeyStoreHandler;
import org.feup.cmov.acmecustomer.services.LocalStorage;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;
import java.util.Arrays;

import javax.crypto.Cipher;

public class Customer implements Serializable {

    private static final int SIGNATURE_BASE64_SIZE = 88;
    @Expose
    private CustomerMetadata metadata;
    @Expose
    private PaymentInfo paymentInfo;

    private ShoppingCart currentCart;

    public Customer(String name, String username, String password, PaymentInfo paymentInfo) {
        this.metadata = new CustomerMetadata(name, username, password);
        this.paymentInfo = paymentInfo;
        this.currentCart = new ShoppingCart();
    }

    public CustomerMetadata getMetadata() {
        return this.metadata;
    }

    public String getName() {
        return this.metadata.getName();
    }

    public String getUsername() {
        return this.metadata.getUsername();
    }

    public boolean verifyPassword(String password) {
        return this.metadata.getPassword() == password;
    }

    public PaymentInfo getPaymentInfo() {
        return this.paymentInfo;
    }

    public double getShoppingCartValue() {
        return this.currentCart.getValue();
    }

    public ShoppingCart getShoppingCart() {
        return this.currentCart;
    }

    public void clearShoppingCart() {
        this.currentCart.clearProducts();
    }

    public void setShoppingCart(ArrayList<Product> products) {
        this.currentCart.setProducts(products);
    }

    public byte[] signMsg(byte[] msg) {
        PrivateKey pk = KeyStoreHandler.getUserPrivateKey(this.metadata.getUsername());
        if (pk == null) {
            System.err.println("User does not have a private key!");
            return null;
        }

        try {
            Signature sig = Signature.getInstance("SHA256WithRSA");
            sig.initSign(pk);
            sig.update(msg);
            return sig.sign();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public byte[] getSignedServerMsgContent(byte[] message, Context context) {
        byte[] signature = Utils.fromBase64(Arrays.copyOfRange(
                message, message.length - SIGNATURE_BASE64_SIZE, message.length
        ));
        byte[] content = Arrays.copyOfRange(
                message, 0, message.length - SIGNATURE_BASE64_SIZE
        );

        boolean verified = false;
        try {
            Signature sign = Signature.getInstance("SHA256withRSA");
            sign.initVerify(
                    KeyStoreHandler.getKeyFromBytes(
                            LocalStorage.getAcmePublicKey(context)
                    ));
            sign.update(content);
            verified = sign.verify(signature);
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return verified? content: null;
    }

    public byte[] decryptMsg(byte[] message) {
        try {
            Cipher cipher = Cipher.getInstance("RSA/NONE/PKCS1Padding");
            cipher.init(Cipher.DECRYPT_MODE, KeyStoreHandler.getUserPrivateKey(this.getUsername()));
            return cipher.doFinal(message);
        }
        catch (Exception e) {
            System.err.println("Failed to decrypt message");
            return null;
        }
    }

}
