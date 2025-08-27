package com.nikko.parkingsystem.controller.exception;

public class ParkingResult {
    private final String spaceName;
    public ParkingResult(String spaceName) { this.spaceName = spaceName; }
    public String getSpaceName() { return spaceName; }
}