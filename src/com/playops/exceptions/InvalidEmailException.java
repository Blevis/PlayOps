package com.playops.exceptions;

public class InvalidEmailException  extends CustomException {
    public InvalidEmailException (String message) {
        super(message);
    }
}