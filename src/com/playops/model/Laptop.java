package com.playops.model;

public class Laptop extends Hardware {
    // Attributes
    public enum Brand { DELL, HP, LENOVO, ASUS, ACER, APPLE }
    private Brand brand;
    private String model;
    private double screenSize;

    // Constructor
    public Laptop(String name, String description, int year, int quantity,
                  double price, Brand brand, String model, double screenSize) {
        super(name, description, year, quantity, price);
        this.brand = brand;
        setModel(model);
        setScreenSize(screenSize);
    }

    // Getters
    public Brand getBrand() { return brand; }
    public String getModel() { return model; }
    public double getScreenSize() { return screenSize; }

    // Setters with validation
    public void setModel(String model){
        validateModel(model);
        this.model = model;
    }

    // - Here I did validation within the setter since it's the last child and won't expand further.
    public void setScreenSize(double screenSize){
        if(screenSize <= 0)
            throw new IllegalArgumentException("Screen size must be positive");
        this.screenSize = screenSize;
    }

    // To string
    @Override
    public String toString(){
        return super.toString() + " | Laptop | " + brand + " " + model + " | " + screenSize + " inch";
    }
}