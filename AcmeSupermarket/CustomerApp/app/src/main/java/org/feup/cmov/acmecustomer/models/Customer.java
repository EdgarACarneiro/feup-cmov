package org.feup.cmov.acmecustomer.models;

import com.google.gson.annotations.Expose;

import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;

import java.io.Serializable;
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

    public Customer(String username, String password, )

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

}
