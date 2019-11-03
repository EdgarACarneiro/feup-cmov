package org.feup.cmov.acmecustomer.models;

import org.feup.cmov.acmecustomer.interfaces.QRCodeInterface;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.PrivateKey;
import java.util.ArrayList;

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
        byte[] encryptedTag;
        ByteArrayOutputStream tag = new ByteArrayOutputStream();

        for(int i = 0; i < this.products.size(); i++) {
            Product currentProduct = this.products.get(i);

            int tagLength = ACME_TAG_LENGTH + UUID_LENGTH + PRICE_EURO_LENGTH + PRICE_CENTS_LENGTH + DESCRIPTION_SIZE_LENGTH + currentProduct.getName().length();
            /*tag = ByteBuffer.allocate(tagLength);
            tag.put("Acme".getBytes(StandardCharsets.ISO_8859_1));
            tag.putLong(currentProduct.getUUID().getMostSignificantBits());
            tag.putLong(currentProduct.getUUID().getLeastSignificantBits());
            tag.putInt(currentProduct.getPrice().getEuros());
            tag.putInt(currentProduct.getPrice().getCents());
            tag.put((byte)currentProduct.getName().length());
            tag.put(currentProduct.getName().getBytes(StandardCharsets.ISO_8859_1));*/
            try {
                tag.write("Acme".getBytes(StandardCharsets.ISO_8859_1));
                tag.write(ByteBuffer.allocate(Long.BYTES).putLong(currentProduct.getUUID().getMostSignificantBits()).array());
                tag.write(ByteBuffer.allocate(Long.BYTES).putLong(currentProduct.getUUID().getLeastSignificantBits()).array());
                tag.write(currentProduct.getPrice().getEuros());
                tag.write(currentProduct.getPrice().getCents());
                tag.write(currentProduct.getName().length());
                tag.write(currentProduct.getName().getBytes(StandardCharsets.ISO_8859_1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            Cipher cipher = Cipher.getInstance(ENC_ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            encryptedTag = cipher.doFinal(tag.toByteArray());
            System.out.println(encryptedTag);
            return encryptedTag;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new byte[0];
    }
}
