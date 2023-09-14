package com.oluwaseun.dronedispatch.exception;

public class UnauthorisedException extends RuntimeException {

    public UnauthorisedException(String message) {
        super(message);
    }
}
