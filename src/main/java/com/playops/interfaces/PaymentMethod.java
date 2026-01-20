package com.playops.interfaces;

import com.playops.exceptions.PaymentException;

public interface PaymentMethod {
    void pay(double amount) throws PaymentException;
}
