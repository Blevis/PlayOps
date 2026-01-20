package com.playops.model;
import com.playops.interfaces.Rentable;
import com.playops.exceptions.PaymentException;
import com.playops.exceptions.InsufficientQuantityException;

public class PhysicalGame extends Game implements Rentable {
    // Constructor
    public PhysicalGame(String name, String description, int year, int quantity, double price, double pricePerDay, GameGenre genre, GamePlatform platform){
        super(name, description, year, quantity, price, pricePerDay, genre, platform);
    }

    // Rent - Other methods
    @Override
    public double rent(int days) throws InsufficientQuantityException, PaymentException{
        if (days <= 0) throw new IllegalArgumentException("Days must be positive");
        if (!isAvailable()) throw new InsufficientQuantityException("No copies available to rent");

        decreaseQuantity(1);
        return getPricePerDay()*days;
    }

    // To string
    @Override
    public String toString() {
        return super.toString() + " | Rentable";
    }
}
