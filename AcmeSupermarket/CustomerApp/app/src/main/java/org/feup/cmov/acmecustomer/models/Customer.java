package org.feup.cmov.acmecustomer.models;

import com.google.gson.annotations.Expose;

import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;
import org.feup.cmov.acmecustomer.services.KeyStoreHandler;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.Signature;
import java.util.ArrayList;

public class Customer implements Serializable {
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

}
