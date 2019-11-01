package org.feup.cmov.acmecustomer.models;

import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements QRCodeInterface, Serializable {
    private CustomerMetadata metadata;
    private PaymentInfo paymentInfo;
    private ShoppingCart currentCart;

    public Customer(String name, String username, String password, PaymentInfo paymentInfo) {
        this.metadata = new CustomerMetadata(name, username, password);
        this.paymentInfo = paymentInfo;
        this.currentCart = new ShoppingCart();
    }

    public Customer(String uuid, String name, String username, String password, PaymentInfo paymentInfo) {
        this.metadata = new CustomerMetadata(uuid, name, username, password);
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

    public double getShoppingCartValue() {
        return this.currentCart.getValue();
    }

    public ArrayList<Product> getShoppingCart() {
        return this.currentCart.getShoppingCart();
    }

    public void setShoppingCart(ArrayList<Product> products) {
        this.currentCart.setShoppingCart(products);
    }

    public String encode() {
        return "";
    }

}
