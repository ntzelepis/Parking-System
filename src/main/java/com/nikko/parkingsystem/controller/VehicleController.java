package com.nikko.parkingsystem.controller;

import com.nikko.parkingsystem.model.ParkingSession;
import com.nikko.parkingsystem.model.Vehicle;
import com.nikko.parkingsystem.model.VehicleType;
import com.nikko.parkingsystem.service.VehicleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }

    @GetMapping("/{licensePlate}")
    public Vehicle getVehicle(@PathVariable String licensePlate) {
        return vehicleService.getVehicle(licensePlate);
    }

    @GetMapping("/{licensePlate}/sessions")
    public List<ParkingSession> getSessions(@PathVariable String licensePlate) {
        return vehicleService.getSessionsByLicensePlate(licensePlate);
    }

    @PostMapping("/create")
    public Vehicle createVehicle(@RequestParam String licensePlate,
                                       @RequestParam String model,
                                       @RequestParam VehicleType type) {
        return vehicleService.createVehicle(licensePlate, model, type);
    }

    @PostMapping
    public Vehicle findVehicle(@RequestParam String licensePlate) {
        return vehicleService.findVehicle(licensePlate);
    }
}
