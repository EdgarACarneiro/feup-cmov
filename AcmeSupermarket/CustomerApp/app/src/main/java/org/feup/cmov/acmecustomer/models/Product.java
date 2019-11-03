package org.feup.cmov.acmecustomer.models;

import java.io.Serializable;
import java.util.UUID;

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

}
