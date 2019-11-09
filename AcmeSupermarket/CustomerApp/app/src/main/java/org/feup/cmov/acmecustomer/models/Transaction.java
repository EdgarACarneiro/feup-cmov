package org.feup.cmov.acmecustomer.models;

import java.util.Date;

public class Transaction {
    private Customer customer;
    private ShoppingCart cart;
    private Coupon coupon;
    private boolean isDiscounted;
    private Date date;

    public Transaction(Customer customer, ShoppingCart cart, Coupon coupon, boolean isDiscounted) {
        this.customer = customer;
        this.cart = cart;
        this.coupon = coupon;
        this.isDiscounted = isDiscounted;
        this.date = new Date();
    }

    public Transaction(Customer customer, ShoppingCart cart, Coupon coupon, boolean isDiscounted, Date date) {
        this.customer = customer;
        this.cart = cart;
        this.coupon = coupon;
        this.isDiscounted = isDiscounted;
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public ShoppingCart getCart() {
        return cart;
    }

    public Coupon getCoupon() {
        return coupon;
    }

    public boolean isDiscounted() {
        return isDiscounted;
    }

    public Date getDate() {
        return date;
    }
}
