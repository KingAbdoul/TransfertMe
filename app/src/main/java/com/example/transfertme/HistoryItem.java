package com.example.transfertme;

public class HistoryItem {
    private String name;
    private String date;
    private double amount;

    public HistoryItem() {
    }

    public HistoryItem(String name, String date, double amount) {
        this.name = name;
        this.date = date;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public String getDate() {
        return date;
    }
    public void setDate(String date) { this.date = date; }

    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) { this.amount = amount; }
}
