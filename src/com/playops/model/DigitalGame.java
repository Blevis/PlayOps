package com.playops.model;

public class DigitalGame extends Game {
    // Constructor
    public DigitalGame(String name, String description, int year, int quantity,
                       double price, GameGenre genre, GamePlatform platform) {
        super(name, description, year, quantity, price, 0, genre, platform); // pricePerDay = 0
    }

    @Override
    public String toString() {
        return super.toString() + " | Digital, Buy only";
    }
}
