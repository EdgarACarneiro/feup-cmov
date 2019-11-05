package org.feup.cmov.acmecustomer.models;

import android.security.keystore.KeyProperties;

import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.Base64;

import javax.crypto.Cipher;

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

    public byte[] encode(PrivateKey privateKey) {
        final int ACME_TAG_LENGTH = 4;
        final int UUID_LENGTH = 16;
        final int PRICE_EURO_LENGTH = 4;
        final int PRICE_CENTS_LENGTH = 4;
        final int DESCRIPTION_SIZE_LENGTH = 1;
        final String ENC_ALGO = "RSA/NONE/PKCS1Padding";
        ByteBuffer tag;
        int tagLength = 0;

        for(int i = 0; /*i < this.products.size()*/ i < 1; i++) {
            tagLength += ACME_TAG_LENGTH + UUID_LENGTH + PRICE_EURO_LENGTH + PRICE_CENTS_LENGTH + DESCRIPTION_SIZE_LENGTH + this.products.get(i).getName().length();
        }

        tag = ByteBuffer.allocate(tagLength);

        for(int i = 0; /*i < this.products.size()*/ i < 1; i++) {
            tag.putInt(0x41636D65);
            tag.putLong(this.products.get(i).getUUID().getMostSignificantBits());
            tag.putLong(this.products.get(i).getUUID().getLeastSignificantBits());
            tag.putInt(this.products.get(i).getPrice().getEuros());
            tag.putInt(this.products.get(i).getPrice().getCents());
            tag.put((byte)this.products.get(i).getName().length());
            tag.put(this.products.get(i).getName().getBytes(StandardCharsets.ISO_8859_1));
        }

        try {
            Cipher cipher = Cipher.getInstance(ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] encTag = cipher.doFinal(tag.array());
            System.out.println("ENCRYPTED TAG: " + StandardCharsets.ISO_8859_1.decode(tag).toString());
            return encTag;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
