package org.feup.cmov.acmecustomer.models;

import java.util.Date;

public class PaymentInfo {
    private int cardNumber;
    private String cardOwner;
    private Date cardValidity;
    private int CVV;

    public PaymentInfo(int cardNumber, String cardOwner, Date cardValidity, int CVV) {
        this.cardNumber = cardNumber;
        this.cardOwner = cardOwner;
        this.cardValidity = cardValidity;
        this.CVV = CVV;
    }

}
