package org.feup.cmov.acmecustomer.models;

import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;

import java.io.Serializable;
import java.util.ArrayList;

public class ShoppingCart implements Serializable, QRCodeInterface {
    private ArrayList<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product p) {
        this.products.add(p);
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public double getValue() {
        double value = 0.0;
        for(int i = 0; i < this.products.size(); i++) {
            value += this.products.get(i).getFullPrice();
        }
        return value;
    }

    public String encode() {
        String s = "";
        for(int i = 0; i < this.products.size(); i++) {
            Product currentProduct = this.products.get(i);
            s += "acme" + currentProduct.getUUID().toString() + currentProduct.getPrice().getEuros() + currentProduct.getPrice().getCents() + currentProduct.getName() + "\n";
        }
        return s;
    }
}
