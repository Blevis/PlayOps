package com.playops.interfaces;
import com.playops.exceptions.InsufficientQuantityException;
import com.playops.exceptions.PaymentException;

public interface Rentable {
    double rent(int days) throws InsufficientQuantityException, PaymentException;
}
