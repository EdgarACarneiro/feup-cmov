package org.feup.cmov.acmecustomer.models;

import org.feup.cmov.acmecustomer.services.GetTransactions;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TransactionRecord {
    private Customer customer;
    private int total;
    private int discont;
    private boolean usedVoucher;
    private Date date;

    public TransactionRecord(Customer customer, boolean usedVoucher, int discont, int total, Date date) {
        this.customer = customer;
        this.total = total;
        this.discont = discont;
        this.usedVoucher = usedVoucher;
        this.date = date;
    }

    public TransactionRecord(Customer customer, GetTransactions.ServerTransaction transaction) {
        this.customer = customer;
        this.total = transaction.getTotal();
        this.discont = transaction.getDiscounted();
        this.usedVoucher = transaction.usedVoucher();
        try {
            this.date = new SimpleDateFormat("dd-MM-yyyy")
                            .parse(transaction.getDate());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Customer getCustomer() {
        return customer;
    }

    public int getTotal() {
        return total;
    }

    public int getDiscount() {
        return discont;
    }
    
    public boolean usedVoucher() {
        return usedVoucher;
    }

    public Date getDate() {
        return date;
    }
}
