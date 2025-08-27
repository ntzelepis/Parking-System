package com.nikko.parkingsystem.payload;

import com.nikko.parkingsystem.model.Vehicle;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AutoParkRequest {
    private String parkingName;
    private String spaceType;
    private Vehicle vehicle;
    private String requestedSpace;

    public static class VehiclePayload {
        private String licensePlate;
        private String model;
        private String type;
    }

}
