package com.playops.model;
import com.playops.exceptions.PaymentException;
import com.playops.interfaces.Buyable;

public class Game extends Product implements Buyable {
    // Attributes
    private GameGenre genre;
    private GamePlatform platform;
    private double pricePerDay;

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
    protected Game(String name, String description, int year, int quantity, double price, double pricePerDay, GameGenre genre, GamePlatform platform){
        super(name, description, year, quantity, price);
        setPricePerDay(pricePerDay);
        this.genre = genre;
        this.platform = platform;
    }

    // Getters
    public double getPricePerDay() {return pricePerDay;}
    public GameGenre getGenre() {return genre;}
    public GamePlatform getPlatform() {return platform;}

    // Setters
    public void setPricePerDay(double pricePerDay){
        validatePrice(pricePerDay);
        this.pricePerDay = pricePerDay;
    }

    // Buy - Other methods
    @Override
    public double buy() throws PaymentException{
        if(!isAvailable()) throw new PaymentException("No stock available");
        setQuantity(getQuantity()-1);
        return getPrice();
    }
    public double calculateRentalPrice(int days) {
        if (days <= 0)
            throw new IllegalArgumentException("Rental days must be positive");

        return pricePerDay * days;
    }

    // To string
    @Override
    public String toString() {
        return super.toString() + " | " + genre + " | " + platform;
    }


}
