package com.playops.payment;

import com.playops.exceptions.PaymentException;
import com.playops.interfaces.PaymentMethod;

public class CashPayment implements PaymentMethod{
    private double cashProvided;

    public CashPayment(double cashProvided){
        if (cashProvided < 0)
            throw new IllegalArgumentException("Cash provided cannot be negative");
        this.cashProvided = cashProvided;
    }

    public double getCashProvided() {
        return cashProvided;
    }

    @Override
    public void pay(double amount) throws PaymentException{
        if(cashProvided < amount){
            throw new PaymentException("Not enough cash provided. Amount required: "+ amount);
        }
        System.out.println("Paid " + amount + " in cash. Change: " + (cashProvided - amount));
    }
}
