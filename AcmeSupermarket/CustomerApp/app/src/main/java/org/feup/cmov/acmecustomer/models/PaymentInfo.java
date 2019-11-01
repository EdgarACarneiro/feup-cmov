package org.feup.cmov.acmecustomer.models;

import java.io.Serializable;

public class PaymentInfo implements Serializable {
    private double cardNumber;
    private String cardOwner;
    private CardValidity cardValidity;
    private int CVV;

    protected class CardValidity implements Serializable {
        private int month;
        private int year;

        protected CardValidity(int month, int year) {
            this.month = month;
            this.year = year;
        }

        protected int getMonth() {
            return this.month;
        }

        protected int getYear() {
            return this.year;
        }
    }

    public PaymentInfo(double cardNumber, String cardOwner, int month, int year, int CVV) {
        this.cardNumber = cardNumber;
        this.cardOwner = cardOwner;
        this.cardValidity = new CardValidity(month, year);
        this.CVV = CVV;
    }

    public double getCardNumber() {
        return this.cardNumber;
    }

    public String getCardOwner() {
        return this.cardOwner;
    }

    public int getValidityMonth() {
        return this.cardValidity.getMonth();
    }

    public int getValidityYear() {
        return this.cardValidity.getYear();
    }

}
