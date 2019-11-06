package org.feup.cmov.acmecustomer.models;

public class Transaction {
    private ShoppingCart cart;
    private Coupon coupon;
    private boolean isDiscounted;

    public Transaction(ShoppingCart cart, Coupon coupon, boolean isDiscounted) {
        this.cart = cart;
        this.coupon = coupon;
        this.isDiscounted = isDiscounted;
    }
}
