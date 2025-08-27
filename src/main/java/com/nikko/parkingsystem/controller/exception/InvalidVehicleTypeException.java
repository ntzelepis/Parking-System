package com.nikko.parkingsystem.controller.exception;

public class InvalidVehicleTypeException extends RuntimeException {
    public InvalidVehicleTypeException(String message) { super(message); }
}
