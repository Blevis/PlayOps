package com.playops.payment;

import com.playops.exceptions.PaymentException;
import com.playops.interfaces.PaymentMethod;

public class PaymentProcessor {
    public void process(PaymentMethod method, double amount) throws PaymentException{
        if (method == null)
            throw new IllegalArgumentException("Payment method cannot be null");
        method.pay(amount);
    }
}
