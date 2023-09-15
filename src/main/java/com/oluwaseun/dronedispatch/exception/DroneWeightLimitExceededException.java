package com.oluwaseun.dronedispatch.exception;

public class DroneWeightLimitExceededException extends RuntimeException {
    public DroneWeightLimitExceededException(String message) {
        super(message);
    }
}
