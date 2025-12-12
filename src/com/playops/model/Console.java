package com.playops.model;

public class Console extends Hardware {
    // Attributes
    public enum Brand { SONY, MICROSOFT, NINTENDO, VALVE }
    private Brand brand;
    private String model;

    // Constructor
    public Console(String name, String description, int year, int quantity,
                   double price, Brand brand, String model) {
        super(name, description, year, quantity, price);
        this.brand = brand;
        setModel(model);
    }

    // Getters
    public Brand getBrand() { return brand; }
    public String getModel() { return model; }

    // Setters
    public void setModel(String model){
        validateModel(model);
        this.model = model;
    }

    // To string
    @Override
    public String toString() {
        return super.toString() + " | Console | " + brand + " " + model;
    }
}