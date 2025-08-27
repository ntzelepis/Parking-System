package com.nikko.parkingsystem.controller.exception;

public class ParkingSpaceNotFoundException extends RuntimeException {
    public ParkingSpaceNotFoundException(String message) {
        super(message);
    }
}
