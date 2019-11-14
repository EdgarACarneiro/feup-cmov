package org.feup.cmov.acmecustomer.models;

import org.feup.cmov.acmecustomer.Constants;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.ArrayList;

public class ShoppingCart implements Serializable {

    private ArrayList<Product> products;

    public ShoppingCart() {
        this.products = new ArrayList<>();
    }

    public void addProduct(Product p) {
        this.products.add(p);
    }

    public void removeProduct(int position) {
        this.products.remove(position);
    }

    public ArrayList<Product> getProducts() {
        return this.products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    public void clearProducts() {
        this.products = new ArrayList<>();
    }

    public double getValue() {
        double value = 0.0;
        for(int i = 0; i < this.products.size(); i++) {
            value += this.products.get(i).getFullPrice();
        }
        return value;
    }

    public byte[] getAsBytes() {
        final int ACME_TAG_LENGTH = 4;
        final int PRODUCT_CODE = 16;
        final int PRICE_EURO_LENGTH = 4;
        final int PRICE_CENTS_LENGTH = 4;

        ByteBuffer tags;
        int tagsLength = 0;

        for(int i = 0; i < this.products.size(); i++) {
            tagsLength += ACME_TAG_LENGTH + PRODUCT_CODE + PRICE_EURO_LENGTH + PRICE_CENTS_LENGTH;
        }

        tags = ByteBuffer.allocate(tagsLength);

        for(int i = 0; i < this.products.size(); i++) {
            tags.putInt(Constants.ACME_TAG_ID);
            tags.putLong(this.products.get(i).getCode().getMostSignificantBits());
            tags.putLong(this.products.get(i).getCode().getLeastSignificantBits());
            tags.putInt(this.products.get(i).getPrice().getEuros());
            tags.putInt(this.products.get(i).getPrice().getCents());
        }

        return tags.array();
    }
}
