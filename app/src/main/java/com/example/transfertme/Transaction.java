package com.example.transfertme;

public class Transaction {
    private String senderNumber;
    private String receiverNumber;
    private double amount;
    private double fees;
    private double recipientReceive;
    private boolean transactionStatus;
    private String transactionDate;
    private String transactionTime;
    private String userId;

    public Transaction() {
        // Constructeur par défaut requis pour Firebase
    }

    public Transaction(String senderNumber, String receiverNumber, double amount, double fees, double recipientReceive) {
        this.senderNumber = senderNumber;
        this.receiverNumber = receiverNumber;
        this.amount = amount;
        this.fees = fees;
        this.recipientReceive = recipientReceive;
    }

    public String getSenderNumber() {
        return senderNumber;
    }

    public void setSenderNumber(String senderNumber) {
        this.senderNumber = senderNumber;
    }

    public String getReceiverNumber() {
        return receiverNumber;
    }

    public void setReceiverNumber(String receiverNumber) {
        this.receiverNumber = receiverNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getFees() {
        return fees;
    }

    public void setFees(double fees) {
        this.fees = fees;
    }

    public double getRecipientReceive() {
        return recipientReceive;
    }

    public void setRecipientReceive(double recipientReceive) {
        this.recipientReceive = recipientReceive;
    }

    public boolean isTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(boolean transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(String transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
