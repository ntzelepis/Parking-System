package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.ParkingSpace;
import com.nikko.parkingsystem.model.Payment;
import com.nikko.parkingsystem.model.SpaceType;
import com.nikko.parkingsystem.model.Vehicle;
import com.nikko.parkingsystem.service.ParkingSessionService;
import com.nikko.parkingsystem.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/sessions")
public class ParkingSessionController {

    private final ParkingSessionService sessionService;
    private final VehicleService vehicleService;

    public ParkingSessionController(ParkingSessionService sessionService, VehicleService vehicleService) {
        this.sessionService = sessionService;
        this.vehicleService = vehicleService;
    }

    @PostMapping("/park/{parkingName}/{spaceType}")
    public ResponseEntity<?> autoPark(@PathVariable String parkingName,
                                      @PathVariable SpaceType spaceType,
                                      @RequestBody Vehicle vehicle) {
        Optional<ParkingSpace> parkedSpace = sessionService.autoParkVehicle(parkingName, spaceType, vehicle);
        return parkedSpace
                .map(space -> ResponseEntity.ok("Vehicle parked successfully in space " + space.getSpaceCode()))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No available " + spaceType + " space in parking " + parkingName));
    }

    @PostMapping("/unpark/{vehicleId}")
    public ResponseEntity<?> unparkVehicle(@PathVariable Long vehicleId) {
        Payment payment = sessionService.unparkVehicle(vehicleId);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Unpark failed"));
        }
        return ResponseEntity.ok(payment);
    }

    @GetMapping("/{vehicleId}/analytics")
    public ResponseEntity<Map<String, Object>> getAnalytics(@PathVariable Long vehicleId) {
        Optional<Vehicle> vehicleOpt = vehicleService.findById(vehicleId);
        if (vehicleOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Map<String, Object> analytics = sessionService.getSessionAnalytics(vehicleOpt.get());
        return ResponseEntity.ok(analytics);
    }
}
