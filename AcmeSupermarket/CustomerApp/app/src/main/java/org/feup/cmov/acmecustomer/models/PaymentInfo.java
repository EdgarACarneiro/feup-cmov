package org.feup.cmov.acmecustomer.models;

public class PaymentInfo {
    private double cardNumber;
    private String cardOwner;
    private CardValidity cardValidity;
    private int CVV;

    protected class CardValidity {
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
