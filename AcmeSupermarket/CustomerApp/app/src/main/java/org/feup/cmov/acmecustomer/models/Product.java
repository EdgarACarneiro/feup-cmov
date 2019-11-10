package org.feup.cmov.acmecustomer.models;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.UUID;

import static org.feup.cmov.acmecustomer.Utils.encode;

public class Product implements Serializable {

    private UUID UUID;
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
        this.UUID = UUID.fromString(uuid);
        this.name = name;
        this.price = new ItemPrice(euros, cents);
    }

    public Product(byte[] content) {
        String acmeSig = encode(Arrays.copyOfRange(content, 0, 4));
        content = Arrays.copyOfRange(content, 4, content.length);

        // Extracting Product Code
        ByteBuffer buffer = ByteBuffer.wrap(content);
        this.UUID = new UUID(
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

    public UUID getUUID() {
        return this.UUID;
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

    public byte[] getProductAsByes() {
        return null;
    }
}
