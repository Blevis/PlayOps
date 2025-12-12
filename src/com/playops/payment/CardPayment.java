package com.playops.payment;

import com.playops.exceptions.PaymentException;
import com.playops.interfaces.PaymentMethod;

public class CardPayment implements PaymentMethod{
    // Attributes
    private final String cardNumber; // String cause can start with 0
    private final String cardHolder;

    // Constructor
    public CardPayment(String cardNumber, String cardHolder) {
        validateCardNumber(cardNumber);
        validateCardHolder(cardHolder);
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
    }

    // Getters
    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    // Validation
    private void validateCardNumber(String cardNumber){
        if (cardNumber == null || cardNumber.isBlank())
            throw new IllegalArgumentException("Card number cannot be blank");
        if(!cardNumber.matches("\\d{12,19}"))
            throw new IllegalArgumentException("Card number must contain 12-19 digits");
    }
    private void validateCardHolder(String cardHolder){
        if (cardHolder == null || cardHolder.isBlank())
            throw new IllegalArgumentException("Card holder name cannot be blank");
    }

    // Pay - Other methods
    @Override
    public void pay(double amount) throws PaymentException {
        if (amount <= 0) {
            throw new PaymentException("Invalid payment amount");
        }
        System.out.println("Paid " + amount + " using card under holder: " + cardHolder);
    }
}
