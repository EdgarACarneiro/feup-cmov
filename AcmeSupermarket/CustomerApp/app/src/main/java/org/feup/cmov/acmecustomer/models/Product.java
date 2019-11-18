package org.feup.cmov.acmecustomer.models;

import org.feup.cmov.acmecustomer.Constants;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import static org.feup.cmov.acmecustomer.Utils.encode;

public class Product implements Serializable {

    public static final int CHECKOUT_MSG_SIZE = 4 + 16 + 4 + 4;

    private UUID code;
    private String name;
    private ItemPrice price;

    protected class ItemPrice implements Serializable {
        private int euros;
        private int cents;

        public ItemPrice(int euros, int cents) {
            this.euros = euros;
            this.cents = cents;
        }

        public int getEuros() {
            return this.euros;
        }

        public int getCents() {
            return this.cents;
        }

        public double getFullPrice() {
            return Double.parseDouble(this.euros + "." + this.cents);
        }
    }

    public Product(String uuid, String name, int euros, int cents) {
        this.code = code.fromString(uuid);
        this.name = name;
        this.price = new ItemPrice(euros, cents);
    }

    public Product(byte[] content) {
        String acmeSig = encode(Arrays.copyOfRange(content, 0, 4));
        content = Arrays.copyOfRange(content, 4, content.length);

        // Extracting Product Code
        ByteBuffer buffer = ByteBuffer.wrap(content);
        this.code = new UUID(
                buffer.getLong(),
                buffer.getLong()
        );

        // Extracting Price
        Integer euros = buffer.getInt();
        Integer cents = buffer.getInt();
        this.price = new ItemPrice(euros, cents);

        // Extracting Product Name
        byte[] prodNameBytes = new byte[buffer.get()];
        buffer.get(prodNameBytes);
        this.name = encode(prodNameBytes);
    }

    public UUID getCode() {
        return this.code;
    }

    public String getName() {
        return this.name;
    }

    public ItemPrice getPrice() {
        return this.price;
    }

    public double getFullPrice() {
        return this.price.getFullPrice();
    }

    public byte[] getProductAsBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(CHECKOUT_MSG_SIZE);
        buffer.putInt(Constants.ACME_TAG_ID);
        buffer.putLong(this.code.getMostSignificantBits());
        buffer.putLong(this.code.getLeastSignificantBits());
        buffer.putInt(this.price.euros);
        buffer.putInt(this.price.euros);
        return buffer.array();
    }
}
