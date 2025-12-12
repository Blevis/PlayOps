package com.playops.model;

public class Desktop extends Hardware {
    // Attributes
    public enum Brand { DELL, HP, LENOVO, ASUS, ACER, CUSTOM }
    private Brand brand;
    private String model;

    // Constructor
    public Desktop(String name, String description, int year, int quantity,
                   double price, Brand brand, String model) {
        super(name, description, year, quantity, price);
        this.brand = brand;
        setModel(model);
    }

    // Getters
    public Brand getBrand() { return brand; }
    public String getModel() { return model; }

    // Setters with validation
    public void setModel(String model){
        validateModel(model);
        this.model = model;
    }

    // To string
    @Override
    public String toString(){
        return super.toString() + " | Desktop | " + brand + " " + model;
    }
}
