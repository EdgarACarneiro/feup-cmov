package org.feup.cmov.acmecustomer.models;

public class Coupon {
    private Integer id;

    public Coupon(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return "Voucher "  + this.id.toString();
    }
}
