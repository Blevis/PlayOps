package com.playops.model;

import com.playops.interfaces.Buyable;

public class Game implements Buyable {
    // Properties
    private static int nextId = 1;
    private final int id;
    private String title;
    private int year;
    private GameGenre genre;
    private GamePlatform platform;
    private String description;
    private double pricePerDay;
    private int quantity;

    // Enumerations
    public enum GameGenre{
        ACTION,
        ADVENTURE,
        ROLE_PLAYING,
        SIMULATION,
        STRATEGY,
        SPORTS,
        RACING,
        FIGHTING,
        HORROR,
        PLATFORMER,
        PUZZLE,
        SANDBOX,
        SHOOTER,
        SURVIVAL,
        STEALTH
    }
    public enum GamePlatform { PC, MS_DOS, Xbox, PlayStation, NintendoSwitch, Nintendo64, GameBoy }

    // Constructor
    public Game(String title, int year, Game.GameGenre genre, GamePlatform platform, String description, double pricePerDay){
        this.id = nextId++;
        this.title = title;
        this.year = year;
        this.genre = genre;
        this.platform = platform;
        this.description = description;
        this.pricePerDay = pricePerDay;
    }

    // Get/Set Methods
    public int getId() {return id;}

    public String getTitle() {return title;}

    public void setTitle(String title) {this.title = title;}

    public int getYear() {return year;}

    public void setYear(int year) {this.year = year;}

    public GameGenre getGenre() {return genre;}

    public void setGenre(GameGenre genre) {this.genre = genre;}

    public GamePlatform getPlatform() {return platform;}

    public void setPlatform(GamePlatform platform) {this.platform = platform;}

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}

    public double getPricePerDay() {return pricePerDay;}

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    // Other Methods
    public double calculateRentalCost(int days) {
        return pricePerDay * days;
    }

    // To String Method
    @Override
    public String toString() {
        return "[" + id + "] " + title + " | " + year + " | " + genre + " | " + platform + " | $" + pricePerDay;
    }

}
