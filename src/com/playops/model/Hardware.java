package com.playops.model;

import com.playops.interfaces.Buyable;
import com.playops.exceptions.PaymentException;

public abstract class Hardware extends Product implements Buyable {
    // Constructor
    protected Hardware(String name, String description, int year, int quantity, double price) {
        super(name, description, year, quantity, price);
    }

    // Validation
    protected void validateModel(String model) {
        if (model == null || model.isBlank())
            throw new IllegalArgumentException("Model cannot be blank");
    }

    // Buy - Other Methods
    @Override
    public double buy() throws PaymentException {
        if (!isAvailable()) throw new PaymentException("No stock available");
        decreaseQuantity(1);
        return getPrice();
    }

    // To string
    @Override
    public String toString() {
        return super.toString() + " | Hardware";
    }
}