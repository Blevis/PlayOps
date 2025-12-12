package com.playops.interfaces;
import com.playops.exceptions.PaymentException;

public interface Buyable {
    double buy() throws PaymentException;
}
