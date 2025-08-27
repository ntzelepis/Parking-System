package com.nikko.parkingsystem.controller.exception;

public class NoAvailableSpaceException extends RuntimeException {
    public NoAvailableSpaceException(String message) {
        super(message);
    }
}
