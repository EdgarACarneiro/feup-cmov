package org.feup.cmov.acmecustomer.models;

public class Item {

    private String name;
    private ItemPrice price;

    private class ItemPrice {
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

    public Item(String name, int euros, int cents) {
        this.name = name;
        this.price = new ItemPrice(euros, cents);
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
