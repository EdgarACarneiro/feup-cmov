package org.feup.cmov.acmecustomer.models;



import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.Signature;
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

    private byte[] concaByteArrays(byte[] a, byte[] b) {
        byte[] res = new byte[a.length + b.length];
        System.arraycopy(a, 0, res, 0, a.length);
        System.arraycopy(b, 0, res, a.length, b.length);

        return res;
    }

    public byte[] encode(PrivateKey privateKey) {
        final int ACME_TAG_LENGTH = 4;
        final int PRODUCT_CODE = 16;
        final int PRICE_EURO_LENGTH = 4;
        final int PRICE_CENTS_LENGTH = 4;
        ByteBuffer tag;
        int tagsLength = 0;

        for(int i = 0; i < this.products.size(); i++) {
            tagsLength += ACME_TAG_LENGTH + PRODUCT_CODE + PRICE_EURO_LENGTH + PRICE_CENTS_LENGTH;
        }

        tag = ByteBuffer.allocate(tagsLength);

        for(int i = 0; i < this.products.size(); i++) {
            tag.putInt(0x41636D65);
            tag.putLong(this.products.get(i).getUUID().getMostSignificantBits());
            tag.putLong(this.products.get(i).getUUID().getLeastSignificantBits());
            tag.putInt(this.products.get(i).getPrice().getEuros());
            tag.putInt(this.products.get(i).getPrice().getCents());
        }

        try {
            Signature sig = Signature.getInstance("SHA256WithRSA");
            sig.initSign(privateKey);
            sig.update(tag);
            return concaByteArrays(tag.array(), sig.sign());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
