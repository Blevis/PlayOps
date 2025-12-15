package com.playops.model;

import java.time.Year;

public abstract class Product {
    // Attributes
    private static int nextId = 1;
    private final int id;
    private String name;
    private String description;
    private int year;
    private int quantity;
    private double price;           // to buy, rent to be handled in Game since only it's children will ever have a use for it

    // Constructor
    protected Product(String name, String description, int year, int quantity, double price) {
        this.id = nextId++;
        setName(name);
        setDescription(description);
        setYear(year);
        setQuantity(quantity);
        setPrice(price);
    }

    // Getters
    public int getId() {return id;}
    public String getName() {return name;}
    public String getDescription() {return description;}
    public int getYear() {return year;}
    public int getQuantity() {return quantity;}
    public double getPrice() {return price;}

    // Setters
    public void setName(String name){
        validateName(name);
        this.name = name;
    }
    public void setDescription(String description){
        validateDescription(description);
        this.description = description;
    }
    public void setYear(int year){
        validateYear(year);
        this.year = year;
    }
    public void setQuantity(int quantity){
        validateQuantity(quantity);
        this.quantity = quantity;
    }
    public void setPrice(double price){
        validatePrice(price);
        this.price = price;
    }

    // Validation
    protected void validateName(String name){
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Name cannot be blank");
    }
    protected void validateDescription(String description){
        if (description == null || description.isBlank())
            throw new IllegalArgumentException("Description cannot be blank");
    }
    protected void validateYear(int year){
        if (year < 0)
            throw new IllegalArgumentException("Year cannot be negative");
        if (year > Year.now().getValue())
            throw new IllegalArgumentException("Year cannot exceed current calendar year");
    }
    protected void validateQuantity(int quantity){
        if (quantity < 0)
            throw new IllegalArgumentException("Quantity cannot be negative");
    }
    protected void validatePrice(double price){
        if (price < 0)
            throw new IllegalArgumentException("Price cannot be negative");
    }

    // Other methods
    public boolean isAvailable() {
        return getQuantity() > 0;
    }
    public void decreaseQuantity(int amount) {
        if (amount <= 0)
            throw new IllegalArgumentException("Decrease amount must be positive");
        if (quantity < amount)
            throw new IllegalArgumentException("Not enough quantity to decrease from");

        quantity -= amount;
    }
    public int decreaseQuantityAndReturn(int amount){
        decreaseQuantity(amount);
        return quantity;
    }
    public void increaseQuantity(int amount){
        if (amount <= 0)
            throw new IllegalArgumentException("Increase amount must be positive");
        quantity += amount;
    }
    public int increaseQuantityAndReturn(int amount){
        increaseQuantity(amount);
        return quantity;
    }

    // To string
    @Override
    public String toString(){
        return "[" + id + "] " + name + " | " + description +
                " | Year: " + year + " | Quantity: " + quantity +
                " | $" + price;
    }
}