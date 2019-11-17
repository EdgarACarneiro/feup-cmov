package org.feup.cmov.acmecustomer.models;

import java.util.UUID;

public class Coupon {

    private UUID id;

    public Coupon(String id) {
        this.id = UUID.fromString(id);
    }

    public String getDescription() {
        return "Voucher "  + id.toString();
    }

    public UUID getId() {
        return id;
    }
}
