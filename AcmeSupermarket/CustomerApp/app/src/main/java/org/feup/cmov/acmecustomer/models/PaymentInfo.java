package org.feup.cmov.acmecustomer.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

public class PaymentInfo implements Serializable {
    @Expose
    private String cardNumber;
    private String cardOwner;
    @Expose
    private CardValidity cardValidity;
    @Expose
    private int CVV;

    protected class CardValidity implements Serializable {
        @Expose
        private int month;
        @Expose
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

    public PaymentInfo(String cardNumber, String cardOwner, int month, int year, int CVV) {
        this.cardNumber = cardNumber;
        this.cardOwner = cardOwner;
        this.cardValidity = new CardValidity(month, year);
        this.CVV = CVV;
    }

    public String getMaskedCardNumber(String mask) {

        int index = 0;
        StringBuilder maskedNumber = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            char c = mask.charAt(i);
            if (c == '#') {
                maskedNumber.append(cardNumber.charAt(index));
                index++;
            } else if (c == 'x') {
                maskedNumber.append(c);
                index++;
            } else {
                maskedNumber.append(c);
            }
        }

        // return the masked number
        return maskedNumber.toString();
    }

    public String getCardNumber() {
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
