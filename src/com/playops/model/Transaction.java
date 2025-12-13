package com.playops.model;

import java.time.LocalDateTime;

public class Transaction {
    // Attributes
    public enum Type{
        BUY,
        RENT
    }
    private final Product product;
    private final Customer customer;
    private final Type type;
    private final double amount;
    private final LocalDateTime timestamp;

    // Constructor
    public Transaction(Product product, Customer customer, Type type, double amount) {
        if (product == null || customer == null){
            throw new IllegalArgumentException("Product/Customer cannot be null");
        }
        if (amount <= 0)
            throw new IllegalArgumentException("Transaction amount must be positive");

        this.product = product;
        this.customer = customer;
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    // Getters
    public double getAmount(){return amount;}
    public Type getType(){return type;}
    public LocalDateTime getTimestamp(){return timestamp;}

    @Override
    public String toString(){
        return type + " | " + product.getName() +
                " | $" + amount +
                " | " + timestamp;
    }

}
