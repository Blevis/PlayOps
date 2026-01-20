package com.playops.exceptions;

public class FileProcessingException extends CustomException {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause /*throwable cause IO errors like to be system level so it'd prolly be a headache to deal with it pa e dit ca ka*/) {
        super(message);
        initCause(cause);
    }
}