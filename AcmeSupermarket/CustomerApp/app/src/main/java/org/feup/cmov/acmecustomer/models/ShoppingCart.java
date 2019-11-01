package org.feup.cmov.acmecustomer.models;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingCart implements Serializable {
    private ArrayList<Product> shoppingCart;

    public ShoppingCart() {
        this.shoppingCart = new ArrayList<>();
    }

    public void addProduct(Product p) {
        this.shoppingCart.add(p);
    }

    public ArrayList<Product> getShoppingCart() {
        return this.shoppingCart;
    }

    public void setShoppingCart(ArrayList<Product> shoppingCart) {
        this.shoppingCart = shoppingCart;
    }
}
