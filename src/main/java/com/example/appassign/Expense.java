package com.example.appassign;

public class Expense {
    private int id;
    private String name;
    private double amount;

    public Expense() {
    }

    public Expense(int id, String name, double amount) {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Expense(double itemId, String itemName, double itemAmount) {
    }

    public Expense(String name, double amount) {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
