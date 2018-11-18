package com.sample.model;

/**
 * Created by RAJESH on 11/17/2018.
 */
public class Transaction {

    private int transactionId;
    private int sourceAcctNumber;
    private int destAcctNumber;
    private int amount;

    public Transaction() {

    }

    public Transaction(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getSourceAcctNumber() {
        return sourceAcctNumber;
    }

    public void setSourceAcctNumber(int sourceAcctNumber) {
        this.sourceAcctNumber = sourceAcctNumber;
    }

    public int getDestAcctNumber() {
        return destAcctNumber;
    }

    public void setDestAcctNumber(int destAcctNumber) {
        this.destAcctNumber = destAcctNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                '}';
    }
}
